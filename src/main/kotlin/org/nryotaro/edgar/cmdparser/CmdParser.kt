package org.nryotaro.edgar.cmdparser

import org.apache.commons.cli.*
import org.nryotaro.edgar.plain.cmd.Arguments
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDate


@Configurable
class CommandContext {

    private fun buildOption(opt: String, longOpt: String, hasArg: Boolean, description: String): Option {
        val o = Option(opt, longOpt, hasArg, description)
        o.isRequired = true
        return o
    }

    @Bean fun options(): Options {
        val options: Options = Options()

        options.addOption(Option.builder("f").optionalArg(true).longOpt("force").desc("Download indices and filed documents even " +
                "if they have already existed in the specified destination").build())
        options.addOption(
                buildOption("d", "date", true, "The crawler retrieves the documents submitted on the specified date"))
        options.addOption(
                buildOption("o", "output-directory", true, "Retrieved documents will be stored in the specified directory"))

        return options
    }
}

@Service
class CmdParser(val options: Options) {

    fun parse(vararg args: String): Arguments {

        val commandLineParser: CommandLineParser = DefaultParser()

        val parsed: CommandLine = commandLineParser.parse(options, args)

        return Arguments(LocalDate.parse(parsed.getOptionValue("d")), File(parsed.getOptionValue("o")))

    }
}
