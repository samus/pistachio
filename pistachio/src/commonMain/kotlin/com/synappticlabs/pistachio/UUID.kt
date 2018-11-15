package com.synappticlabs.pistachio

expect class UUID {}

expect val UUID.UUIDString: String

expect object UUIDFactory {
    fun create(): UUID
    fun fromString(string: String): UUID?
}

//companion object {
//    fun create(): UUID
//    fun fromString(string: String): UUID?
//}