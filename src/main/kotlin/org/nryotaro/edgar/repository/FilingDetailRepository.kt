package org.nryotaro.edgar.repository

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.url.Builder
import org.nryotaro.edgar.plain.index.Index
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class FilingDetailRepository(val client: EdgarClient) {

    fun retrieve(index: Index) = {
        TODO()
    }
}