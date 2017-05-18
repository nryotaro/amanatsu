package org.nryotaro.edgar.client

import org.junit.Ignore
import org.junit.Test
import org.nryotaro.edgar.EdgarTest
import org.nryotaro.httpcli.HttpCli


class AsyncEdgarClientTest: EdgarTest() {


    @Ignore
    @Test
    fun getTest() {
        AsyncEdgarClient().get("https://discuss.kotlinlang.org/t/sealed-classes-design/82").doOnNext {
            println(String(it.content))
        }.subscribe {  }

        Thread.sleep(30000)
    }


}
