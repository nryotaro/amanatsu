package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class FiledDocumentRepository(private val client: EdgarClient) {

    fun retrieve(outputRoot: File, documentUrl: String) {
        TODO()
    }
}
