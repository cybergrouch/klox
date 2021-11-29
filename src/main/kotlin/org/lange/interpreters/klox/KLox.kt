package org.lange.interpreters.klox

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

object KLox {

    fun main(args: Array<String>) {

        val kLoxModule = module {
            single<LoggingService> { LoggingServiceImpl() }
            single<FileProcessorService> { FileProcessorServiceImpl(loggingService = get()) }
            single<SourceProcessorService> { SourceProcessorServiceImpl(reporterService = get()) }
            single<ReporterService> { ReporterServiceImpl(loggingService = get()) }
            single<ReplProcessorService> { ReplProcessorServiceImpl(sourceProcessorService = get()) }
        }

        startKoin {
            modules(kLoxModule)
        }

        KLoxApplication().main(args = args)
    }
}
