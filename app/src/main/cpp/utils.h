//
// Created by 58 on 2017/7/6.
//

#include <string>
#include <jni.h>
#include <malloc.h>


std::string jstringTostring(JNIEnv *env, jstring jstr);
jstring charTojstring(JNIEnv *env, const char *pat);

