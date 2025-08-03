package com.einfochips.aidlclient

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
import com.einfochips.aidlserver.MyAidlInterface
import com.einfochips.aidlserver.Student

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var textView: TextView
    private var myRemoteService: MyAidlInterface? = null
    private var isBound = false

    private val serviceConnetion = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: caled")
            isBound = true
            myRemoteService = MyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: caled")
            isBound = false
            myRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBtn = findViewById<Button>(R.id.actionBtn)
        val bindBtn = findViewById<Button>(R.id.bindBtn)
        val unbindBtn = findViewById<Button>(R.id.unbindBtn)
        textView = findViewById(R.id.textView)

        actionBtn.setOnClickListener(actionBtnOnClickListener)
        bindBtn.setOnClickListener(bindBtnOnClickListener)
        unbindBtn.setOnClickListener(unbindBtnOnClickListener)
    }

    private val actionBtnOnClickListener = View.OnClickListener {
//        textView.text = "${myRemoteService?.randomNumber}"
//        textView.text = "${myRemoteService?.getStudentDetails(Student(1, "Dhruv", 97, 85, 94))}"
//        textView.text = myRemoteService?.createStudent().toString()
//        textView.text = myRemoteService?.getResult(Student(21, "Dhruv", 56, 78, 88)).toString()
        val resultKt = myRemoteService?.getResultKt(Student(22, "Maddy", 56, 78, 88))
        textView.text = resultKt.toString()
    }

    private val bindBtnOnClickListener = View.OnClickListener {
        if (!isBound) {
            val serviceIntent = Intent("android.intent.action.RemoteService")
            serviceIntent.setPackage("com.einfochips.aidlserver")
            bindService(serviceIntent, serviceConnetion, BIND_AUTO_CREATE)
        }
    }

    private val unbindBtnOnClickListener = View.OnClickListener {
        if (isBound) {
            unbindService(serviceConnetion)
            isBound = false
            myRemoteService = null
        }
    }
}