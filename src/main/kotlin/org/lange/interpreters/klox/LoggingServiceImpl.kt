package org.lange.interpreters.klox

import mu.KotlinLogging

class LoggingServiceImpl : LoggingService {

    private val logger = KotlinLogging.logger {}

    override fun info(message: String) {
        logger.info { message }
    }

    override fun warn(message: String) {
        logger.warn { message }
    }

    override fun error(message: String) {
        logger.error { message }
    }

    override fun console(message: String) {
        with(message) {
            println(this)
            logger.info { this }
        }
    }
}
