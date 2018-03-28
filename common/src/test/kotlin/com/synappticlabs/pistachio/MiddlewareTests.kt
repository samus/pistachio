package com.synappticlabs.pistachio


import com.synappticlabs.pistachio.repostories.InMemoryRepository
import kotlin.test.*

@Suppress("unused")
class MiddlewareTests {
    private lateinit var store: Store
    private lateinit var peopleRepo : Repository<Person>

    @BeforeTest
    fun setup() {
        peopleRepo = InMemoryRepository<Person>(name = "People")
        listOf(Person("James", "Brown", 77),
                Person("Em", "Inem", 34))
            .forEach { peopleRepo.put(it) }
    }

    @Test
    fun dispatchesWithNoMiddleware() {
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)))
        var dispatched = false
        store.dispatch(object: Command{
            override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                dispatched = true
                return ChangeList()
            }
        })
        assertEquals(true, dispatched, "Store did not dispatch")
    }

    fun dispatchesWithOneMiddleware() {
        var dispatched = false
        var middlewareExecuted = false
        val assertMiddleware: Middleware = { _, _ ->
            { next ->
                { command ->
                    middlewareExecuted = true
                    next(command)
                }
            }
        }
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware))
        store.dispatch(object: Command{
            override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                dispatched = true
                return ChangeList()
            }
        })
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(true, dispatched, "Store did not dispatch")
    }

    fun dispatchesWithTwoMiddleware() {
        var dispatched = false
        var middleware1Executed = false
        val assertMiddleware1: Middleware = { _, _ ->
            { next ->
                { command ->
                    middleware1Executed = true
                    next(command)
                }
            }
        }
        var middleware2Executed = false
        val assertMiddleware2: Middleware = { _, _ ->
            { next ->
                { command ->
                    middleware2Executed = true
                    next(command)
                }
            }
        }
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware1, assertMiddleware2))
        store.dispatch(object: Command{
            override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                dispatched = true
                return ChangeList()
            }
        })
        assertEquals(true, middleware1Executed, "Store did not execute middleware 1")
        assertEquals(true, middleware2Executed, "Store did not execute middleware 2")
        assertEquals(true, dispatched, "Store did not dispatch")
    }

    fun middlewareCanHaltDispatch() {
        var dispatched = false
        var middlewareExecuted = false
        val assertMiddleware: Middleware = { _, _ ->
            { _ ->
                { _ ->
                    middlewareExecuted = true
                }
            }
        }
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware))
        store.dispatch(object: Command{
            override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                dispatched = true
                return ChangeList()
            }
        })
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(false, dispatched, "Store did dispatch")
    }

    fun middlewareCanDispatchAlternateCommand() {
        var dispatched = false
        var altDispatched = false
        var middlewareExecuted = false
        val assertMiddleware: Middleware = { dispatch, _ ->
            { next ->
                { command ->
                    middlewareExecuted = true
                    next(command)
                    dispatch(object: Command{
                        override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                            altDispatched = true
                            return ChangeList()
                        }
                    })
                }
            }
        }
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware))
        store.dispatch(object: Command{
            override fun apply(repositories: Map<String, Repository<*>>): ChangeList {
                dispatched = true
                return ChangeList()
            }
        })
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(true, dispatched, "Store did not dispatch")
        assertEquals(true, altDispatched, "Store did not dispatch midleware command")

    }
}

