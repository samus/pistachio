package com.synappticlabs.pistachio

import java.util.UUID as JUUID

actual class UUID {
    private var juuid: JUUID

    internal constructor(uuid: JUUID) {
        juuid = uuid
    }

    actual companion object {
        actual fun create(): UUID {
            return UUID(JUUID.randomUUID())
        }

        actual fun fromString(string: String): UUID? {
            return JUUID.fromString(string)?.let { juuid ->
                return@let UUID(juuid)
            }
        }
    }

    actual val UUIDString: String
        get() = juuid.toString()
}