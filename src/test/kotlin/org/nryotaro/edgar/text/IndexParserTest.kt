package org.nryotaro.edgar.text

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired
import java.nio.file.Files


class BuilderTest : EdgarTest() {


   @Autowired
   lateinit var parser: IndexParser

   @Test fun parseIndex()  {
       val text =  this.javaClass.getResourceAsStream("crawler.20170314.idx").bufferedReader().use { it.readText() }
       parser.parse(text)
   }
}
