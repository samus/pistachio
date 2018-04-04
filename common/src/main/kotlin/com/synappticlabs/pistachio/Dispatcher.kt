package com.synappticlabs.pistachio

interface Dispatch {
    fun dispatch(block: () -> Unit)
}

expect class Dispatcher(): Dispatch {
    override fun dispatch(block: () -> Unit)
}