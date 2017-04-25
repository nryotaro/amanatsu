package org.nryotaro.edgar.text

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.datetime.DateFormatter
import java.nio.file.Files
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class IndexParserTest : EdgarTest() {


   @Autowired
   lateinit var parser: IndexParser

   @Test fun parseIndex()  {

       val text =  this.javaClass.getResourceAsStream("crawler.20170314.idx").bufferedReader().use { it.readText() }
       val parsed = parser.parse(text)

       println(parsed)
   }
}
