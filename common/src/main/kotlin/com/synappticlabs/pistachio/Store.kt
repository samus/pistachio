package com.synappticlabs.pistachio

class Store (private val repositories: ArrayList<Repository<Any>>){
    fun dispatch(cmd: Command) {
        cmd.execute(repositories)
    }
}