package org.nryotaro.edgar

import org.junit.Test
import org.junit.runner.RunWith
import org.nryotaro.edgar.annotation.qualifier.MainRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.*
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.reflect.KClass

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("ut")
@Import(EdgarRunnerConfiguration::class)
abstract class EdgarTest {
    protected  fun readTextFile(file: String, klass: KClass<out Any>): String {
        return klass.java.getResourceAsStream(file)
                .bufferedReader().use { it.readText() }
    }
}

@Service
@Profile("ut")
@Primary
class EdgarMock: Edgar {
    override fun execute(vararg args: String) {
    }
}

@Configuration
open class EdgarRunnerConfiguration {
   @Bean
   @MainRunner
   open fun edgar(): Edgar {
       return EdgarService()
   }
}

class ATest: EdgarTest() {

    @Autowired
    @MainRunner
    lateinit var edgar: Edgar

    @Test fun t() {
        println("foo")
    }
}
