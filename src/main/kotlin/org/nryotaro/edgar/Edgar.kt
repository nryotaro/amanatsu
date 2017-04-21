package org.nryotaro.edgar

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Service

@Service
class Sv(@Value("\${foo.bar}") val c: Int) {

}

@SpringBootApplication
open class Foo (val sv: Sv) : CommandLineRunner {

    override fun run(vararg p0: String?) {
        println(sv.c)
        println("foo")
        //throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Foo::class.java, *args)
}
