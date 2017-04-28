package org.nryotaro.edgar.client

import org.nryotaro.edgar.url.Builder
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ClientHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
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
interface EdgarClient {
    fun getRawResponse(url: String): Mono<ClientResponse>
    fun get(url: String): Mono<String>
}

class EdgarClientImpl(val client: WebClient, val builder: Builder): EdgarClient {

    override  fun getRawResponse(url: String): Mono<ClientResponse> {
       return client.get().uri(url).exchange()
    }

    override  fun get(url: String): Mono<String> {
        return client.get().uri(url).exchange().flatMap{
            if(it.statusCode().is2xxSuccessful) it.bodyToMono(String::class) else Mono.empty()
        }
    }
}