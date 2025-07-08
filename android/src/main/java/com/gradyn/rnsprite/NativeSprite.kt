@file:Suppress("DEPRECATION") // GenericDraweeView is what react native itself is using and
// Vito doesn't seem to be documented yet so we're using it anyway

package com.margelo.nitro.rnsprite

import android.graphics.Matrix
import android.view.ViewGroup
import android.widget.FrameLayout
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.react.uimanager.ThemedReactContext
import com.gradyn.rnsprite.MatrixScaleType

class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {
  override var srcX: Double = -1.0
    set(value) {
      field = value
      render()
    }
  override var srcY: Double = -1.0
    set(value) {
      field = value
      render()
    }
  override var srcW: Double = -1.0
    set(value) {
      field = value
      render()
    }
  override var srcH: Double = -1.0
    set(value) {
      field = value
      render()
    }

  override var assetUri: String = ""
    set(value) {
      field = value
      render()
    }

  override val view = SimpleDraweeView(context)

  fun render() {
    // make sure all the data we need has been sent over the bridge
    if (srcW < 0 || srcY < 0 || srcH < 0 || srcX < 0 || assetUri.isBlank()) return

    // crop
    view.layoutParams = ViewGroup.LayoutParams(srcW.toInt(), srcH.toInt())

    // shift
    val matrix = Matrix().apply {
      setScale(1f, 1f) // dont scale
      postTranslate(-srcX.toFloat(), -srcY.toFloat())
    }
    val hierarchy =
      GenericDraweeHierarchyBuilder(view.resources)
        .setActualImageScaleType(MatrixScaleType(matrix))
        .build()
    view.hierarchy = hierarchy

    // apply
    view.setImageURI(assetUri)
    view.requestLayout()
    view.invalidate()
  }
}
