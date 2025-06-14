package com.gradyn.rnsprite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.facebook.react.bridge.*
import org.json.JSONObject

class SpriteModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName() = "SpriteModule"

  companion object {
    // bitmap cache + ref counts
    private val bitmapCache = mutableMapOf<String, Bitmap>()
    private val sheetRef   = mutableMapOf<String, Int>()

    @JvmField
    val bitmapCache = mutableMapOf<String, Bitmap>()
  }

  @ReactMethod
  fun loadMetadata(pack: String, promise: Promise) {
    try {
      val jsonText = reactApplicationContext.assets
        .open("rnsprite/$pack.png.json")
        .bufferedReader()
        .use { it.readText() }
      val jo = JSONObject(jsonText)
      val out = Arguments.createMap()
      // Build a js friendly map. This is processed on the native side for thread control, consistency with bitmap loading, and so we only need a single round-trip over the bridge
      jo.keys().asSequence().forEach { key ->
        jo.optJSONObject(key)?.let {
          val m = Arguments.createMap().apply {
            putInt("x", it.getInt("x"))
            putInt("y", it.getInt("y"))
            putInt("width", it.getInt("width"))
            putInt("height", it.getInt("height"))
          }
          out.putMap(key, m)
        }
      }
      promise.resolve(out)
    } catch (e: Exception) {
      promise.reject("LOAD_META_ERROR", e)
    }
  }

  /** bump & decode if first time */
  @ReactMethod
  fun preloadSheet(pack: String) {
    val count = sheetRef.getOrDefault(pack, 0)
    if (count > 0) {
      sheetRef[pack] = count + 1
    } else {
      // first time: decode into cache
      val bmp = BitmapFactory.decodeStream(
        reactApplicationContext.assets.open("rnsprite/$pack.png")
      )
      bitmapCache[pack] = bmp
      sheetRef[pack] = 1
    }
  }

  /** unmount hook calls this; recycles when no more refs */
  @ReactMethod
  fun releaseSheet(pack: String) {
    val count = sheetRef.getOrDefault(pack, 1) - 1
    if (count <= 0) {
      sheetRef.remove(pack)
      bitmapCache.remove(pack)?.recycle()
    } else {
      sheetRef[pack] = count
    }
  }
}