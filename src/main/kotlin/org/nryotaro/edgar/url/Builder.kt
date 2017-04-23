package org.nryotaro.edgar.url

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class Builder(@Value("\${url.dailyindex}") val dailyindex: String) {

    fun buildIndex(date: LocalDate): String {
       return  dailyindex + date.year.toString() + "/QTR" + calcQuarter(date) + "/crawler." +
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
