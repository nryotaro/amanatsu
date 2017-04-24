package org.nryotaro.edgar.text

import org.nryotaro.edgar.exception.EdgarException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class IndexParser {

    private val pattern = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    fun parse(index: String) :String {
       val lines =  index.split(Regex("\\r?\\n"))
       var date: LocalDate?

       lines.forEach date@ {
           println(it)
           if(it.startsWith("Last Data Received:")) {
               val date: String?= Regex("Last Data Received:\\s+(\\w.+)").find(it)?.groupValues?.get(1)

               EdgarException("")
               val c = if(date != null)  LocalDate.parse(date, pattern) else null
               println(date)
               //0LocalDate.parse(DateTime) DateTimeForm LocalDate
               return@date
           }
       }

       return TODO()
    }
}
