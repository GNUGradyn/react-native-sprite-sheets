@file:Suppress("DEPRECATION") // GenericDraweeView is what react native itself is using and
// Vito doesn't seem to be documented yet so we're using it anyway

package com.margelo.nitro.rnsprite

import android.graphics.Color
import android.widget.FrameLayout
import androidx.core.net.toUri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.react.uimanager.ThemedReactContext

class NativeSprite(val context: ThemedReactContext) : HybridNativeSpriteSpec() {
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

  private val draweeView = SimpleDraweeView(context)

  override val view = FrameLayout(context).apply {
    clipChildren = true
    setBackgroundColor(Color.RED)
    addView(draweeView)
  }

  private fun render() {
    if (srcW < 0 || srcH < 0 || srcX < 0 || srcY < 0 || assetUri.isBlank()) return

    view.layoutParams = FrameLayout.LayoutParams(srcW.toInt(), srcH.toInt())

    draweeView.translationX = -srcX.toFloat()
    draweeView.translationY = -srcY.toFloat()

    draweeView.layoutParams = FrameLayout.LayoutParams(
      10980, 10980
    )

    val request = ImageRequestBuilder
      .newBuilderWithSource(assetUri.toUri())
      .build()

    val controller = Fresco.newDraweeControllerBuilder()
      .setImageRequest(request)
      .setOldController(draweeView.controller)
      .build()

    draweeView.controller = controller
  }
}
