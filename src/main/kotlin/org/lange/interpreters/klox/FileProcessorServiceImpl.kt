package org.lange.interpreters.klox

class FileProcessorServiceImpl(
    private val loggingService: LoggingService
) : FileProcessorService {
    override fun processFile(path: String) {
        loggingService.console("Running Source Path: $path")
    }
}
