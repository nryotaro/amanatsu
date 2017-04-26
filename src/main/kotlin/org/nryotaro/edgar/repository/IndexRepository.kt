package org.nryotaro.edgar.repository

import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.http.RawHttpResponse
import org.nryotaro.edgar.plain.index.Indices
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import reactor.core.publisher.Mono

@Component
class IndexRepository(val client: EdgarClient, @Value("\${url.dailyindex}") private val dailyIndex: String) {

    fun  retrieve(date: LocalDate): Indices {
        val resp: Mono<RawHttpResponse> = client.get(buildIndex(date))

        println(resp.flatMap {
            it.body }.block())

        //return client.retrieveIndex(date)
        TODO()
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
