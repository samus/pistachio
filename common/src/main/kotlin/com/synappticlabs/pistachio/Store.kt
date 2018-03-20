package com.synappticlabs.pistachio

interface Store {
    fun dispatch(cmd: Command<Any>)
}