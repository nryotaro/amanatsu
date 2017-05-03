package org.nryotaro.edgar

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import org.nryotaro.edgar.client.EdgarClientContext
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.cmdparser.CommandContext
import org.nryotaro.edgar.plain.cmd.Arguments
import org.nryotaro.edgar.plain.filingdetail.FilingDetail
import org.nryotaro.edgar.plain.index.Index
import org.nryotaro.edgar.plain.index.Indices
import org.nryotaro.edgar.retriever.FiledDocumentRepository
import org.nryotaro.edgar.retriever.FilingDetailRepository
import org.nryotaro.edgar.retriever.IndexRetriever
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
        private val filingDetailRepository: FilingDetailRepository,
        private val filedDocumentRepository: FiledDocumentRepository): Edgar {
    override fun execute(vararg args: String) {
        val arguments: Arguments = try {cmdParser.parse(*args)} catch(e: ParseException) {
            System.err.println("${e.message + System.lineSeparator()}")
            printHelp()
            null
        } ?: return
       
        val c: Mono<Indices> = indexRepository.retrieve(arguments.date)
        val d: Flux<Index> = c.flatMapIterable { it.indices }
        val e: Flux<FilingDetail> = d.flatMap { filingDetailRepository.retrieve(it)}
        e.subscribe({
            println("fin")
        })
    }

    private fun printHelp() {
        val n = System.lineSeparator()
        HelpFormatter().printHelp(appName, "Download documents filed with Edgar$n$n",
                cmdParser.options,
                "${n}Please report issues at https://github.com/nryotaro/edgar-crawler",
                true)
    }
}


