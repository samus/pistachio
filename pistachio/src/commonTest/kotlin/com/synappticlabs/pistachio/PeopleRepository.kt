package com.synappticlabs.pistachio

import com.synappticlabs.pistachio.repostories.InMemoryRepository

internal class PersonRepository: InMemoryRepository<Person>("People") {
    var dispatchCount = 0
    override fun apply(command: Command, changeList: ChangeList) {
        dispatchCount += 1
        when (command) {
            is AddPersonCommand -> {
                put(command.person, command.person.id)
                changeList.added(this.name, command.person.id)
            }
        }
    }
}

internal class AddPersonCommand(val person: Person): Command