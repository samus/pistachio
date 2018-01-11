package com.synappticlabs.pistachio

import kotlin.test.*

@Suppress("unused")
class AdderTest {
    lateinit var adder: Adder

    @BeforeTest
    fun setup() {
        adder = Adder()
    }

    @Test
    fun addsTwoNumbers() {
        assertTrue { 2 == adder.add(1, 1) }
        assertTrue { 3 == adder.add(1, 2) }
    }
}