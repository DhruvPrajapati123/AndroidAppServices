package com.einfochips.aidlserver;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Result implements Parcelable {

    int rollNo;
    String name;
    float examResult;

    public Result(Student student) {
        this.rollNo = student.getRollNo();
        this.name = student.getName();
        this.examResult = (student.getPhysics() + student.getChemistry() + student.getMaths()) / 3f;
    }

    protected Result(Parcel in) {
        rollNo = in.readInt();
        name = in.readString();
        examResult = in.readFloat();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rollNo);
        dest.writeString(name);
        dest.writeFloat(examResult);
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "rollNo=" + rollNo +
                ", name='" + name + '\'' +
                ", examResult=" + examResult +
                '}';
    }
}
