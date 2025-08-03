package com.einfochips.remoteprocess

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var randomNumText: TextView
    private var mService: MyAidlInterface? = null;
    private var isBound = false;
    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("Dhruv", "onServiceConnected: called")
            mService = MyAidlInterface.Stub.asInterface(service)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("Dhruv", "onServiceDisconnected: called")
            mService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getRandomNumberBtn = findViewById<Button>(R.id.getRandomNumberBtn)
        val bindBtn = findViewById<Button>(R.id.bindBtn)
        val unbindBtn = findViewById<Button>(R.id.unbindBtn)
        randomNumText = findViewById<TextView>(R.id.randomNumText)
        getRandomNumberBtn.setOnClickListener(this)
        bindBtn.setOnClickListener(this)
        unbindBtn.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.getRandomNumberBtn -> randomNumText.text = mService?.randomNumber.toString()
            R.id.bindBtn -> {
                if (!isBound) {
//                    bindService(Intent(this, MyService::class.java), serviceConnection, BIND_AUTO_CREATE)
                    val intent = Intent("MyRemoteService")
                    intent.setPackage("com.einfochips.remoteprocess")
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE)
                }
            }
            R.id.unbindBtn -> {
                if (isBound) {
                    unbindService(serviceConnection)
                    mService = null
                    isBound = false
                }
            }
        }
    }
}