package com.synappticlabs.pistachio


import kotlin.test.*

object EmptyCommand: Command

@Suppress("unused")
class MiddlewareTests {
    private lateinit var store: Store
    private lateinit var peopleRepo : PersonRepository

    @BeforeTest
    fun setup() {
        peopleRepo = PersonRepository()
        listOf(Person("James", "Brown", 77),
                Person("Em", "Inem", 34))
            .forEach { peopleRepo.put(it) }
    }

    @Test
    fun dispatchesWithNoMiddleware() {
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), dispatcher = MockDispatcher())
        val count = peopleRepo.dispatchCount
        store.dispatch(command = EmptyCommand)

        assertEquals(1, peopleRepo.dispatchCount - count, "Store did not dispatch")
    }

    fun dispatchesWithOneMiddleware() {
        var middlewareExecuted = false
        val assertMiddleware = object: Middleware {
            override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
                middlewareExecuted = true
                return command
            }
        }

        val count = peopleRepo.dispatchCount
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware), dispatcher = MockDispatcher())
        store.dispatch(EmptyCommand)
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(1, peopleRepo.dispatchCount - count, "Store did not dispatch")
    }

    fun dispatchesWithTwoMiddleware() {
        var middleware1Executed = false
        val assertMiddleware1 = object: Middleware {
            override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
                middleware1Executed = true
                return command
            }

        }
        var middleware2Executed = false
        val assertMiddleware2 = object: Middleware {
            override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
                middleware2Executed = true
                return command
            }
        }

        val count = peopleRepo.dispatchCount
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware1, assertMiddleware2), dispatcher = MockDispatcher())
        store.dispatch(EmptyCommand)
        assertEquals(true, middleware1Executed, "Store did not execute middleware 1")
        assertEquals(true, middleware2Executed, "Store did not execute middleware 2")
        assertEquals(1, peopleRepo.dispatchCount - count, "Store did not dispatch")
    }

    fun middlewareCanHaltDispatch() {
        var middlewareExecuted = false
        val assertMiddleware = object: Middleware {
            override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
                middlewareExecuted = true
                return null
            }
        }

        val count = peopleRepo.dispatchCount
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware), dispatcher = MockDispatcher())
        store.dispatch(EmptyCommand)
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(count, peopleRepo.dispatchCount, "Store did dispatch")
    }

    fun middlewareCanDispatchAlternateCommand() {
        class ABCommand: Command

        var middlewareExecuted = false
        val assertMiddleware = object: Middleware {
            override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
                dispatch(ABCommand())
                middlewareExecuted = true
                return command
            }
        }
        val count = peopleRepo.dispatchCount
        store = Store(mapOf(Pair(peopleRepo.name, peopleRepo)), listOf(assertMiddleware), dispatcher = MockDispatcher())
        store.dispatch(EmptyCommand)
        assertEquals(true, middlewareExecuted, "Store did not execute middleware")
        assertEquals(2, peopleRepo.dispatchCount - count, "Store did not dispatch all commands")
    }
}

