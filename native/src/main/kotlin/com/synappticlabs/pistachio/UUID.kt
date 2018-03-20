package com.synappticlabs.pistachio

import platform.Foundation.*

actual class UUID {
    private lateinit var _uuid: NSUUID
    actual companion object {
        actual fun fromString(string: String): UUID? {
            return NSUUID(string = string)?.let { id ->
                val uuid = UUID()
                uuid._uuid = id
                return@let uuid
            }
        }
    }
}