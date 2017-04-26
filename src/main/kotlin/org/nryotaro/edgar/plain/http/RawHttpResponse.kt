package org.nryotaro.edgar.plain.http

import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono

data class RawHttpResponse(val status: HttpStatus, val body: Mono<String>)

