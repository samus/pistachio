package com.synappticlabs.pistachio.repositories

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.darwin.*
import platform.objc.*


import com.synappticlabs.pistachio.*
import com.synappticlabs.pistachio.Repository


/*
  * KeyArchivingRepository will write out objects to a directory that have been serialized with
  * NSCoding and NSKeyArchiving.
  *
  * WARNING: Currently due to limitations with Kotlin Native this code crashes.
  * Once passing ObjC objects to ObjC functions is supported it will be fixed.
 */
class KeyArchivingRepository<T>(override val name: String, val path: String): Repository<T> {
    private val fileManager = NSFileManager.defaultManager
    private val directory: NSURL
    init {
        directory = NSURL.fileURLWithPath(path, isDirectory = true)
    }

    override fun put(obj: T): UUID {
        val uuid = UUID.create()

        this.write(uuid, obj)

        return uuid
    }

    override fun update(obj: T, uuid: UUID) {
        this.write(uuid, obj)
    }

    override fun read(id: UUID): T? {
        val file = fileURL(id) ?: return null
        return readFile(file)
    }

    override fun scan(filter: (T) -> Boolean): List<T> {
        val files: List<String> = fileManager.contentsOfDirectoryAtPath(directory.absoluteString!!, null).toList()
        val urls: List<NSURL> = files.mapNotNull {path: String ->
            return@mapNotNull NSURL.fileURLWithPath(path, isDirectory = true)
        }
        val objs = ArrayList<T>()
        urls.forEach { path: NSURL ->
            readFile(path)?.let { it ->
                if (filter(it)) {
                    objs.add(it)
                }
            }
        }

        return objs
    }

    override fun delete(id: UUID) {
        val file = fileURL(id) ?: return
        fileManager.removeItemAtURL(file, error = null)
    }

    internal fun write(uuid: UUID, obj: T) {
        if (obj !is ObjCObject) {
            throw IllegalArgumentException("Object: $obj must be a subclass of NSObject and implement NSCoding.")
        }
        val objc = obj as ObjCObject
        val data = NSKeyedArchiver.archivedDataWithRootObject(objc)
        val file = fileURL(uuid)
        val path = file!!.path!!
        fileManager.removeItemAtURL(file, error = null)
        fileManager.createFileAtPath(path, contents = data, attributes = null)
    }

    internal fun readFile(url: NSURL): T? {
        val file = url.absoluteString ?: return null
        if (fileManager.fileExistsAtPath(file) == false) { return null }
        try {
            //Not sure if this is a good idea but it won't compile otherwise.
            @Suppress("UNCHECKED_CAST")
            return NSKeyedUnarchiver.unarchiveObjectWithFile(file) as? T
        } catch (error: Throwable) {
            return null
        }
    }

    internal fun fileURL(uuid: UUID): NSURL? {
        return directory.URLByAppendingPathComponent(uuid.UUIDString)
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