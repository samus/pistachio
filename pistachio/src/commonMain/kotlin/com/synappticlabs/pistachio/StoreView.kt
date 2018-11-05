package com.synappticlabs.pistachio

abstract class StoreView {

    var listener: StoreViewListener? = null

    fun storeInitialized(repositories: Map<String,Repository<*>>) {
        initialize(repositories)
        listener?.onViewReady(this)
    }

    internal fun storeChanged(repositories: Map<String,Repository<*>>, changeSet: ChangeList) {
        update(repositories, changeSet)
        listener?.onViewChanged(this)
    }

    abstract fun initialize(repositories: Map<String,Repository<*>>)
    abstract fun update(repositories: Map<String,Repository<*>>, changeSet: ChangeList)

}

interface StoreViewListener {
    fun onViewReady(view: StoreView)
    fun onViewChanged(view: StoreView)
}