package com.einfochips.boundserviceextendbinderdemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import java.util.*

class BoundService: Service() {
    private val myBinder = MyBinder()

    inner class MyBinder: Binder() {
        fun getService(): BoundService = this@BoundService
    }

    override fun onBind(intent: Intent?): MyBinder = myBinder

    fun getRandomNumber() = Random().nextInt(100)
}