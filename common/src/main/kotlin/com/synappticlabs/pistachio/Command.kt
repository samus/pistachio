package com.synappticlabs.pistachio

interface Command {
    fun execute(repositories: ArrayList<Repository<Any>>): ArrayList<UUID>
}