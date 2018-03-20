package com.synappticlabs.pistachio

import java.util.UUID as JUUID

actual class UUID {
    private lateinit var juuid: JUUID
    actual companion object {
        actual fun fromString(string: String): UUID? {
            return JUUID.fromString(string)?.let { juuid ->
                val uuid = UUID()
                uuid.juuid = juuid
                return@let uuid
            }
        }
    }
}