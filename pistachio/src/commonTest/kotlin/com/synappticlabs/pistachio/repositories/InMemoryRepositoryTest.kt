package com.synappticlabs.pistachio.repositories

import com.synappticlabs.pistachio.ChangeList
import com.synappticlabs.pistachio.Command
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
        val foo = Foo("test")
        val id = repo.put(foo)
        val actual = repo.read(id)
        assertEquals(foo, actual, "Unable to put and retrieve an object")
    }

    @Test
    fun scansARepo(){
        listOf("1a", "1b", "2c", "2d", "3e").map { Foo(it) }.forEach { repo.put(it) }
        val actual = repo.scan { it.name.startsWith("1") }
        assertEquals(2, actual.count())
    }

    internal data class Foo (val name: String)
    internal class FooRepo: InMemoryRepository<Foo>("Foo") {
        override fun apply(command: Command, changeList: ChangeList) {
        }
    }
}

