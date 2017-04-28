package org.nryotaro.edgar.repository

import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.plain.index.Indices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import java.time.LocalDate


class IndexRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var indexRepository: IndexRepository

    @MockBean
    lateinit var client: EdgarClient

    @MockBean
    lateinit var clientResponse: ClientResponse

    @Test
    fun retrieve() {
        `when`(clientResponse.statusCode()).thenReturn(HttpStatus.OK)
        `when`(clientResponse.bodyToMono(String::class.java))
                .thenReturn(Mono.just(readTextFile("crawler.20170314.idx", this::class)))
        `when`(client.getRawResponse("Archives/edgar/daily-index/2017/QTR1/crawler.20170314.idx"))
                .thenReturn(Mono.just(clientResponse))
        val indices: Indices =  indexRepository.retrieve(LocalDate.parse("2017-03-14")).block()

       assertThat(indices.filedDate, `is`(LocalDate.parse("2017-03-14")))
       assertThat(indices.indices,
               `is`(listOf(Index("1ST SOURCE CORP","DEF 14A", 34782,LocalDate.parse("2017-03-14"),
              "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"))))

    }

}
