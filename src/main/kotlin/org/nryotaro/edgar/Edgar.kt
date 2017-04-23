package org.nryotaro.edgar

import com.sun.xml.internal.fastinfoset.util.StringArray
import org.nryotaro.edgar.cmdparser.CmdParser
import org.nryotaro.edgar.cmdparser.CommandContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Service
import java.util.Arrays.asList


@SpringBootApplication
@Import(CommandContext::class)
open class Bootstrap(val sv: Sv) : CommandLineRunner {

    override fun run(vararg args: String) {
        println(sv.c)
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Bootstrap::class.java, *args)
}
