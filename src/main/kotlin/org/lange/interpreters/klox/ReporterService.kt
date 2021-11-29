package org.lange.interpreters.klox

interface ReporterService {
    fun error(line: Int, message: String) {
        report(
            line = line,
            message = message
        )
    }

    fun report(line: Int, where: String? = null, message: String)
}