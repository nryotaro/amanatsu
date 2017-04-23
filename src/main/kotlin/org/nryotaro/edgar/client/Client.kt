package org.nryotaro.edgar.client

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono


class Client {

    fun temp() {
       val client =  WebClient.create("https://www.sec.gov")
        val c= client.get().uri("Archives/edgar/daily-index/2017/QTR1/crawler.20170331.idx").exchange()
    }
}