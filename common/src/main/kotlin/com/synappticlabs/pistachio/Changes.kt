package com.synappticlabs.pistachio

sealed class Change(val uuid: UUID) {
    class Addition(uuid: UUID) : Change(uuid)
    class Update(uuid: UUID) : Change(uuid)
    class Remove(uuid: UUID) : Change(uuid)
}

class ChangeList {
    val changes = ArrayList<Change>()

    fun added(uuid: UUID) {
        changes.add(Change.Addition(uuid))
    }
    fun added(uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Addition(it) })
    }
    fun removed(uuid: UUID) {
        changes.add(Change.Remove(uuid))
    }
    fun removed(uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Remove(it) })
    }
    fun updated(uuid: UUID) {
        changes.add(Change.Update(uuid))
    }
    fun updated(uuids: List<UUID>) {
        changes.addAll(uuids.map { Change.Update(it) })
    }
}