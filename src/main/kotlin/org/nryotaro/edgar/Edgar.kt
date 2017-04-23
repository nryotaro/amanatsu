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
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.Arrays.asList


@SpringBootApplication
@Import(CommandContext::class)
open class Bootstrap(val edgar: Edgar, val parser: CmdParser) : CommandLineRunner {

    override fun run(vararg args: String) {

        edgar.execute()
    }


}
@Service
@Profile("ut")
class EdgarMock: Edgar {
    override fun execute() {
        println("bar")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Edgar {
   fun execute()
}

@Service
@Profile("prod")
class EdgarService: Edgar {
    override fun execute() {

        println("foo")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Bootstrap::class.java, *args)
}
