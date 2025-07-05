package com.margelo.nitro.rnsprite

import android.graphics.Color
import com.facebook.react.uimanager.ThemedReactContext
import android.view.View
import android.widget.TextView


class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {
  override val view = TextView(context).apply {
    text = "native side test"
    setBackgroundColor(Color.RED)
  }

  override var srcX: Double = 0.0
  override var srcY: Double = 0.0
  override var srcW: Double = 0.0
  override var srcH: Double = 0.0
  override var width: Double? = 200.0
  override var height: Double? = 200.0
  override var assetID: Double = 0.0
}
