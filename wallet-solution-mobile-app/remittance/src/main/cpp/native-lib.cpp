//
// Created by LenoxBrown on 25/06/2021.
//
#include <jni.h>
#include <string>
#include <jni.h>

extern "C" jstring
Java_com_ekenya_lamparam_Keys_testURL(JNIEnv *env, jobject object) {
    std::string test_url = "https://testgateway.ekenya.co.ke:8443/";
    return env->NewStringUTF(test_url.c_str());
}
