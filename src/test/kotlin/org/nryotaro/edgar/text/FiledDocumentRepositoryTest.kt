package org.nryotaro.edgar.text

import org.junit.Test
import org.nryotaro.edgar.Edgar
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.repository.FiledDocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.nio.file.Paths

class FiledDocumentRepositoryTest: EdgarTest() {


    @Autowired
    lateinit var filedDocumentRepository: FiledDocumentRepository

    @Test
    fun retrieve(){

    }
}
