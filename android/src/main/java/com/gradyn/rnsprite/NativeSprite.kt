package com.margelo.nitro.rnsprite

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.facebook.react.uimanager.ThemedReactContext

class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec() {

  override var srcX: Double = 0.0
  override var srcY: Double = 0.0
  override var srcW: Double = 0.0
  override var srcH: Double = 0.0  
  override var width: Double = 0.0  
  override var height: Double = 0.0  
  override var assetID: Int = 0
}
