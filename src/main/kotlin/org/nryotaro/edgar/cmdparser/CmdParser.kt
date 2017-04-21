package org.nryotaro.edgar.cmdparser

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options


object CmdParser {

    fun parse(vararg args: String) {

        val commandLineParser: CommandLineParser = DefaultParser()


        val  option: Options = Options()
        option.addOption("d", "date", true, "foobar")

        //val line: CommandLine = commandLineParser.parse(option, args)
        val line: CommandLine = commandLineParser.parse(option, args)

        println(line)


    }
}
