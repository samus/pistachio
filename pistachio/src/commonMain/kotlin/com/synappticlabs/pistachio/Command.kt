package com.synappticlabs.pistachio

interface Command {
    fun apply(repositories: Map<String,Repository<*>>): ChangeList
}