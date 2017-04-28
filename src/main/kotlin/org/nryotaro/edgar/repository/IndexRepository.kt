package org.nryotaro.edgar.repository

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.index.Indices
import org.nryotaro.edgar.text.IndexParser
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

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
@Component
class IndexRepository(val client: EdgarClient, @Value("\${url.dailyindex}") private val dailyIndex: String,
                      private val parser: IndexParser) {

    fun  retrieve(date: LocalDate): Mono<Indices> {
        println(buildIndex(date))
         return client.getRawResponse(buildIndex(date)).flatMap {
              when(it.statusCode().series()) {
                 INFORMATIONAL -> TODO("information")
                 SUCCESSFUL -> it.bodyToMono(String::class.java).flatMap{ Mono.just(parser.parse(it)) }
                 REDIRECTION -> TODO("redirection")
                 CLIENT_ERROR -> Mono.empty()
                 SERVER_ERROR -> TODO("server error")
             }
        }
    }

    fun buildIndex(date: LocalDate): String {
        return  dailyIndex + date.year.toString() + "/QTR" + calcQuarter(date) + "/crawler." +
                date.format(DateTimeFormatter.BASIC_ISO_DATE) + ".idx"
    }

    fun calcQuarter(date: LocalDate): Int {
        return when (date.monthValue) {
            1, 2, 3 -> 1
            4, 5, 6 -> 2
            7, 8, 9 -> 3
            10, 11, 12 -> 4
            else -> throw UnsupportedOperationException("the month of $date could be null")
        }
    }
}
