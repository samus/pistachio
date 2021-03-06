package com.synappticlabs.pistachio

import android.content.Context
import java.io.File
import java.io.File.pathSeparator
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.IOException
import java.lang.ClassNotFoundException

abstract class SerializedRepository<T:Any>(override val name: String,
                                           val directory: File,
                                           val context: Context) : Repository<T> {

    override fun put(obj: T, uuid: UUID) {
        write(uuid, obj)
    }

    override fun update(obj: T, uuid: UUID) {
        write(uuid, obj)
    }

    override fun read(id: UUID): T? {
        val file = File(directory, id.UUIDString)
        return read(file)
    }

    override fun scan(filter: (T) -> Boolean): List<T> {
        val objects = directory.listFiles()
                .filter { it.isFile() }
                .mapNotNull { this.read(it)}
                .filter { filter(it) }
        return objects
    }

    override fun delete(id: UUID) {
        val file = File(directory, id.UUIDString)
        file.delete()
    }

    private fun write(uuid: UUID, obj: T) {
        val file = File(directory, uuid.UUIDString)
        val fStream = FileOutputStream(file)
        val objStream = ObjectOutputStream(fStream)

        objStream.writeObject(obj)

        fStream.close()
        objStream.close()
    }

    private fun read(file: File): T? {
        try {
            val fStream = FileInputStream(file)
            val objStream = ObjectInputStream(fStream)
            @Suppress("UNCHECKED_CAST")
            val obj = objStream.readObject() as? T
            objStream.close()
            fStream.close()
            return obj
        } catch (i: IOException) {
            return null
        } catch (c: ClassNotFoundException) {
            return null
        }
    }

    companion object {
        fun directoryForRepositoryNamed(name: String, context: Context): File {
            return context.getDir("pistachio" + pathSeparator + name, Context.MODE_PRIVATE)

        }
    }
}