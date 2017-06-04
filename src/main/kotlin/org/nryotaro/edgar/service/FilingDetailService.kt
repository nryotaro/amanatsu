package org.nryotaro.edgar.service

import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.retriever.FilingDetailRetriever
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File


@Service
class FilingDetailService(private val retriever: FilingDetailRetriever) {

    fun retrieve(indices: Flux<Index>, dest: File, overwrite : Boolean): Flux<FilingDetail> {
        return indices.flatMap {retriever.retrieve(it, dest, overwrite)}
    }

}

