package com.einfochips.aidlserver

import android.os.Parcel
import android.os.Parcelable

class Student(var rollNo: Int, var name: String?, var physics: Int, var chemistry: Int, var maths: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {}

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(rollNo)
        dest?.writeString(name)
        dest?.writeInt(physics)
        dest?.writeInt(chemistry)
        dest?.writeInt(maths)
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String = "RollNo: ${rollNo} Name: ${name} Physics: ${physics} Maths: ${maths} Chemistry: ${chemistry}"
}