package com.synappticlabs.pistachio.repositories

import com.synappticlabs.pistachio.ChangeList
import com.synappticlabs.pistachio.Command
import com.synappticlabs.pistachio.UUID
import com.synappticlabs.pistachio.UUIDFactory
import com.synappticlabs.pistachio.repostories.InMemoryRepository
import kotlin.test.*

@Suppress("unused")
class InMemoryRepositoryTest {
    private lateinit var repo: FooRepo

    @BeforeTest
    fun setup() {
        repo = FooRepo()
    }

    @Test
    fun putsAndRetrievesAFoo() {
        val foo = Foo(name = "test")
        repo.put(foo, foo.id)
        val actual = repo.read(foo.id) ?: fail("Foo not retrieved from repo.")
        assertEquals(foo, actual, "Retrieved Foo is not the same as the put Foo.")
    }

    @Test
    fun scansARepo(){
        listOf("1a", "1b", "2c", "2d", "3e").map { Foo(name = it) }.forEach { repo.put(it, it.id) }
        val actual = repo.scan { it.name.startsWith("1") }
        assertEquals(2, actual.count())
    }

    internal data class Foo (val id: UUID = UUIDFactory.create(), val name: String)
    internal class FooRepo: InMemoryRepository<Foo>("Foo") {
        override fun apply(command: Command, changeList: ChangeList) {
        }
    }
}

