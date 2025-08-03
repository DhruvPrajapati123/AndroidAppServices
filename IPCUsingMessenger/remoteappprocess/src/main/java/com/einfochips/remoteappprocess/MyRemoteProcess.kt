package com.einfochips.remoteappprocess

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

class MyRemoteProcess: Service() {

    /**
     * Target we publish for clients to send messages to MyHandler.
     */
    private lateinit var messenger: Messenger

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(MyHandler(this))
        return messenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class MyHandler(val applicationContext: Context): Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                1 -> Toast.makeText(applicationContext, "hello from another application", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }
}