package com.synappticlabs.pistachio

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.darwin.*

interface DispatchQueue {
    companion object {
        val main: DispatchQueue = object : DispatchQueue {
            override fun async(block: () -> Unit) {
                dispatch_async(dispatch_get_main_queue()) { block() }
            }

            override fun asyncAfter(ms: Long, block: () -> Unit) {
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, ms * NSEC_PER_MSEC), dispatch_get_main_queue()) {
                    block()
                }
            }
        }
    }

    fun async(block: () -> Unit)
    fun asyncAfter(ms: Long, block: () -> Unit)
}

actual class Dispatcher: Dispatch {
    actual override fun dispatch(block: () -> Unit) {
        DispatchQueue.main.async(block)
    }
}