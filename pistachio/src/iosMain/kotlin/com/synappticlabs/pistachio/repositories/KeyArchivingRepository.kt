package com.synappticlabs.pistachio.repositories

import platform.Foundation.*

import com.synappticlabs.pistachio.*
import com.synappticlabs.pistachio.Repository


/*
  * KeyArchivingRepository will write out objects to a directory that have been serialized with
  * NSCoding and NSKeyArchiving.
  *
 */
abstract class KeyArchivingRepository<T>(override val name: String, path: String): Repository<T> {
    private val fileManager = NSFileManager.defaultManager
    private val directory: NSURL = NSURL.fileURLWithPath(path, isDirectory = true)

    override fun put(obj: T, uuid: UUID) {
        write(uuid, obj)
    }

    override fun update(obj: T, uuid: UUID) {
        write(uuid, obj)
    }

    override fun read(id: UUID): T? {
        val file = fileURL(id) ?: return null
        return readFile(file)
    }

    override fun scan(filter: (T) -> Boolean): List<T> {
        val objs = ArrayList<T>()
        val contents = fileManager.contentsOfDirectoryAtPath(directory.path!!, null) ?: return objs

        val files: List<String> = contents.mapNotNull { it ->
            return@mapNotNull it as String
        }.filter { it -> !it.startsWith(".") }

        val urls: List<NSURL> = files.mapNotNull {file: String ->
            return@mapNotNull directory.URLByAppendingPathComponent(file)
        }
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

    private fun write(uuid: UUID, obj: T) {
        val objc = obj as Any
        val data = NSKeyedArchiver.archivedDataWithRootObject(objc)
        val file = fileURL(uuid)
        val path = file!!.path!!
        fileManager.removeItemAtURL(file, error = null)
        fileManager.createFileAtPath(path, contents = data, attributes = null)
    }

    private fun readFile(url: NSURL): T? {
        val file = url.path ?: return null
        if (fileManager.fileExistsAtPath(file) == false) { return null }
        try {
            //Not sure if this is a good idea but it won't compile otherwise.
            @Suppress("UNCHECKED_CAST")
            return NSKeyedUnarchiver.unarchiveObjectWithFile(file) as? T
        } catch (error: Throwable) {
            return null
        }
    }

    private fun fileURL(uuid: UUID): NSURL? {
        return directory.URLByAppendingPathComponent(uuid.UUIDString)
    }

    companion object {
        fun pathForRepositoryNamed(name: String): String? {
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
            return if (created) {
                path
            } else {
                null
            }
        }
    }
}