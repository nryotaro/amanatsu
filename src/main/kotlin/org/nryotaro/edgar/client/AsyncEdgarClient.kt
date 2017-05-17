package org.nryotaro.edgar.client

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpResponse
import org.nryotaro.edgar.client.HttpResponse as NHttpResponse
import io.netty.handler.codec.http.LastHttpContent
import org.nryotaro.httpcli.HttpCli
import org.nryotaro.httpcli.handler.CliHandler
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class AsyncEdgarClient {
    val cli = HttpCli()

    fun get(url: String) {
       //getResponse(url).reduce { Pair<Int?, ByteArray?>(), {a, b -> } }
    }

    fun getResponse(url: String): Flux<NHttpResponse> {

        return Flux.generate<NHttpResponse> {
            cli.get(url, object : CliHandler {

                override fun acceptHttpResponse(response: HttpResponse) {
                    it.next(Status(response.status().code()))
                }

                override fun acceptLastHttpContent(msg: LastHttpContent) {
                    it.next(HttpContent(toBytes(msg.content())))
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

                override fun acceptContent(msg: HttpContent) {
                    it.next(HttpContent(toBytes(msg.content())))
                }
            })
        }

    }

    //HttpResponse(val statusCode: Mono<Int>, val content: Flux<ByteArray>)
    /*
    fun getRawResponse(url: String): Mono<ClientResponse>

    fun get(url: String): Mono<String>

    fun download(url: String, path: Path): Mono<Boolean>
    */
}
