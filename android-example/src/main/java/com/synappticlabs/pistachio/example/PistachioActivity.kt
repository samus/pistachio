package com.synappticlabs.pistachio.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.synappticlabs.pistachio.Hello

class PistachioActivity : AppCompatActivity() {
    init {
        val h = Hello()
        val greeting = h.sayHello("Buddy")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pistachio)
    }
}
