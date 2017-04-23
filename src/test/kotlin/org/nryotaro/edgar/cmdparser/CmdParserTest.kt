package org.nryotaro.edgar.cmdparser

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.nryotaro.edgar.Bootstrap
import org.nryotaro.edgar.plainvannila.Arguments
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest
//@ExtendWith(SpringExtension::class)
@Import(Bootstrap::class)
class CmdParserTest {

    @Autowired
    lateinit var parser: CmdParser

    @Test
    fun parseArguments() {
        val args =  parser.parse("-d", "2017-04-12", "-o", "/data")
        assertThat(args,`is`(Arguments(LocalDate.parse("2017-04-12"), File("/data"))) )
    }

}
