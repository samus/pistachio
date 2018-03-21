package com.synappticlabs.pistachio

interface Repository<T> {
    val name: String
    fun put(obj: T): UUID
    fun update(obj: T, uuid: UUID)
    fun read(id: UUID): T?
    fun scan(filter: (T) -> Boolean): List<T>
    fun delete(id: UUID)
}