package com.synappticlabs.pistachio

actual class Dispatcher: Dispatch {
    actual override fun dispatch(block: () -> Unit) {
        //For now just execute the block.  Work on main thread execution later.
        block()
    }
}