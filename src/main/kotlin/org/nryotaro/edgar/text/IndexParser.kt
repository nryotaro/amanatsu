package org.nryotaro.edgar.text

import org.nryotaro.edgar.exception.EdgarException
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class IndexParser {

    private val pattern = DateTimeFormatter.ofPattern("MMM dd, uuuu", Locale.US)

    fun parse(index: String) :String {
       val lines =  index.split(Regex("\\r?\\n"))
       var date: LocalDate?

       lines.forEach(fun(line: String) {

       })

       lines.forEach {
           println(it)
           if(it.startsWith("Last Data Received:")) {
               val dateStr: String?= Regex("Last Data Received:\\s+(\\w.+)").find(it)?.groupValues?.get(1)
               date = if(dateStr != null) LocalDate.parse(dateStr, pattern) else null
               return@forEach
           }
           if(it.startsWith("-----------")) {
           }
       }

       return TODO()
    }
}
