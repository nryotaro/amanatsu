package org.nryotaro.edgar.text

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.nryotaro.edgar.exception.EdgarException
import org.nryotaro.edgar.plain.filingdetail.Document
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class FilingDetailParser {

    val log: Logger = LoggerFactory.getLogger(FilingDetailParser::class.java)

    val digit = Regex("\\d+")

    fun parse(filingDetail: String): List<FilingDetail> {
        val tables: Elements = Jsoup.parse(filingDetail).select("div#formDiv table.tableFile")

        val parsed  = tables.map{parseTable(it)}
        val parsedNotNull = parsed.filterNotNull()

        if(parsed.size != parsedNotNull.size) {
            throw EdgarException("\"${filingDetail}\": unexpected format")
        }

        return parsedNotNull.flatMap { it }


        /*
        val trs = Jsoup.parse(filingDetail).select("div#formDiv table.tableFile tr")

        val headers = trs.first().select("th")
        if(!(headers[0].text() == "Seq" &&
                headers[1].text() == "Description" &&
                headers[2].text() == "Document" &&
                headers[3].text() == "Type" &&
                headers[4].text() == "Size" &&
                headers.size == 5)) {
            throw EdgarException("\"${filingDetail}\": unexpected format")
        }
        val i = {i: String -> if(i.matches(digit)) Integer.parseInt(i) else null}

        return (1..trs.size-1).map {
            val tds = trs[it].select("td")
            FilingDetail(
                    seq = i(tds[0].text()),
                    description = tds[1].text(),
                    document =  Document(url = tds[2].select("a").first().attr("href"), filename = tds[2].text()),
                    type = tds[3].text(),
                    size = i(tds[4].text()))
        }
        */
    }

    private fun parseTable(elem: Element): List<FilingDetail>? {

        val trs = elem.select("tr")

        val headers = trs.first().select("th")
        if(!(headers[0].text() == "Seq" &&
                headers[1].text() == "Description" &&
                headers[2].text() == "Document" &&
                headers[3].text() == "Type" &&
                headers[4].text() == "Size" &&
                headers.size == 5)) {
            return null
        }
        val i = {i: String -> if(i.matches(digit)) Integer.parseInt(i) else null}

        return (1..trs.size-1).map {
            val tds = trs[it].select("td")
            FilingDetail(
                    seq = i(tds[0].text()),
                    description = tds[1].text(),
                    document =  Document(url = tds[2].select("a").first().attr("href"), filename = tds[2].text()),
                    type = tds[3].text(),
                    size = i(tds[4].text()))
        }
    }

}
