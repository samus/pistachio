package com.synappticlabs.pistachio

expect class UUID {
    companion object {
        fun fromString(string: String): UUID?
    }
}