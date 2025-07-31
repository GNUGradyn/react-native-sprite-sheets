@file:Suppress("DEPRECATION") // GenericDraweeView is what react native itself is using and
// Vito doesn't seem to be documented yet so we're using it anyway

package com.margelo.nitro.rnsprite

import android.graphics.Color
import android.graphics.Outline
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
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

  override val view = object : FrameLayout(context) {
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
      super.onLayout(changed, left, top, right, bottom)
      Log.d("NativeSprite", "onLayout: ${right - left}x${bottom - top}")
    }
  }.apply {
    // We cannot use clipChildren because Fresco draws outside the view system using native APIs.
    // React Natives fix for this is also in "true native land" via java/C++ hybrid so we cannot emulate that.
    // clipToOutline is also hardware accelerated so performance should be very close to rn
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
      override fun getOutline(view: View, outline: Outline) {
        outline.setRect(0, 0, srcW.toInt(), srcH.toInt())
      }
    }
    setBackgroundColor(Color.RED)
    addView(draweeView)
  }


  private fun render() {
    // Wait for everything to come over the bridge
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
