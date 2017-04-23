package org.nryotaro.edgar

import com.sun.xml.internal.fastinfoset.util.StringArray
import org.nryotaro.edgar.client.EdgarClientContext
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.cmdparser.CommandContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.Arrays.asList

fun main(args: Array<String>) {
    SpringApplication.run(Bootstrap::class.java, *args)
}

@SpringBootApplication
@Import(CommandContext::class, EdgarClientContext::class)
open class Bootstrap(val edgar: Edgar, val parser: CmdParser) : CommandLineRunner {

    override fun run(vararg args: String) {
        edgar.execute()
    }
}

interface Edgar {
   fun execute(vararg args: String)
}

@Service
@Profile("prod")
class EdgarService: Edgar {
    override fun execute(vararg args: String) {
        println("foo")
    }
}


