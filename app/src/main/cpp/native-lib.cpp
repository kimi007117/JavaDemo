#include <jni.h>
#include <string>
#include "MD5.h"
#include "XXTEA.h"
#include "utils.h"
#include <android/log.h>

#define TAG "native-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

std::string get_aid(std::string aid) ;

std::string get_rid(std::string rid) ;

extern "C" {
JNIEXPORT jstring JNICALL Java_com_noe_rxjava_JNIActivity_md5(JNIEnv *, jobject, jstring);
JNIEXPORT jstring JNICALL Java_com_noe_rxjava_JNIActivity_encrypt(JNIEnv *, jobject, jstring);
JNIEXPORT jstring JNICALL Java_com_noe_rxjava_JNIActivity_decrypt(JNIEnv *, jobject, jstring);
JNIEXPORT jstring JNICALL Java_com_noe_rxjava_JNIActivity_sub(JNIEnv *, jobject, jstring);
}

JNIEXPORT jstring JNICALL
Java_com_noe_rxjava_JNIActivity_sub(JNIEnv *env, jobject instance, jstring data_) {
    std::string str = jstringTostring(env, data_);
    std::string sub = str.substr(0, 8);
    std::string aid = get_aid(sub);
    std::string rid = get_rid(sub);
    std::string key = aid + rid;
    std::string md = md5(key);
    std::string s = md.substr(0,8);

//    // TODO
//    LOGI("str=%s", s.c_str());
//    LOGI("sub=%s", sub.c_str());
//    char ap[8];
//    const char *data = env->GetStringUTFChars(data_, 0);
//    strlcpy(ap, data, sizeof(data));
//    std::string hhh = s + sub;
//    LOGI("data=%d", strlen(data));
//
//    jstring st = charTojstring(env, ap);
//    std::string d = jstringTostring(env, st);
//    env->ReleaseStringUTFChars(data_, data);
    return env->NewStringUTF(s.c_str());
}

std::string get_aid(std::string aid) {
    const char *data = aid.data();
    std::string result;
    for (int i = 0; i < strlen(data); ++i) {
        if (i % 2 == 0) {
            result += data[i];
        }
    }
    return result;
}

std::string get_rid(std::string rid) {
    const char *data = rid.data();
    std::string result;
    for (int i = 0; i < strlen(data); ++i) {
        if (i % 2 == 1) {
            result += data[i];
        }
    }
    return result;
}

JNIEXPORT jstring JNICALL
Java_com_noe_rxjava_JNIActivity_md5(JNIEnv *env, jobject instance, jstring data_) {
    std::string c = jstringTostring(env, data_);
    std::string result = md5(c);
    return env->NewStringUTF(result.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_noe_rxjava_JNIActivity_encrypt(JNIEnv *env, jobject instance, jstring data_) {
    std::string srcBuffer = jstringTostring(env, data_);
    std::string key = "abcderff";//秘钥
    std::string result = xxtea_encrypt(srcBuffer, key);
    return env->NewStringUTF(result.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_noe_rxjava_JNIActivity_decrypt(JNIEnv *env, jobject instance, jstring data_) {
    std::string srcBuffer = jstringTostring(env, data_);
    std::string key = "abcderff";//秘钥
    std::string result = xxtea_decrypt(srcBuffer, key);
    return env->NewStringUTF(result.c_str());
}

