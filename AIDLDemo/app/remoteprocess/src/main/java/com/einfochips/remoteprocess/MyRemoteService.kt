package com.einfochips.remoteprocess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.random.Random

class MyRemoteService: Service() {

    private val binder = object : MyAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            TODO("Not yet implemented")
        }

        override fun getRandomNumber(): Int = Random.nextInt(100)
    }

    override fun onBind(intent: Intent?): IBinder = binder
}