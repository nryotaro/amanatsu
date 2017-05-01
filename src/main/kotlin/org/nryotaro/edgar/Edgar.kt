package org.nryotaro.edgar

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import org.nryotaro.edgar.annotation.qualifier.MainRunner
import org.nryotaro.edgar.client.EdgarClientContext
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.cmdparser.CommandContext
import org.nryotaro.edgar.plain.cmd.Arguments
import org.nryotaro.edgar.repository.FiledDocumentRepository
import org.nryotaro.edgar.repository.FilingDetailRepository
import org.nryotaro.edgar.repository.IndexRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

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
        private val cmdParser: CmdParser,
        private val indexRepository: IndexRepository,
        private val filingDetailRepository: FilingDetailRepository,
        private val filedDocumentRepository: FiledDocumentRepository): Edgar {
    override fun execute(vararg args: String) {
        val args: Arguments? = try {cmdParser.parse(*args)} catch(e: ParseException) {
            val n = System.lineSeparator()
            System.err.println("${e.message + n}")
            HelpFormatter().printHelp("myapp", "Download documents filed in Edgar\n\n",
                    cmdParser.options,
                    "\nPlease report issues at https://github.com/nryotaro/edgar-crawler",
                    true)
            null
        }
    }
}


