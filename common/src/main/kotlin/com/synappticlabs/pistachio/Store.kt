package com.synappticlabs.pistachio

class Store (private val repositories: Map<String, Repository<*>>){
    private val dispatcher = Dispatcher()
    private val views = ArrayList<StoreView>()

    fun dispatch(cmd: Command) {
        dispatcher.dispatch {
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
