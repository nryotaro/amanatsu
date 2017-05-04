package org.nryotaro.edgar.retriever

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.index.Indices
import org.nryotaro.edgar.text.IndexParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.Series.INFORMATIONAL
import org.springframework.http.HttpStatus.Series.SUCCESSFUL
import org.springframework.http.HttpStatus.Series.REDIRECTION
import org.springframework.http.HttpStatus.Series.SERVER_ERROR
import org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import reactor.core.publisher.Mono
import java.io.File

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
@Component
class IndexRetriever(private val client: EdgarClient, @Value("\${url.dailyindex}") private val dailyIndex: String,
                     private val parser: IndexParser) {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun retrieve(date: LocalDate, destRoot: File, force: Boolean = false): Mono<Indices> {
        val dest = File(destRoot, buildIndex(date))
        val writer: (String) -> Unit = {
            dest.parentFile.mkdirs()
            dest.createNewFile()
            dest.writeText(it)
        }
        return if(!force && dest.exists() && dest.isFile) retrieve(dest, {}) else retrieve(date, writer)
    }

    private fun retrieve(date: LocalDate, writer: (String) -> Unit): Mono<Indices> {
        return readFromRemote(buildIndex(date)).doOnNext(writer).flatMap { Mono.just(parser.parse(it))}
    }

    private fun retrieve(localDest: File, writer: (String) -> Unit): Mono<Indices> {
        return Mono.just(readFromLocal(localDest)).doOnNext(writer).flatMap{ Mono.just(parser.parse(it)) }
    }

    private fun readFromRemote(path: String): Mono<String> {
        return client.getRawResponse(path).flatMap {
              when(it.statusCode().series()) {
                 INFORMATIONAL -> TODO("information")
                 SUCCESSFUL -> it.bodyToMono(String::class.java)
                 REDIRECTION -> TODO("redirection")
                 CLIENT_ERROR -> {
                     log.info(""""$path" doesn't exist""")
                     Mono.empty()}
                 SERVER_ERROR -> TODO("server error")
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
