package org.nryotaro.edgar.repository

import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.client.EdgarClientTest
import org.nryotaro.edgar.plain.index.Indices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.io.File
import java.time.LocalDate


class IndexRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var indexRepository: IndexRepository

    @MockBean
    lateinit var client: EdgarClient

    @MockBean
    lateinit  var clientResponse: ClientResponse

    @Test
    fun retrieve() {
        val text =  this.javaClass.getResourceAsStream("crawler.20170314.idx").bufferedReader().use { it.readText() }
        `when`(clientResponse.statusCode()).thenReturn(HttpStatus.OK)
        `when`(clientResponse.bodyToMono(String::class.java)).thenReturn(Mono.just(text))
        `when`(client.getRawResponse("Archives/edgar/daily-index/2017/QTR1/crawler.20170314.idx"))
                .thenReturn(Mono.just(clientResponse))
        val indices: Indices =  indexRepository.retrieve(LocalDate.parse("2017-03-14")).block()

    }
}
