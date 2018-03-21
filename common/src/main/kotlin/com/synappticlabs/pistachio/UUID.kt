package com.synappticlabs.pistachio

expect class UUID {
    companion object {
        fun create(): UUID
        fun fromString(string: String): UUID?
    }
}