package org.lange.interpreters.klox

class SourceProcessorServiceImpl : SourceProcessorService {
    override fun process(source: String) {
        println("Run: $source")
    }
}
