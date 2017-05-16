package org.nryotaro.edgar.client

import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import java.nio.file.Path


interface EdgarClient {

    fun getRawResponse(url: String): Mono<ClientResponse>

    fun get(url: String): Mono<String>

    fun download(url: String, path: Path): Mono<Boolean>
}
