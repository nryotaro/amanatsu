package org.nryotaro.edgar.text

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.retriever.FiledDocumentRepository
import org.springframework.beans.factory.annotation.Autowired

class FiledDocumentRepositoryTest: EdgarTest() {


    @Autowired
    lateinit var filedDocumentRepository: FiledDocumentRepository

    @Test
    fun retrieve(){

    }
}
