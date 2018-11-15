package com.synappticlabs.pistachio.repostories

import com.synappticlabs.pistachio.Repository
import com.synappticlabs.pistachio.UUID

abstract class InMemoryRepository<T>(override val name: String) : Repository<T> {
    private val repo = HashMap<UUID, T>()

    override fun put(obj: T): UUID {
        val uuid = UUID.create()
        repo[uuid] = obj
        return uuid
    }

    override fun update(obj: T, uuid: UUID) {
        repo[uuid] = obj
    }

    override fun read(id: UUID): T? {
        return repo[id]
    }

    override fun scan(filter: (T) -> Boolean): List<T> {
        return repo.entries.filter { entry ->
            return@filter filter(entry.value)
        }.map { it.value }
    }

    override fun delete(id: UUID) {
        repo.remove(id)
    }
}