package org.nryotaro.edgar.cmdparser

import org.apache.commons.cli.Options
//import kotlin.test.assertEquals
//import kotlin.test.todo

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.nryotaro.edgar.Foo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
//@ExtendWith(SpringExtension::class)
@Import(Foo::class)
class CmdParserTest {

    @Autowired
    lateinit var  options: Options

    @Test
    fun contextLoads() {

        println("foobar")
    }

}
