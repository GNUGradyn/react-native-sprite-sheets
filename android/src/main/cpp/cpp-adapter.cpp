#include <jni.h>
#include "spritesheetsOnLoad.hpp"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
  return margelo::nitro::spritesheets::initialize(vm);
}
