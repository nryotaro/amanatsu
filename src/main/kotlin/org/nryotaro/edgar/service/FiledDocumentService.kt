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

    fun collect(details: Flux<FilingDetail>, destRoot: File, force: Boolean = false): Flux<Boolean> {
        return details.doOnNext { log.debug("""download: ${it.document.url}""") }.delayElements(Duration.ofMillis(100L), Schedulers.single())
                .map { Pair(it.document.url, File(destRoot, it.document.url)) }
                .filter { force || !it.second.exists() }.flatMap {
            retriever.retrieve(it.first, it.second)
        }
    }
}
