package org.nryotaro.edgar.text

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class IndexParser {
    fun parse(index: String) :String {
       val lines =  index.split(Regex("\\r?\\n"))
       var date: LocalDate?

       lines.forEach date@ {
           if(it.startsWith("Last Data Received:")) {

               return@date
           }
       }

       return TODO()
    }
}
