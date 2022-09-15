#include <jni.h>
#include <string>


#include <jni.h>




extern "C"
JNIEXPORT jstring JNICALL
Java_com_ekenya_rnd_common_Constants_getStringBaseUrl(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("http://102.37.14.127:5000/");
}