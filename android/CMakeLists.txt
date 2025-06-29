cmake_minimum_required(VERSION 3.9.0)
project(rnsprite)

set(PACKAGE_NAME rnsprite)
set(CMAKE_CXX_STANDARD 20)

# 1) Define your shared library with both your adapter and the generated stub
add_library(${PACKAGE_NAME} SHARED
  src/main/cpp/cpp-adapter.cpp
  src/main/cpp/rnspriteOnLoad.cpp
)

# 2) Include the Nitrogen-generated autolinking script
#    (go up one level from android/ into the repo root, then into nitrogen/generated/android)
include("${CMAKE_SOURCE_DIR}/../nitrogen/generated/android/rnsprite+autolinking.cmake")

# 3) Your own include paths
include_directories(
  src/main/cpp
  ${CMAKE_SOURCE_DIR}/../node_modules/react-native/ReactCommon/jsi
)

# 4) Link against Android log, the Android framework, and the C++ STL
find_library(LOG_LIB log)
target_link_libraries(
  ${PACKAGE_NAME}
  ${LOG_LIB}
  android
  c++_static      # or c++_shared
)
