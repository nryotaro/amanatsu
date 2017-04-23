package org.nryotaro.edgar.url

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.nryotaro.edgar.Bootstrap
import org.nryotaro.edgar.cmdparser.CommandContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest
//@ExtendWith(SpringExtension::class)
@Import(CommandContext::class)
class BuilderTest {

     @Autowired
    lateinit var builder: Builder

    @Test
    fun buildIndexUrl() {
        assertThat(builder.buildIndex(LocalDate.parse("2016-12-02")),
                `is`("https://www.sec.gov/Archives/edgar/daily-index/2016/QTR4/crawler.20161202.idx"))
    }
}




