package org.nryotaro.edgar.text

import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired


class FilingDetailParserTest: EdgarTest() {

    @Autowired
    lateinit var parser: FilingDetailParser

    @Test
    fun parse() {
        parser.parse(readTextFile("0001209191-17-028829-index.htm", FilingDetailParserTest::class))
    }

}
