package com.synappticlabs.pistachio

import android.os.Handler
import android.os.Looper

actual class Dispatcher {
    val handler = Handler(android.os.Looper.getMainLooper())
    actual fun dispatch(block: () -> Unit) {
        handler.post(object: Runnable{
            override fun run() {
                block()
            }
        })
    }
}