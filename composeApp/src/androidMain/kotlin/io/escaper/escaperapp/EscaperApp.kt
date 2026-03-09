package io.escaper.escaperapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EscaperApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EscaperApp)
            installCommonModules()
        }
    }
}