package org.nryotaro.edgar.text

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.plain.index.Index
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class IndexParserTest : EdgarTest() {

   @Autowired
   lateinit var parser: IndexParser

   @Test fun parseIndex()  {
       val text =  this.javaClass.getResourceAsStream("crawler.20170314.idx").bufferedReader().use { it.readText() }
       val indices = parser.parse(text)

       assertThat(indices.filedDate, `is`(LocalDate.parse("2017-03-14")))
       assertThat(indices.indices,
               `is`(listOf(Index("1ST SOURCE CORP","DEF 14A", 34782,LocalDate.parse("2017-03-14"),
              "http://www.sec.gov/Archives/edgar/data/34782/0000034782-17-000039-index.htm"))))
   }
}
