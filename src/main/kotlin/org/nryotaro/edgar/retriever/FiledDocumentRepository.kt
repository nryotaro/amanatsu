package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

@Repository
class FiledDocumentRepository(private val client: EdgarClient) {

    fun retrieve(documentUrl: String, outputRoot: File, force: Boolean = false) {

        val dest = File(outputRoot, documentUrl)
        if(force || !dest.exists()) {
            client.download(documentUrl, Paths.get(dest.toURI()))
        }
    }
}
