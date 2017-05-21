package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.index.Indices
import org.nryotaro.edgar.text.IndexParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
@Component
class IndexRetriever(
        private val client: EdgarClient,
        @Value("\${url.dailyindex}") private val dailyIndex: String,
        @Value("\${edgar.traffic.limit}") private val trafficLimit: Long,
        private val parser: IndexParser) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun retrieve(date: LocalDate, destRoot: File, force: Boolean = false): Mono<Indices> {
        val dest = File(destRoot, buildIndex(date))
        return if(!force && dest.exists() && dest.isFile)  {
            retrieve(dest, {})
        } else {
            retrieve(date, {dest.parentFile.mkdirs(); dest.createNewFile(); dest.writeText(it)})
        }
    }

    private fun retrieve(date: LocalDate, writer: (String) -> Unit): Mono<Indices> {
        return Mono.just(buildIndex(date)).delayElement(Duration.ofMillis(trafficLimit))
                .flatMap{readFromRemote(it)}.doOnNext(writer).flatMap { Mono.just(parser.parse(it))}
    }

    private fun retrieve(localDest: File, writer: (String) -> Unit): Mono<Indices> {
        return Mono.just(readFromLocal(localDest)).doOnNext(writer).flatMap{ Mono.just(parser.parse(it)) }
    }

    private fun readFromRemote(path: String): Mono<String> {
        return client.get(path).delayElement(Duration.ofMillis(trafficLimit)).flatMap {
            when(it.status) {
                200 -> Mono.just(String(it.content))
                429 -> {log.error("Exceeded the SEC’s Traffic Limit: ${String(it.content)}"); Mono.empty()}
                else -> {log.error("Unexpected response: ${String(it.content)}"); Mono.empty()}
            }
        }
    }
    private fun readFromLocal(file: File): String {
        return file.readText()
    }

    private fun buildIndex(date: LocalDate): String {
        return  dailyIndex + date.year.toString() + "/QTR" + calcQuarter(date) + "/crawler." +
                date.format(DateTimeFormatter.BASIC_ISO_DATE) + ".idx"
    }

    private fun calcQuarter(date: LocalDate): Int {
        return when (date.monthValue) {
            1, 2, 3 -> 1
            4, 5, 6 -> 2
            7, 8, 9 -> 3
            10, 11, 12 -> 4
            else -> throw UnsupportedOperationException("the month of $date could be null")
        }
    }
}
