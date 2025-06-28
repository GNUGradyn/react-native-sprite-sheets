package com.margelo.nitro.rnsprite

import com.facebook.react.uimanager.ThemedReactContext
import android.view.View


class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {
  override val view: View = View(context)

  override var srcX: Double = 0.0
  override var srcY: Double = 0.0
  override var srcW: Double = 0.0
  override var srcH: Double = 0.0  
  override var width: Double? = 0.0  
  override var height: Double? = 0.0  
  override var assetID: Double = 0.0
}
