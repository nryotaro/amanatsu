package org.nryotaro.edgar.repository

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.plain.index.Index
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class FilingDetailRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var filingDetailRepository: FilingDetailRepository

    @Test fun retrieve() {
        filingDetailRepository.retrieve(Index(
                companyName = "1ST SOURCE CORP",
                formType = "DEF 14A",
                cik = 34782,
                dateFiled = LocalDate.parse("2017-03-14"),
                url = "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"
        ))
    }
}
