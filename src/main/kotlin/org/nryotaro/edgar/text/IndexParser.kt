package org.nryotaro.edgar.text

import org.nryotaro.edgar.exception.EdgarException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class IndexParser {

    private val pattern = DateTimeFormatter.ofPattern("MMM dd, uuuu")

    fun parse(index: String) :String {
       val lines =  index.split(Regex("\\r?\\n"))
       var date: LocalDate?

       lines.forEach {
           println(it)
           if(it.startsWith("Last Data Received:")) {
               val dateStr: String?= Regex("Last Data Received:\\s+(\\w.+)").find(it)?.groupValues?.get(1)
               println(dateStr)

               date = if(dateStr != null) LocalDate.parse(dateStr, pattern) else null
               return@forEach
           }
       }

       return TODO()
    }
}
