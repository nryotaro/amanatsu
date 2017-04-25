package org.nryotaro.edgar.repository

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired


class IndexRepositoryTest : EdgarTest() {

    @Autowired
    lateinit var indexRepository: IndexRepository
    @Test
    fun retrieve() {

    }
}
