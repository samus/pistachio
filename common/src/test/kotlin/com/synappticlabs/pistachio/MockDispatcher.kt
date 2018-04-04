package com.synappticlabs.pistachio

class MockDispatcher : Dispatch {
    override fun dispatch(block: () -> Unit) {
        block()
    }
}