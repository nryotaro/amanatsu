package org.nryotaro.edgar

import org.junit.Test
import org.nryotaro.edgar.client.EdgarClient
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.retriever.FilingDetailRetriever
import org.nryotaro.edgar.retriever.IndexRetriever
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.CountDownLatch

class Experiment: EdgarTest() {


    @Autowired
    lateinit var edgar: EdgarClient

    @Autowired
    lateinit var indexRetriever: IndexRetriever

    @Autowired
    lateinit var repository: FilingDetailRetriever

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun f() {

        val cli = WebClient.create("http://www.sec.gov")
        val da = LocalDate.parse("2017-03-14")
        val ff = File("/tmp/hoge")
        val i: Flux<Index> = indexRetriever.retrieve(da, ff, false).flatMapIterable { it.indices }
                //.filter{it.url != "http://www.sec.gov/Archives/edgar/data/740629/9999999997-17-002782-index.htm"}
        //f1(da, ff)

        //val e: Flux<Index> = Flux.just(*i.toIterable().toList().toTypedArray())

        //(val companyName: String, val formType: String, val cik: Int, val dateFiled: LocalDate, val url: String)
        val ii = Flux.just(Index("comp", "form", 1,LocalDate.now(), "http://www.sec.gov/Archives/edgar/data/1420522/9999999997-17-002867-index.htm"),
                Index("comp", "form", 1,LocalDate.now(), "http://www.sec.gov/Archives/edgar/data/740629/9999999997-17-002782-index.htm"))
        val c  =ii.delayElements(Duration.ofMillis(100000L)).
                flatMap{
                    //cli.get().uri(it.url).exchange().onErrorResume { Mono.empty() }
                    repository.retrieve(it, ff,false)
                }

        val latch = CountDownLatch(1)
        c.doOnNext { log.debug("next: $it") }.doOnComplete {
            latch.countDown()
        }.subscribe {  }
        latch.await()
    }

    fun f1(da: LocalDate, ff: File): Flux<Index> {
        return indexRetriever.retrieve(da,
                ff, false).flatMapIterable { it.indices }.filter{it.url != "http://www.sec.gov/Archives/edgar/data/740629/9999999997-17-002782-index.htm" && it.url != "http://www.sec.gov/Archives/edgar/data/1420522/9999999997-17-002867-index.htm"}
    }
    //

}
