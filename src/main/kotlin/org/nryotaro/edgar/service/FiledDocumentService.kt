package org.nryotaro.edgar.service

import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.File
import java.net.URL
import java.nio.channels.FileChannel
import java.time.Duration

@Service
class FiledDocumentService(private val retriever: FiledDocumentRetriever) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun collect(details: Flux<FilingDetail>, destRoot: File, force: Boolean = false): Flux<FileChannel> {
        //f(collect(details.delayElements(Duration.ofMillis(100L)), destRoot, force).toIterable().toList())
        return details.doOnNext { log.debug("""download: ${it.document.url}""") }.delayElements(Duration.ofMillis(100L), Schedulers.single())
                .map { Pair(it.document.url, File(destRoot, it.document.url)) }
                .filter { force || !it.second.exists() }.flatMap {
            retriever.retrieve(it.first, it.second)
        }
    }

    /*
    private fun collect(details: Flux<FilingDetail>, destRoot: File, force: Boolean = false): Flux<FileChannel> {
        return details.map{Pair(it.document.url, File(destRoot, it.document.url))}
                .filter { force || !it.second.exists() }.map { retriever.retrieve(it.first, it.second)
        }
    }
    */

    /*
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
    */
}
