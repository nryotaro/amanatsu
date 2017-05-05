package org.nryotaro.edgar

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import org.nryotaro.edgar.client.EdgarClientContext
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.cmdparser.CommandContext
import org.nryotaro.edgar.plain.cmd.Arguments
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.retriever.FiledDocumentRetriever
import org.nryotaro.edgar.retriever.FilingDetailRetriever
import org.nryotaro.edgar.retriever.IndexRetriever
import org.nryotaro.edgar.service.FiledDocumentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

fun main(args: Array<String>) {
    SpringApplication.run(Bootstrap::class.java, *args)
}

@SpringBootApplication
@Import(CommandContext::class, EdgarClientContext::class)
open class Bootstrap(val edgar: Edgar, val parser: CmdParser) : CommandLineRunner {

    override fun run(vararg args: String) {
        edgar.execute(*args)
    }

}

interface Edgar {
   fun execute(vararg args: String)
}


@Service
@Profile("prod")
class EdgarImpl(
        @Value("\${spring.application.name}") val appName: String,
        private val cmdParser: CmdParser,
        private val indexRepository: IndexRetriever,
        private val filingDetailRepository: FilingDetailRetriever,
        private val filedDocumentService: FiledDocumentService): Edgar {

    val log  = LoggerFactory.getLogger(this::class.java)

    override fun execute(vararg args: String) {
        val arguments: Arguments = try {cmdParser.parse(*args)} catch(e: ParseException) {
            System.err.println("${e.message + System.lineSeparator()}")
            printHelp()
            null
        } ?: return

        val indices: Flux<Index> = indexRepository.retrieve(arguments.date,
                arguments.destination, arguments.overwrite).flatMapIterable { it.indices }
        val filingDetails: Flux<FilingDetail>
                = indices
                /*
                .filter{
            if(
            it.url != "http://www.sec.gov/Archives/edgar/data/740629/9999999997-17-002782-index.htm"
                    && it.url != "http://www.sec.gov/Archives/edgar/data/1420522/9999999997-17-002867-index.htm") {
                true
            } else {
                false
            }
        }
        */
                .flatMap { filingDetailRepository.retrieve(it, arguments.destination, arguments.overwrite)}

        println("hoge")
        filingDetails.blockLast()
        println("bar")
        //filedDocumentService.blockCollect(filingDetails, arguments.destination, arguments.overwrite)
    }

    private fun printHelp() {
        val n = System.lineSeparator()
        HelpFormatter().printHelp(appName, "Download documents filed with Edgar$n$n",
                cmdParser.options,
                "${n}Please report issues at https://github.com/nryotaro/edgar-crawler",
                true)
    }
}


