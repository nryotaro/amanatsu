package org.nryotaro.edgar.retriever

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Mono
import java.io.File
import java.net.URI
import java.time.LocalDate

class FilingDetailRetrieverTest : EdgarTest() {

    val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var filingDetailRetriever: FilingDetailRetriever

    @MockBean
    lateinit var client: EdgarClient

    @Test
    fun retrieve() {
        val path = "Archives/edgar/data/34782/0000034782-17-000039-index.htm"
        val url = "http://www.sec.gov/" + path
        `when`(client.get(path)).thenReturn(
                Mono.just(readTextFile("0001209191-17-028829-index.htm", FilingDetailRetrieverTest::class)))

        val destRoot = createTempDir()
        log.debug("$destRoot")

        val details = filingDetailRetriever.retrieve(Index(
                companyName = "1ST SOURCE CORP",
                formType = "DEF 14A",
                cik = 34782,
                dateFiled = LocalDate.parse("2017-03-14"),
                url = url
        ), destRoot).reduce(listOf<FilingDetail>(), {acc, filingDetail -> acc.plus(filingDetail) }).block()

        assertThat(details.size, `is`(7))
        assertThat(File(destRoot, path).exists(), `is`(true))
    }


}
