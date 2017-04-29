package org.nryotaro.edgar.client

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Configurable
class EdgarClientContext {

    @Bean fun client(@Value("\${url.root}") edgarRootUrl: String): WebClient {
       return WebClient.create(edgarRootUrl)
    }

}

interface EdgarClient {
    fun getRawResponse(url: String): Mono<ClientResponse>
    fun get(url: String): Mono<String>
    fun getBin(url: String): Mono<DataBuffer>
}

@Service
class EdgarClientImpl(val client: WebClient,
                      @Value("\${url.root}") val edgarRootUrl: String): EdgarClient {

    override  fun getRawResponse(url: String): Mono<ClientResponse> {
       return client.get().uri(cutEdgarRootUrl(url)).exchange()
    }

    override fun get(url: String): Mono<String> {
        return client.get().uri(cutEdgarRootUrl(url)).exchange().flatMap{
            if(it.statusCode().is2xxSuccessful) it.bodyToMono(String::class) else Mono.empty()
        }
    }

    /**
     * TODO write test
     */
    override fun getBin(url: String): Mono<DataBuffer> {
        return client.get().uri(cutEdgarRootUrl(url)).exchange().flatMapMany {
            it.body(BodyExtractors.toDataBuffers())
        }.reduce{acc, b -> acc.write(b)}
    }


    private fun cutEdgarRootUrl(url: String): String {
        return url.substringAfter(edgarRootUrl)
    }
}