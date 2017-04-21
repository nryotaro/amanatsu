package org.nryotaro.edgar

import com.sun.xml.internal.fastinfoset.util.StringArray
import org.nryotaro.edgar.cmdparser.CmdParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Service
import java.util.Arrays.asList

@Service
class Sv(@Value("\${foo.bar}") val c: Int) {

}

@SpringBootApplication
open class Foo (val sv: Sv) : CommandLineRunner {

    override fun run(vararg args: String) {
        println(sv.c)

        CmdParser.parse(*args)
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Foo::class.java, *args)
}
