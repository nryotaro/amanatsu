package org.nryotaro.edgar.repository

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Mono
import java.time.LocalDate

class FilingDetailRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var filingDetailRepository: FilingDetailRepository

    @MockBean
    lateinit var client: EdgarClient

    @Test fun retrieve() {
        val url = "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"
        `when`(client.get(url)).thenReturn(
                Mono.just(readTextFile("0001209191-17-028829-index.htm", FilingDetailRepositoryTest::class)))

        val details = filingDetailRepository.retrieve(Index(
                companyName = "1ST SOURCE CORP",
                formType = "DEF 14A",
                cik = 34782,
                dateFiled = LocalDate.parse("2017-03-14"),
                url = url
        )).reduce(listOf<FilingDetail>(), {acc, filingDetail -> acc.plus(filingDetail) }).block()

        assertThat(details.size, `is`(7))
    }


}
