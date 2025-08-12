package com.gradyn.rnsprite

import com.facebook.common.references.CloseableReference
import com.facebook.imagepipeline.image.CloseableImage
import com.margelo.nitro.core.Promise
import com.margelo.nitro.rnsprite.HybridSpriteCacheSpec
import java.util.concurrent.ConcurrentHashMap

class SpriteCache() : HybridSpriteCacheSpec() {
  private data class Entry(
    val tokens: MutableSet<String> = hashSetOf(),
    var img: CloseableReference<CloseableImage>? = null
  )

  private val map = ConcurrentHashMap<String, Entry>()

  override fun pin(uri: String, token: String): Promise<Unit> {
    TODO("Not yet implemented")
  }

  override fun release(uri: String, token: String) {
    TODO("Not yet implemented")
  }
}
