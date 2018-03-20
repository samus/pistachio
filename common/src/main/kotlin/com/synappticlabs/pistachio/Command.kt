package com.synappticlabs.pistachio

interface Command<ID> {
    fun execute(): ArrayList<ID>
}