package com.synappticlabs.pistachio

sealed class Change(val repositoryName: String, val uuid: UUID) {
    class Addition(repositoryName: String, uuid: UUID) : Change(repositoryName, uuid)
    class Update(repositoryName: String, uuid: UUID) : Change(repositoryName, uuid)
    class Remove(repositoryName: String, uuid: UUID) : Change(repositoryName, uuid)
}

class ChangeList {
    val changes = ArrayList<Change>()

    fun added(repositoryName: String, uuid: UUID) {
        changes.add(Change.Addition(repositoryName, uuid))
    }
    fun added(repositoryName: String, uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Addition(repositoryName, it) })
    }
    fun removed(repositoryName: String, uuid: UUID) {
        changes.add(Change.Remove(repositoryName, uuid))
    }
    fun removed(repositoryName: String, uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Remove(repositoryName, it) })
    }
    fun updated(repositoryName: String, uuid: UUID) {
        changes.add(Change.Update(repositoryName, uuid))
    }
    fun updated(repositoryName: String, uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Update(repositoryName, it) })
    }

    companion object {
        val empty = ChangeList()
    }
}