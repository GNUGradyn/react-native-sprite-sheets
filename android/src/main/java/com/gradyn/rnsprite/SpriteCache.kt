package com.margelo.nitro.rnsprite

import android.util.Log
import androidx.core.net.toUri
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.margelo.nitro.core.Promise
import java.net.URI

object SpriteCache : HybridSpriteCacheSpec() {
  private data class Entry(
    val tokens: MutableSet<String> = hashSetOf(),
    var img: CloseableReference<CloseableImage>? = null,
    var width: Int = -1,
    var height: Int = -1
  )

  private val map = HashMap<String, Entry>()
  private val lock = Any()

  override fun pin(uri: String, token: String, height: Double, width: Double): Promise<Unit> {
    val promise = Promise<Unit>()

    var noop = false
    synchronized(lock) {
      val entry = map.getOrPut(uri) { Entry() }
      // this should never happen but this is a no-op protective measure to stop memory leaks in case it does
      if (!entry.tokens.add(token)) {
        Log.w("rnsprite", "BUG: same token registered twice. This is a BUG in react-native-sprite-sheets but should be inconsequential")
        noop = true
      }

      // this should never happen because the js side wont call this method if there are already tokens but this is a no-op protective measure to stop the image from being re-cached for no reason if it does
      if (entry.img != null) {
        Log.w("rnsprite", "BUG: image already cached. This is a BUG in react-native-sprite-sheets but should be inconsequential")
        noop = true
      }
    }

    if (noop) {
      promise.resolve(Unit)
      return promise
    }

    val request = ImageRequestBuilder
      .newBuilderWithSource(uri.toUri())
      .build()
    val dataSource = Fresco.getImagePipeline().fetchDecodedImage(request, null)

    dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
      override fun onNewResultImpl(src: DataSource<CloseableReference<CloseableImage>>) {
        try {
          if (src.isFinished) {
            synchronized(lock) {
              map[uri]?.img = CloseableReference.cloneOrNull(src.result)
              map[uri]?.height = height.toInt()
              map[uri]?.width = width.toInt()
              promise.resolve(Unit)
            }
          }
        } finally { src.close() }
      }
      override fun onFailureImpl(src: DataSource<CloseableReference<CloseableImage>>) {
        synchronized(lock) {
          map[uri]?.let { e ->
            e.tokens.remove(token)
            if (e.tokens.isEmpty() && e.img == null) map.remove(uri)
          }
        }
        promise.reject(Error("Failed to load spritesheet"))
      }
    }, UiThreadImmediateExecutorService.getInstance())

    return promise
  }

  override fun release(uri: String, token: String) {
    synchronized(lock) {
      // This should never happen, but this is here to prevent a crash as a protective measure
      val e = map[uri]
      if (e == null) {
        Log.w("rnsprite", "Tried to evict a sprite sheet that is not cached. This is a BUG in react-native-sprite-sheets but should be inconsequential")
        return
      }
      e.tokens.remove(token)
      if (e.tokens.isEmpty()) {
        CloseableReference.closeSafely(e.img)
        map.remove(uri)
      }
    }
  }

  fun fetch(uri: URI): CachedBitmap {
    synchronized(lock) {
      val e = map[uri.toString()] ?: throw IllegalStateException("Not cached: $uri")
      val pinned = e.img ?: throw IllegalStateException("Not cached: $uri")
      var closableRef = CloseableReference.cloneOrNull(pinned)
        ?: throw IllegalStateException("Clone failed for $uri")
      return CachedBitmap(closableRef, e.width, e.height)
    }
  }
}
