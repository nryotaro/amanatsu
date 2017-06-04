package org.nryotaro.edgar.text

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.edgar.plain.filingdetail.Document
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.springframework.beans.factory.annotation.Autowired


class FilingDetailParserTest: EdgarTest() {

    @Autowired
    lateinit var parser: FilingDetailParser

    @Test
    fun parse() {
        val details: List<FilingDetail> = parser.parse(readTextFile("0001209191-17-028829-index.htm", FilingDetailParserTest::class))
        assertThat(details.size, `is`(7))
        assertThat(details[0].seq , `is`(1))
        assertThat(details[0].description, `is`("DEF 14A"))
        assertThat(details[0].document, `is`(Document(filename = "proxy2017.htm", url = "/Archives/edgar/data/34782/000003478217000039/proxy2017.htm")))
        assertThat(details[0].type, `is`("DEF 14A"))
        assertThat(details[0].size, `is`(929547))
    }

    @Test
    fun parseTwoTables() {

        val details: List<FilingDetail> = parser.parse(readTextFile("0001564590-17-004283-index.htm", FilingDetailParserTest::class))
        assertThat(details.size, `is`(20))

        assertThat(details[0].seq , `is`(1))
        assertThat(details[0].description, `is`("10-K"))
        assertThat(details[0].document, `is`(Document(filename = "akao-10k_20161231.htm", url = "/Archives/edgar/data/1301501/000156459017004283/akao-10k_20161231.htm")))
        assertThat(details[0].type, `is`("10-K"))
        assertThat(details[0].size, `is`(4132994))

        assertThat(details[14].seq , `is`(14))
        assertThat(details[14].description, `is`("XBRL INSTANCE DOCUMENT"))
        assertThat(details[14].document, `is`(Document(filename = "akao-20161231.xml", url = "/Archives/edgar/data/1301501/000156459017004283/akao-20161231.xml")))
        assertThat(details[14].type, `is`("EX-101.INS"))
        assertThat(details[14].size, `is`(3306989))
    }

}
