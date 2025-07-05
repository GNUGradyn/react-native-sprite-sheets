package com.margelo.nitro.rnsprite

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.facebook.react.uimanager.ThemedReactContext

class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {
  override var srcX: Double = 0.0
  override var srcY: Double = 0.0
  override var srcW: Double = 0.0
  override var srcH: Double = 0.0
  override var width: Double? = 0.0
  override var height: Double? = 0.0

  // Note to self for tomorrow: this is always 0 even when we send 1 over the bridge
  override var assetID: Double = 0.0

  override val view = ImageView(context).apply {
    setImageBitmap(BitmapFactory.decodeResource(context.resources, assetID.toInt()))
  }
}
