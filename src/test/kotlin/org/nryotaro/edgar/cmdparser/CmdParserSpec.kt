package org.nryotaro.edgar.cmdparser

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.*
//import kotlin.test.assertEquals
//import kotlin.test.todo

import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CmdParserSpec: Spek({
    describe("a calculator") {

        on("addition") {
            val sum = 2+4

            it("should return the result of adding the first number to the second number") {
                assertEquals(6, sum)
            }
        }

        on("subtraction") {
            val subtract =  4-2

            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, subtract)
            }
        }
    }
})
