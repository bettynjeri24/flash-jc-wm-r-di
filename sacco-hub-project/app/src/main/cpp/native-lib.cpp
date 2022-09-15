#include <jni.h>
#include <string.h>
extern "C" {
JNIEXPORT jstring JNICALL
Java_com_ekenya_rnd_baseapp_BaseApp_getBaseURL(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(" https://test-portal.ekenya.co.ke/tijara-api/api/");
    //https://test-portal.ekenya.co.ke/saccohub/api/
}
}