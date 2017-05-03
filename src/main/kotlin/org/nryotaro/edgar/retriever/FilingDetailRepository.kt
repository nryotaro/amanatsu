package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.text.FilingDetailParser
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class FilingDetailRepository(private val client: EdgarClient, val filingDetailParser: FilingDetailParser) {

    fun retrieve(index: Index): Flux<FilingDetail> {
        return client.get(index.url).flatMapIterable{filingDetailParser.parse(it)}
    }
}