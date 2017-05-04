package org.nryotaro.edgar.retriever

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.plain.index.Indices
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import java.io.File
import java.time.LocalDate


class IndexRetrieverTest : EdgarTest() {
    val log: Logger = LoggerFactory.getLogger(IndexRetrieverTest::class.java)

    @Autowired
    lateinit var indexRepository: IndexRetriever

    @MockBean
    lateinit var client: EdgarClient

    @MockBean
    lateinit var clientResponse: ClientResponse

    @Test
    fun retrieve() {
        prepareMockHttpHandle()

        val dest = createTempDir()
        log.debug("the output directory: ${dest}")
        val indices: Indices =  indexRepository.retrieve(LocalDate.parse("2017-03-14"), dest).block()

        assertThat(indices.filedDate, `is`(LocalDate.parse("2017-03-14")))
        assertThat(indices.indices,
                `is`(listOf(Index("1ST SOURCE CORP","DEF 14A", 34782,LocalDate.parse("2017-03-14"),
                        "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"))))

        assertThat(File(dest, "Archives/edgar/daily-index/2017/QTR1/crawler.20170314.idx").exists(), `is`(true))
    }


    @Test
    fun retrieveFromLocal() {
        val dest = File(this.javaClass.getResource(".").toURI())
        val indices: Indices =  indexRepository.retrieve(LocalDate.parse("2017-03-14"), dest).block()

        assertThat(indices.filedDate, `is`(LocalDate.parse("2017-03-14")))
        assertThat(indices.indices,
                `is`(listOf(Index("1ST SOURCE CORP","DEF 14A", 34782, LocalDate.parse("2017-03-14"),
                        "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"))))
        verify(client, never()).getRawResponse(anyString())

    }

    private fun prepareMockHttpHandle() {
        `when`(clientResponse.statusCode()).thenReturn(HttpStatus.OK)
        `when`(clientResponse.bodyToMono(String::class.java))
                .thenReturn(Mono.just(readTextFile("crawler.20170314.idx", this::class)))
        `when`(client.getRawResponse("Archives/edgar/daily-index/2017/QTR1/crawler.20170314.idx"))
                .thenReturn(Mono.just(clientResponse))
    }

    @Test
    fun forceRetrieve() {
        prepareMockHttpHandle()

        val dest = File(createTempDir(), "Archives")
        dest.mkdirs()
        File(File(this.javaClass.getResource(".").toURI()), "Archives").copyRecursively(dest)
        log.debug("the output directory: ${dest.parent}")

        val indices: Indices =  indexRepository.retrieve(LocalDate.parse("2017-03-14"), dest, force = true).block()

        assertThat(indices.filedDate, `is`(LocalDate.parse("2017-03-14")))
        assertThat(indices.indices,
                `is`(listOf(Index("1ST SOURCE CORP","DEF 14A", 34782,LocalDate.parse("2017-03-14"),
                        "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"))))
        assertThat(File(dest, "Archives/edgar/daily-index/2017/QTR1/crawler.20170314.idx").exists(), `is`(true))
        verify(client, times(1)).getRawResponse(anyString())
    }
}
