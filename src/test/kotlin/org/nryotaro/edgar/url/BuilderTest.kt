package org.nryotaro.edgar.url

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate


class BuilderTest : EdgarTest() {

    @Autowired
    lateinit var builder: Builder

    @Test
    fun buildIndexUrl() {
        assertThat(builder.buildIndex(LocalDate.parse("2016-12-02")),
                `is`("Archives/edgar/daily-index/2016/QTR4/crawler.20161202.idx"))
    }
}



