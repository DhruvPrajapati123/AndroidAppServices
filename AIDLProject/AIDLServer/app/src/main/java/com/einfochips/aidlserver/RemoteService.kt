package com.einfochips.aidlserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.random.Random

class RemoteService: Service() {

    private val binder = object : MyAidlInterface.Stub() {
        override fun getRandomNumber(): Int = Random.nextInt(100)
        override fun getStudentDetails(student: Student?): String {
            return "Rollno: ${student?.rollNo} Name: ${student?.name}"
        }

        override fun createStudent(): Student = Student(2, "Maddy", 45, 67, 88)
        override fun getResult(student: Student?): Result = Result(student)
        override fun getResultKt(student: Student?): ResultKt = ResultKt(student!!)
    }

    override fun onBind(intent: Intent?): IBinder = binder
}