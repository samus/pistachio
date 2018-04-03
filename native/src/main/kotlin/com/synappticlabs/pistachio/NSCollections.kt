package com.synappticlabs.pistachio

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.darwin.*
import platform.objc.*

internal fun <T> NSArray?.toList(): kotlin.collections.List<T> {
    if (this == null) return listOf()
    return objectEnumerator().toList()
}

internal fun <T> NSSet?.toList(): kotlin.collections.List<T> {
    if (this == null) return listOf()
    return objectEnumerator().toList()
}

internal fun <T> NSEnumerator.toList(): List<T> {
    val items = mutableListOf<T>()
    var obj = nextObject()
    while (obj != null) {
        items += obj.uncheckedCast<T>()
        obj = nextObject()
    }
    return items
}