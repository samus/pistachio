package com.synappticlabs.pistachio

interface Repository<T> {
    val name: String
    fun put(obj: T): UUID
    fun read(id: UUID): T
    fun scan(): ArrayList<T>
    fun delete(id: UUID)
}