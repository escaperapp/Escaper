#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include "tpws_log.h"

/* convert Java String[] -> argv */
static char** jni_to_argv(JNIEnv *env, jobjectArray args, int *argc_out)
{
    int argc = (*env)->GetArrayLength(env, args);

    char **argv = calloc(argc, sizeof(char*));

    for (int i = 0; i < argc; i++) {
        jstring str = (jstring)(*env)->GetObjectArrayElement(env, args, i);

        const char *utf = (*env)->GetStringUTFChars(env, str, NULL);
        argv[i] = strdup(utf);

        (*env)->ReleaseStringUTFChars(env, str, utf);
        (*env)->DeleteLocalRef(env, str);
    }

    *argc_out = argc;
    return argv;
}

/* ================= RUN ================= */

JNIEXPORT jint JNICALL
Java_io_escaper_escaperapp_nativebridge_TpwsBridge_run(
        JNIEnv *env,
        jobject thiz,
        jobjectArray args)
{
    int argc;
    char **argv = jni_to_argv(env, args, &argc);
    LOGI("JNI: before tpws_start");
    //tpws_start(argc, argv);
    LOGI("JNI: after tpws_start");

    /* JNI returns immediately */
    return 0;
}

/* ================= STOP ================= */

JNIEXPORT void JNICALL
Java_io_escaper_escaperapp_nativebridge_TpwsBridge_stop(
        JNIEnv *env,
jobject thiz)
{
}