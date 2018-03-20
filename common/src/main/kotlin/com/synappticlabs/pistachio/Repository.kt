package com.synappticlabs.pistachio

interface Repository<T, ID> {
    fun put(obj: T): ID
    fun read(id: ID): T
    fun scan(): ArrayList<T>
    fun delete(id: ID)
}