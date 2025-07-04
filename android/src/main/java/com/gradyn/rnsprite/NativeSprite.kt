package com.margelo.nitro.rnsprite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.collection.LruCache
import com.facebook.react.uimanager.ThemedReactContext
import okhttp3.Request
import java.io.IOException
import java.io.BufferedInputStream

class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {
  override var srcX: Double = 0.0
    set(value) {
      field = value
      view.srcX = value.toInt()
      view.invalidate()
    }
  override var srcY: Double = 0.0
    set(value) {
      field = value
      view.srcY = value.toInt()
      view.invalidate()
    }
  override var srcW: Double = 0.0
    set(value) {
      field = value
      view.srcW = value.toInt()
      view.invalidate()
    }
  override var srcH: Double = 0.0
    set(value) {
      field = value
      view.srcH = value.toInt()
      view.invalidate()
    }

  override var assetUri: String = ""
    set(value) {
      field = value
      SpriteFetcher.load(
        field,
        onReady = { bmp ->
          view.post { // postback onto UI thread
            view.fullSheet = bmp
          }
        },
        onError = { e -> Log.e("NativeSprite", "sprite load failed", e) })
    }

  override val view = NativeSpriteView(context).apply {
    setBackgroundColor(Color.RED) // test
  }

  // TODO: move caching logic to a shared object so items can be evacuated or pre-loaded from the js side
  object SpriteFetcher {
    private val client = com.facebook.react.modules.network.OkHttpClientProvider
      .getOkHttpClient()

    private val cache = object : LruCache<String, Bitmap>(50 * 1024 * 1024) {
      override fun sizeOf(key: String, value: Bitmap) = value.byteCount
    }

    fun load(uri: String, onReady: (Bitmap) -> Unit, onError: (Exception) -> Unit) {
      cache.get(uri)?.let { return onReady(it) }

      Thread {
        try {
          val resp = client.newCall(Request.Builder().url(uri).build()).execute()
          resp.body?.use { body ->
            val input = BufferedInputStream(body.byteStream())
            val bmp = BitmapFactory.decodeStream(input)
              ?: throw IOException("Failed to decode sprite sheet")
            cache.put(uri, bmp)
            onReady(bmp)
          }
        } catch (e: Exception) {
          onError(e)
        }
      }.start()
    }

    fun unload(uri: String) {
      cache.remove(uri)
    }
  }


}
