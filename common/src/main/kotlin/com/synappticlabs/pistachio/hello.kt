package com.synappticlabs.pistachio

class Hello {
    fun sayHello(name: String): String {
        return "Hello $name"
    }
    fun sayHello(firstName: String, lastName: String): String {
        return "Hello $firstName $lastName"
    }
}

class Foo {
    fun bar(): String {
        return "baz"
    }
}
