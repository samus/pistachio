package com.synappticlabs.pistachio

class Store (private val repositories: ArrayList<Repository<Any>>){
    val dispatcher = Dispatcher()

    fun dispatch(cmd: Command) {
        dispatcher.dispatch {
            cmd.execute(repositories)
        }
    }
}