package org.nryotaro.edgar.repository

import org.nryotaro.edgar.client.EdgarClient
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.BodyExtractors
import java.io.File
import javax.validation.ReportAsSingleViolation

@Repository
class FiledDocumentRepository(private val client: EdgarClient) {

    fun retrieve(outputRoot: File, documentUrl: String) {
        TODO()
    }
}
