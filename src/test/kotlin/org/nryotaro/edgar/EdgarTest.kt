package org.nryotaro.edgar

import org.junit.runner.RunWith
import org.nryotaro.edgar.repository.IndexRepositoryTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.reflect.KClass

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("ut")
abstract class EdgarTest {
    protected  fun readTextFile(file: String, klass: KClass<out Any>): String {
        return klass.java.getResourceAsStream(file)
                .bufferedReader().use { it.readText() }
    }
}

@Service
@Profile("ut")
class EdgarMock: Edgar {
    override fun execute(vararg args: String) {
    }
}
