package com.synappticlabs.pistachio

expect class Dispatcher() {
    fun dispatch(block: () -> Unit)
}