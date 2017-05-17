package org.nryotaro.edgar.client

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpResponse
import org.nryotaro.edgar.client.HttpResponse as NHttpResponse
import io.netty.handler.codec.http.LastHttpContent
import org.nryotaro.handler.CliHandler
import org.nryotaro.httpcli.HttpCli


class AsyncEdgarClient {
   val cli = HttpCli()

    /*
   fun getResponse(url: String):  NHttpResponse {
      cli.get(url, object: CliHandler {

         override fun acceptHttpResponse(response: HttpResponse) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

         override fun acceptLastHttpContent(msg: LastHttpContent) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

         override fun onException(ctx: ChannelHandlerContext, cause: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

         override fun onFailure(cause: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

         override fun acceptContent(msg: HttpContent) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
         }

      })
     */

   }

    //HttpResponse(val statusCode: Mono<Int>, val content: Flux<ByteArray>)
    /*
    fun getRawResponse(url: String): Mono<ClientResponse>

    fun get(url: String): Mono<String>

    fun download(url: String, path: Path): Mono<Boolean>
    */
}
