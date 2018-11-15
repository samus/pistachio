package com.synappticlabs.pistachio

import platform.Foundation.*

actual typealias UUID = platform.Foundation.NSUUID

actual val UUID.UUIDString: String
    get() = this.UUIDString

actual object UUIDFactory {
    actual fun create(): UUID {
        return UUID()
    }

    actual fun fromString(string: String): UUID? {
        return UUID(uUIDString = string)
    }
}