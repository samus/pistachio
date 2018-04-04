package com.synappticlabs.pistachio


import com.synappticlabs.pistachio.repostories.InMemoryRepository
import kotlin.test.*

@Suppress("unused")
class StoreViewTests {
    private lateinit var store: Store

    @BeforeTest
    fun setup() {
        val repo = InMemoryRepository<Person>(name = "People")
        listOf(Person("James", "Brown", 77),
                Person("Em", "Inem", 34)).forEach { repo.put(it) }
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
        store.dispatch(AddPersonCommand(Person("old", "man", 1000)))
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

    internal class AddPersonCommand(private val person: Person): Command {
        override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
            @Suppress("UNCHECKED_CAST")
            val repo = repositories["People"] as Repository<Person>
            val changes = ChangeList()
            changes.added(repo.name, repo.put(person))
            return changes
        }
    }
}