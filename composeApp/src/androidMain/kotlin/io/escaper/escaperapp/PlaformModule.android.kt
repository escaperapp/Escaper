package io.escaper.escaperapp

import io.escaper.escaperapp.core.ZapretProxy
import io.escaper.escaperapp.data.AndroidConnectionStatusRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getPlatformModule(): Module? {
    return module {
        single {
            AndroidConnectionStatusRepository()
        }
        single {
            ZapretProxy()
        }
    }
}