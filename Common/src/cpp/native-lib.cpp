#include <jni.h>
#include <string>

extern "C" jstring Java_com_codeIn_common_di_ApiClient_getBaseUrlFromNative(
        JNIEnv* env,
        jobject /* this */) {
    std::string baseURL = "https://mycashback.mycashtest.com/api/";
    return env->NewStringUTF(baseURL.c_str());
}