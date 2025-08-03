package com.einfochips.aidlserver

import android.os.Parcel
import android.os.Parcelable

data class Student(var rollNo: Int, var firstName: String?, var lastName: String?, var physics: Int, var chemistry: Int, var maths: Int): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rollNo)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(physics)
        parcel.writeInt(chemistry)
        parcel.writeInt(maths)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}