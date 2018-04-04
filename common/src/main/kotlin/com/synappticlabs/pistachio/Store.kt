package com.synappticlabs.pistachio

class Store (val repositories: Map<String, Repository<*>>,
             private val middleware: List<Middleware> = emptyList(),
             private val dispatcher: Dispatch = Dispatcher()){

    private val views = ArrayList<StoreView>()

    fun dispatch(command: Command) {
        dispatcher.dispatch {
            val dispatch = { cmd: Command -> this.dispatch(cmd) }
            var cmd = command
            for (m in middleware) {
                cmd = m.next(cmd, repositories, dispatch) ?: break
            }
            val changes = cmd.apply(repositories)
            views.forEach { it.storeChanged(repositories, changes) }
        }
    }

    fun registerView(view: StoreView) {
        views.add(view)
        view.storeInitialized(repositories)
    }

    fun unregisterView(view: StoreView) {
        views.remove(view)
    }
}
