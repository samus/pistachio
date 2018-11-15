package com.synappticlabs.pistachio

import com.synappticlabs.pistachio.repostories.InMemoryRepository

internal class PersonRepository: InMemoryRepository<Person>("People") {
    var dispatchCount = 0
    override fun apply(command: Command, changeList: ChangeList) {
        dispatchCount += 1
        when (command) {
            is AddPersonCommand -> {
                val id = put(command.person)
                changeList.added(this.name, id)
            }
        }
    }
}

internal class AddPersonCommand(val person: Person): Command