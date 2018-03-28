package com.synappticlabs.pistachio

typealias DispatchFunction = (command: Command) -> Unit
//typealias Middleware = (dispatch: DispatchFunction, repositories: Map<String, Repository<*>>) -> (DispatchFunction) -> DispatchFunction

interface Middleware {
    fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command?
}