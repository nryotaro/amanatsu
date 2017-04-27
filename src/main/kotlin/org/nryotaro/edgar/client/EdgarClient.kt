package org.nryotaro.edgar.client

import org.nryotaro.edgar.plain.http.RawHttpResponse
import org.nryotaro.edgar.url.Builder
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.io.File
import java.time.LocalDate

@Configurable
class EdgarClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
       return WebClient.create(edgarRootUrl)
    }

}

@Service
class EdgarClient(val client: WebClient, val builder: Builder) {

    /*
    fun retrieveIndex(date: LocalDate) {
        val cc = client.get().uri(builder.buildIndex(date)).exchange()
                .flatMap { e->
            e.bodyToMono(String::class)
        }.block()
    }
    */

    fun get(url: String): Mono<RawHttpResponse> {
        return client.get().uri(url).exchange().flatMap {
            Mono.justOrEmpty(RawHttpResponse(it.statusCode(), it.bodyToMono(String::class)))
        }
    }
}