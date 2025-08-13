@file:Suppress("DEPRECATION") // GenericDraweeView is what react native itself is using and
// Vito doesn't seem to be documented yet so we're using it anyway

package com.margelo.nitro.rnsprite

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.facebook.common.internal.Supplier
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.react.uimanager.ThemedReactContext
import java.net.URI


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
    // We cannot use clipChildren because Fresco draws outside the view system using native APIs.
    // React Natives fix for this is also in "true native land" via java/C++ hybrid so we cannot emulate that.
    // clipToOutline is also hardware accelerated so performance should be very close to rn
    clipToOutline = true
    outlineProvider = BoundsOutlineProvider()
    addView(draweeView)
    addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
      v.invalidateOutline()
    }
  }

  private fun setImageFromPinned(assetUri: String) {
    val ref = SpriteCache.fetch(URI(assetUri))

    draweeView.layoutParams = FrameLayout.LayoutParams(
      ref.width, ref.height
    )

    val supplier: Supplier<DataSource<CloseableReference<CloseableImage>>> =
      Supplier { DataSources.immediateDataSource(ref.img) }

    val controller = Fresco.newDraweeControllerBuilder()
      .setOldController(draweeView.controller)
      .setDataSourceSupplier(supplier)
      .build()

    draweeView.controller = controller
  }

  private fun render() {
    // Wait for everything to come over the bridge
    if (srcW < 0 || srcH < 0 || srcX < 0 || srcY < 0 || assetUri.isBlank()) return

    draweeView.translationX = -srcX.toFloat()
    draweeView.translationY = -srcY.toFloat()

    try {
      setImageFromPinned(assetUri)
    } catch (e: IllegalStateException) {
      // Fail-hard in dev; fail-soft in release
      if (BuildConfig.DEBUG) throw e
      draweeView.controller = null // refuse to render if not pinned
    }
  }

  inner class BoundsOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
      outline.setRect(0, 0, view.width, view.height)
    }
  }
}
