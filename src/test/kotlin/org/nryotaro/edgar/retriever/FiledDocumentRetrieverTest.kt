package org.nryotaro.edgar.retriever

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.springframework.beans.factory.annotation.Autowired

class FiledDocumentRetrieverTest : EdgarTest() {


    @Autowired
    lateinit var filedDocumentRepository: FiledDocumentRetriever

    @Test
    fun retrieve(){

    }
}
