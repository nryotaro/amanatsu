package org.nryotaro.edgar.repository

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate


class IndexRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var indexRepository: IndexRepository
    @Test
    fun retrieve() {
        println("---adfasdfa---")
        indexRepository.retrieve(LocalDate.parse("2017-03-14"))
    }
}
