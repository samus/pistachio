package com.synappticlabs.pistachio.repositories

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.darwin.*
import platform.objc.*


import com.synappticlabs.pistachio.Repository
import com.synappticlabs.pistachio.UUID

class KeyArchivingRepository<T>(override val name: String, val path: String): Repository<T> {
    private val fileManager = NSFileManager.defaultManager
    private val directory: NSURL
    init {
        directory = NSURL.fileURLWithPath(path, isDirectory = true)
    }
    override fun put(obj: T): UUID {
        if (obj !is ObjCObject) {
            throw IllegalArgumentException("Object: $obj must be a subclass of NSObject.")
        }
        val objc = obj as ObjCObject
        val uuid = UUID.create()

        val data = NSKeyedArchiver.archivedDataWithRootObject(objc)
        val file = directory.URLByAppendingPathComponent(uuid.UUIDString)
        val path = file!!.path!!
        fileManager.removeItemAtURL(file, error = null)
        fileManager.createFileAtPath(path, contents = data, attributes = null)
        return uuid
    }

    override fun update(obj: T, uuid: UUID) {

    }

    override fun read(id: UUID): T? {
        return null
    }

    override fun scan(filter: (T) -> Boolean): List<T> {
        return emptyList()
    }

    override fun delete(id: UUID) {

    }

    companion object {
        fun <T>repositoryNamed(name: String): KeyArchivingRepository<T>? {
            val fileManager = NSFileManager.defaultManager
            val documents = fileManager.URLForDirectory(NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null) ?: return null
            val directory = documents.URLByAppendingPathComponent(name, isDirectory = true) ?: return null
            val path = directory.path ?: return null

            val created = fileManager.createDirectoryAtURL (directory, withIntermediateDirectories = true,
                    attributes = null,
                    error = null)
            if (created) {
                return KeyArchivingRepository<T>(name = name, path = path)
            } else {
                return null
            }
        }
    }
}