package org.nryotaro.edgar.text

import org.nryotaro.edgar.exception.EdgarException
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class IndexParser {

    private val pattern = DateTimeFormatter.ofPattern("MMM dd, uuuu", Locale.US)

    fun parse(index: String) :String {
        val lines =  index.split(Regex("\\r?\\n"))
        val date = extractDate(lines)
        val  indices = extractIndexLine(lines)

       return TODO()
    }

    private fun extractDate(lines: List<String>): LocalDate {
        val dateLine: String = lines.first { it.startsWith("Last Data Received:") }
        val dateStr: String? = Regex("Last Data Received:\\s+(\\w.+)").find(dateLine)?.groupValues?.get(1)
        return if(dateStr != null) LocalDate.parse(dateStr, pattern) else throw EdgarException("failed to find date in \"$dateLine\"")
    }

    private fun extractIndexLine(lines: List<String>): List<String> {
        var start: Int = -1
        var found: Boolean = false
        for(line in lines) {
            start++
            if(line.startsWith("-----")) {
                found = true
                break
            }
        }
        return if(found) lines.subList(start+1, lines.size)  else throw EdgarException("----")
    }
}
