package com.synappticlabs.pistachio.example

import android.app.Application
import com.synappticlabs.pistachio.Dispatcher
import com.synappticlabs.pistachio.Store

class PistachioExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        store = Store(repositories = hashMapOf(accountRepoName to AccountRepository(this)),
                        middleware = listOf(LoggingMiddleware()),
                        dispatcher = Dispatcher())
    }
}

lateinit var store: Store
