open class Hello {
    fun sayHello(name: String): String {
        return "Hello $name"
    }
    fun sayHello(firstName: String, lastName: String): String {
        return "Hello $firstName $lastName"
    }
}