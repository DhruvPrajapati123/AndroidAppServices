package com.einfochips.boundserviceextendbinderdemo

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
import android.widget.Toast


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var randomNumberText: TextView
    private lateinit var serviceIntent: Intent
    private var mService: BoundService? = null
    private var isBound = false
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("Dhruv", "onServiceConnected: called")

            // We've bound to BoundService, cast the IBinder and get BoundService instance.
            val binder = service as BoundService.MyBinder
            mService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("Dhruv", "onServiceDisconnected: called")
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomNumberText = findViewById(R.id.randomNumber)
        serviceIntent = Intent(this, BoundService::class.java)
        val randomNumberBtn = findViewById<Button>(R.id.randomNumberBtn)
        val bindBtn = findViewById<Button>(R.id.bindBtn)
        val unbindBtn = findViewById<Button>(R.id.unbindBtn)

        randomNumberBtn.setOnClickListener(this)
        bindBtn.setOnClickListener(this)
        unbindBtn.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d("Dhruv", "onStop: called")
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.randomNumberBtn -> {
                    randomNumberText.text = mService?.getRandomNumber().toString()
                }
                R.id.bindBtn -> {
                    if (!isBound) {
                        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)
                    }
                }
                R.id.unbindBtn -> {
//                    if (isBound) {
//                        unbindService(serviceConnection)
//                    }
                    Toast.makeText(this, "Please back from application to unbind", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}