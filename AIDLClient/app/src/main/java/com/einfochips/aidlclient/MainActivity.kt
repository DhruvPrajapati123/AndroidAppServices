package com.einfochips.aidlclientserver

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.einfochips.aidlserver.AidlServerInterface
import com.einfochips.aidlserver.Student
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var sumText: TextView
    private var isBound = false
    private var mServerService: AidlServerInterface? = null;
    private val serverServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("Dhruv", "onServiceConnected: called")
            isBound = true
            mServerService = AidlServerInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("Dhruv", "onServiceDisconnected: called")
            isBound = false
            mServerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sumButton = findViewById<Button>(R.id.sumBtn)
        sumText = findViewById(R.id.sumText)
        sumButton.setOnClickListener(mSumOnClickListener)
    }

    @SuppressLint("SetTextI18n")
    private val mSumOnClickListener = View.OnClickListener {
        val a = Random.nextInt(100)
        val b = Random.nextInt(100)

        sumText.text = "Sum expression: $a + $b = ${mServerService?.add(a, b)}\n" +
                "${mServerService?.getStudentInfo(Student(13, "Dhruv", "Prajapati", 23, 56, 89))}"

    }

    override fun onStart() {
        super.onStart()
        if (!isBound) {
            val intent = Intent("ServerService")
            intent.setPackage("com.einfochips.aidlserver")
            bindService(intent, serverServiceConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        if (isBound) {
            unbindService(serverServiceConnection)
            isBound = false
            mServerService = null
        }
        super.onStop()
    }
}