package com.synappticlabs.pistachio

class Store (private val repositories: Map<String, Repository<*>>,
             private val middleware: List<Middleware> = emptyList()){
    private val dispatcher = Dispatcher()
    private val views = ArrayList<StoreView>()

    fun dispatch(cmd: Command) {
        this.wrappedDispatchFunction(cmd)
    }

    fun registerView(view: StoreView) {
        views.add(view)
        view.storeInitialized(repositories)
    }

    fun unregisterView(view: StoreView) {
        views.remove(view)
    }

    private var wrappedDispatchFunction: DispatchFunction = middleware
            .reversed()
            .fold(initial = { cmd: Command -> this.primaryDispatchFunction(cmd)},
                operation = {dispatchFunction, middleware ->
                    val dispatch = { cmd: Command -> this.dispatch(cmd)}
                    middleware(dispatch, this.repositories)(dispatchFunction)
                })

    private fun primaryDispatchFunction(cmd: Command) {
        dispatcher.dispatch {
            val changes = cmd.apply(repositories)
            views.forEach { it.storeChanged(repositories, changes) }
        }
    }
}
