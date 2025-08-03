package com.einfochips.ipcusingmessenger

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sendBtn: Button
    private var mService: Messenger? = null
    private var isBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("Dhruv", "onServiceConnected: called")
            mService = Messenger(service)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            isBound = false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendBtn = findViewById(R.id.sendBtn)
        sendBtn.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d("Dhruv", "onStart: called")
        // Bind to the service.
        val intent = Intent("MyRemoteProcess")
        intent.setPackage("com.einfochips.remoteappprocess")
        Log.d("Dhruv", bindService(intent, serviceConnection, BIND_AUTO_CREATE).toString())

    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    fun sayHello() {
        if (!isBound) return
        mService?.send(Message.obtain(null, 1, 0, 0))
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.sendBtn -> sayHello()
        }
    }
}