package com.synappticlabs.pistachio

import android.os.Handler
import android.os.Looper

actual class Dispatcher: Dispatch {
    val handler = Handler(android.os.Looper.getMainLooper())
    actual override fun dispatch(block: () -> Unit) {
        handler.post { block() }
    }
}