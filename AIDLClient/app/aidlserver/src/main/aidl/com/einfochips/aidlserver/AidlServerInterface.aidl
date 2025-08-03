// AidlServerInterface.aidl
package com.einfochips.aidlserver;

import com.einfochips.aidlserver.Student;
// Declare any non-default types here with import statements

interface AidlServerInterface {
    int add(int a, int b);
    String getStudentInfo(in Student student);
}