package com.einfochips.aidlserver

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class ServerService: Service() {

    private val binder = object : AidlServerInterface.Stub() {
        override fun add(a: Int, b: Int) = a + b

        override fun getStudentInfo(student: Student?): String {
            return "Roll No:" + student?.rollNo + " Name: " + student?.firstName + " " + student?.lastName
        }
    }
    override fun onBind(intent: Intent?): IBinder = binder
}