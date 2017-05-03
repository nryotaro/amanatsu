package org.nryotaro.edgar.plain.cmd

import java.io.File
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime

data class Arguments(val date: LocalDate, val destination: File, val overwrite: Boolean = false)
