package org.nryotaro.edgar.text

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.nryotaro.edgar.exception.EdgarException
import org.springframework.stereotype.Service


@Service
class FilingDetailParser {

    fun parse(filingDetail: String) {
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

        for(i in 1..trs.size-1) {
            val tds = trs[i].select("td")
             tds[0]
        }
        TODO()
    }

}
