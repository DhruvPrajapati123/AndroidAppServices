package com.einfochips.aidlserver

import android.os.Parcel
import android.os.Parcelable

class ResultKt: Parcelable {
    private var result = 0.0f;
    private var name: String? = null
    private var rollNo: Int = 0

    constructor(student: Student) {
        rollNo = student.rollNo
        name = student.name!!
        result = (student.physics + student.chemistry + student.maths) / 3f;
    }

    constructor(parcel: Parcel) {
        result = parcel.readFloat()
        name = parcel.readString()
        rollNo = parcel.readInt()
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeFloat(result)
        parcel?.writeString(name)
        parcel?.writeInt(rollNo)
    }

    companion object CREATOR : Parcelable.Creator<ResultKt> {
        override fun createFromParcel(parcel: Parcel): ResultKt {
            return ResultKt(parcel)
        }

        override fun newArray(size: Int): Array<ResultKt?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String = "Result{" +
            "rollNo=" + rollNo +
            ", name='" + name + '\'' +
            ", examResult=" + result +
            '}'
}