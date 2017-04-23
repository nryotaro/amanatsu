package org.nryotaro.edgar.client

import org.nryotaro.edgar.url.Builder
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.LocalDate

@Configurable
class EdgarClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
       return WebClient.create(edgarRootUrl)
    }

}

@Service
class EdgarClient(val client: WebClient, val builder: Builder) {

    fun retrieveIndex(date: LocalDate) {
        val c = builder.buildIndex(date)
        val cc = client.get().uri(builder.buildIndex(date)).exchange().flatMap { e->
            e.bodyToMono(String::class)
        }.block()

        println(cc)
    }
}