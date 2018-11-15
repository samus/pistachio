package com.synappticlabs.pistachio

import java.util.UUID.*

actual typealias UUID = java.util.UUID

actual val UUID.UUIDString: String
    get() = this.toString()


actual object UUIDFactory {
    actual fun create(): UUID {
        return randomUUID()
    }

    actual fun fromString(string: String): UUID? {
        return java.util.UUID.fromString(string)
    }
}