package org.nryotaro.edgar.client

import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Path


interface EdgarClient {

    fun getRawResponse(url: String): Mono<ClientResponse>

    fun get(url: String): Mono<String>

    fun download(url: String, path: Path): Mono<Boolean>
}

sealed class PartialHttpResponse

data class Status(val status: Int): PartialHttpResponse()

data class PartialHttpContent(val content: ByteArray): PartialHttpResponse()

data class FullHttpResponse(val status: Int, val content: ByteArray)



