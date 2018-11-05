package com.synappticlabs.pistachio

expect class UUID {
    val UUIDString: String

    companion object {
        fun create(): UUID
        fun fromString(string: String): UUID?
    }
}