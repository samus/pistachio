package com.synappticlabs.pistachio

import platform.Foundation.*

actual class UUID {
    private var nsuuid: NSUUID

    internal constructor(uuid: NSUUID) {
        nsuuid = uuid
    }

    actual val UUIDString: String
            get() = nsuuid.UUIDString

    actual companion object {

        actual fun create(): UUID {
            return UUID(NSUUID())
        }

        actual fun fromString(string: String): UUID? {
            return NSUUID(uUIDString = string)?.let { id ->
                return@let UUID(id)
            }
        }
    }
}