package org.nryotaro.edgar

import io.netty.channel.EventLoopGroup
import org.junit.Ignore
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
import reactor.core.publisher.FluxSink
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

    @Ignore
    @Test
    fun exec() {
    }
}
