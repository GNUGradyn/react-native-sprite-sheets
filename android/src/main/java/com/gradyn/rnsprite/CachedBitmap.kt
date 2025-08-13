package com.margelo.nitro.rnsprite

import com.facebook.common.references.CloseableReference
import com.facebook.imagepipeline.image.CloseableImage

public data class CachedBitmap(
  val img: CloseableReference<CloseableImage>?,
  val width: Int,
  val height: Int
)
