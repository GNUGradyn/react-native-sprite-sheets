#include <jni.h>

#include "rnspriteOnLoad.hpp"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
  return margelo::nitro::rnsprite::initialize(vm);
}
