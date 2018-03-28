package com.synappticlabs.pistachio

typealias DispatchFunction = (cmd: Command) -> Unit
typealias Middleware = (dispatch: DispatchFunction, repositories: Map<String, Repository<*>>) -> (DispatchFunction) -> DispatchFunction