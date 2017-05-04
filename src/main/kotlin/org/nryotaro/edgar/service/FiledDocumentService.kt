package org.nryotaro.edgar.service

import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.net.URL
import java.nio.channels.FileChannel

@Service
class FiledDocumentService(private val retriever: FiledDocumentRetriever) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    private fun collect(details: Flux<FilingDetail>, destRoot: File, force: Boolean = false): Flux<Triple<String, File,FileChannel>> {
        return details.map{Pair(it.document.url, File(destRoot, it.document.url))}
                .filter { force || !it.second.exists() }.map {
            Triple(it.first, it.second, retriever.retrieve(it.first, it.second))
        }
    }
    
    fun blockCollect(details: Flux<FilingDetail>, destRoot: File, force: Boolean = false) {
        f(collect(details, destRoot, force).toIterable().toList())
    }

    private tailrec fun f(chans: List<Triple<String, File,FileChannel>>): List<Triple<String, File, FileChannel>> {
        if(chans.isEmpty())
            return listOf<Triple<String, File,FileChannel>>()
        return f(chans.filter {
            if(!it.third.isOpen && it.second.exists()) {
                log.debug(""""${it.first}" was successfully downloaded""")
                false
            } else true
        })
    }
}
