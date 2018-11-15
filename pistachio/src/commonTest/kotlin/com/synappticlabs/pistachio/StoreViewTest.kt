package com.synappticlabs.pistachio


import com.synappticlabs.pistachio.repostories.InMemoryRepository
import kotlin.test.*

@Suppress("unused")
class StoreViewTests {
    private lateinit var store: Store

    @BeforeTest
    fun setup() {
        val repo = PersonRepository()
        listOf(Person(firstName = "James", lastName = "Brown", age =  77),
                Person(firstName = "Em", lastName = "Inem", age = 34))
                .forEach { repo.put(it, it.id) }
        store = Store(mapOf(Pair(repo.name, repo)), dispatcher = MockDispatcher())
    }

    @Test
    fun initializes() {
        val v = PersonStoreView()
        store.registerView(v)
        assertEquals(2, v.populationCount)
        assertNotNull(v.oldest)
        assertEquals(77, v.oldest?.age)
    }

    @Test
    fun updates() {
        val v = PersonStoreView()
        store.registerView(v)
        store.dispatch(AddPersonCommand(Person(firstName = "old", lastName = "man", age =  1000)))
        assertEquals(1000, v.oldest?.age, "View did not update")
    }

    internal class PersonStoreView: StoreView() {
        override fun initialize(repositories: Map<String, Repository<*>>) {
            val repo = repositories["People"] ?: return
            rebuild(repo)
        }

        override fun update(repositories: Map<String, Repository<*>>, changeSet: ChangeList) {
            val repo = repositories["People"] ?: return
            rebuild(repo)
        }

        private fun rebuild(repository: Repository<*>) {
            repository.scan { true }.forEach { obj ->
                when (obj) {
                    is Person -> {
                        this.populationCount++
                        this.oldest.let { oldest ->
                            if (oldest == null || obj.age > oldest.age) {
                                this.oldest = obj
                            }
                        }
                    }
                }
            }
        }

        var populationCount = 0
        var oldest: Person? = null
    }
}