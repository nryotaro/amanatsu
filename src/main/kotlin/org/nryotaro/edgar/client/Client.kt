package org.nryotaro.edgar.client

import org.nryotaro.edgar.url.Builder
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate

@Configurable
class ClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
       return WebClient.create(edgarRootUrl)
    }

}

class Client(val client: WebClient, val builder: Builder) {

    fun retrieveIndex(date: LocalDate) {
        /*
        client.get().uri {} = {

        }
        */
       val c= client.get().uri("Archives/edgar/daily-index/2017/QTR1/crawler.20170331.idx").exchange()
    }
}