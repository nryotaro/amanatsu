package org.nryotaro.edgar.client

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponse
import io.netty.handler.codec.http.LastHttpContent
import org.nryotaro.httpcli.HttpCli
import org.nryotaro.httpcli.handler.CliHandler
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.nryotaro.edgar.client.PartialHttpResponse as PHttpResponse
import io.netty.handler.codec.http.HttpContent as NettyHttpContent

@Repository
class AsyncEdgarClient: EdgarClient {
    val cli = HttpCli()

    override  fun get(url: String): Mono<FullHttpResponse> {
         return getResponse(url).reduce (
                Pair<Int, ByteArray>(-1, ByteArray(0)), { (first, second), b ->
            when(b) {
                is Status -> Pair(b.status, second)
                is PartialHttpContent -> Pair(first, byteArrayOf(*second,*b.content))
            }
        }).map { FullHttpResponse(it.first, it.second) }
    }

    override fun getResponse(url: String): Flux<PHttpResponse> {

        return Flux.create<PHttpResponse> {
            cli.get(url, object : CliHandler {
                override fun acceptContent(msg: NettyHttpContent) {
                    it.next(PartialHttpContent(toBytes(msg.content())))
                }

                override fun acceptHttpResponse(response: HttpResponse) {
                    it.next(Status(response.status().code()))
                }

                override fun acceptLastHttpContent(msg: LastHttpContent) {
                    it.next(PartialHttpContent(toBytes(msg.content())))
                    it.complete()
                }

                private fun toBytes(buf: ByteBuf): ByteArray {
                    val length = buf.readableBytes()
                    val array: ByteArray = ByteArray(length)
                    buf.getBytes(buf.readerIndex(), array)
                    return array
                }
                override fun onException(cause: Throwable) {
                    it.error(cause)
                }

                override fun onFailure(cause: Throwable) {
                    it.error(cause)
                }
            })
        }

    }
}
