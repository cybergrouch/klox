package org.lange.interpreters.klox

interface LoggingService {
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun console(message: String)
}
