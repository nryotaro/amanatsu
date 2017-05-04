package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths

@Repository
class FiledDocumentRetriever(private val client: EdgarClient) {

    fun retrieve(documentUrl: String, dest: File): FileChannel {
        return client.download(documentUrl, Paths.get(dest.toURI()))
    }
}
