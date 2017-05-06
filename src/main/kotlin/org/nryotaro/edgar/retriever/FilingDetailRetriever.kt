package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.Document
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.text.FilingDetailParser
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer

@Repository
class FilingDetailRetriever(
        private val client: EdgarClient,
        private val filingDetailParser: FilingDetailParser) {

    val log: Logger = LoggerFactory.getLogger(FilingDetailParser::class.java)
    
    fun retrieve(index: Index, destRoot: File, force: Boolean = false): Flux<FilingDetail> {
        val path =  URL(index.url).path.substringAfter("/")
        val dest = File(destRoot, path)

        return if(!force && dest.exists() && dest.isFile)
            retrieve(Mono.just(readFromLocal(dest)), {})
        else
            retrieve(readFromRemote(path), {
                dest.parentFile.mkdirs();
                dest.createNewFile();
                dest.writeText(it)})
    }

    private fun retrieve(text: Mono<String>, writer: (String) -> Unit): Flux<FilingDetail> {
        return text.doOnNext(writer).flatMapIterable { filingDetailParser.parse(it)}.onErrorResume {
            Mono.empty()
        }
    }

    private fun readFromLocal(file: File): String {
        return file.readText()
    }
    private fun readFromRemote(url: String): Mono<String> {
        return client.get(url)
    }
}