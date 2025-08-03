// MyAidlInterface.aidl
package com.einfochips.aidlserver;

import com.einfochips.aidlserver.Student;
import com.einfochips.aidlserver.Result;
import com.einfochips.aidlserver.ResultKt;
// Declare any non-default types here with import statements

interface MyAidlInterface {
    int getRandomNumber();
    String getStudentDetails(in Student student);
    Student createStudent();
    Result getResult(in Student student);
    ResultKt getResultKt(in Student student);
}