package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.text.FilingDetailParser
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.nio.ByteBuffer

@Repository
class FilingDetailRetriever(
        private val client: EdgarClient,
        private val filingDetailParser: FilingDetailParser,
        @Value("\${url.root}") private val edgarRootUrl: String) {

    fun retrieve(index: Index): Flux<FilingDetail> {
        return client.get(index.url).flatMapIterable{filingDetailParser.parse(it)}
    }

    private fun retrieve(index: Index, destRoot: File, force: Boolean = false) {
        val subDest = index.url.substringAfter(edgarRootUrl)
        val dest = File(destRoot, subDest)

        if(!force && dest.exists() && dest.isFile)
            retrieve(Mono.just(readFromLocal(dest)), {})
        else
            retrieve(readFromRemote(subDest), {dest.parentFile.mkdirs(); dest.createNewFile(); dest.writeText(it)})
    }

    private fun retrieve(text: Mono<String>, writer: (String) -> Unit): Flux<FilingDetail> {
        return text.doOnNext(writer).flatMapIterable { filingDetailParser.parse(it)}
    }

    private fun readFromLocal(file: File): String {
        return file.readText()
    }
    private fun readFromRemote(url: String): Mono<String> {
        return client.get(url)
    }
}