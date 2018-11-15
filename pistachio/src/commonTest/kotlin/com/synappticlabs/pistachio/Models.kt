package com.synappticlabs.pistachio

internal data class Person (val id: UUID = UUIDFactory.create(), val firstName: String, val lastName: String, val age: Int)
