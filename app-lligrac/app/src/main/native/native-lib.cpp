

#include <jni.h>


extern "C" {
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getStringAlgorithmName(JNIEnv *env, jobject) {
    return env->NewStringUTF("AES/GCM/NoPadding");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getStringPbkdf2Name(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("PBKDF2WithHmacSHA1");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getStringNonceSize(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("12");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getStringTagSize(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("128");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getKeySize(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("256");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getSaltSize(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("16");
}
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getPbkdf2Iterations(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("32767");
}
}extern "C"
JNIEXPORT jstring JNICALL
Java_co_ekenya_kcbpocr_security_DataEncryption_getAesAlgorithm(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("AES");
}extern "C"
JNIEXPORT jstring JNICALL
Java_io_eclectics_cargildigitalapp_AppCargillDigital_getStringBaseUrl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("http://172.16.15.125:8787/kcb-api-1.0/");
}
//JAccLookupData
extern "C" JNIEXPORT jstring JNICALL
Java_io_eclectics_cargildigitalapp_AppCargillDigital_getStringUATBaseUrl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("http://102.37.14.127:5000/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_io_eclectics_cargildigitalapp_AppCargillDigital_00024Companion_getStringBaseUrl(JNIEnv *env,
                                                                                     jobject thiz) {
    return env->NewStringUTF("http://102.37.14.127:5000/");
}
extern "C"
JNIEXPORT jstring JNICALL
Java_io_eclectics_cargildigitalapp_AppCargillDigital_00024Companion_getStringUATBaseUrl(JNIEnv *env,
                                                                                        jobject thiz) {
    return env->NewStringUTF("http://102.37.14.127:5000/");
}