#include <jni.h>
#include <stdlib.h>
#include <time.h>

JNIEXPORT jint JNICALL
Java_com_example_androidautomotivesimulator_NativeBridge_getSpeedDelta(JNIEnv *env, jobject obj) {
    srand(time(NULL) ^ rand());
    return (rand() % 5) - 2;
}

JNIEXPORT jint JNICALL
Java_com_example_androidautomotivesimulator_NativeBridge_getBatteryDelta(JNIEnv *env, jobject obj) {
    srand(time(NULL) ^ rand());
    return - (rand() % 2);
}
