package com.gradyn.rnsprite

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class SpriteViewManager : SimpleViewManager<SpriteView>() {

  override fun getName() = "Sprite"

  override fun createViewInstance(ctx: ThemedReactContext) = SpriteView(ctx)

  @ReactProp(name = "pack")
  fun setPack(v: SpriteView, pack: String) {
    v.pack = pack
  }

  @ReactProp(name = "srcX")
  fun setSrcX(v: SpriteView, x: Int) { v.srcX = x }

  @ReactProp(name = "srcY")
  fun setSrcY(v: SpriteView, y: Int) { v.srcY = y }

  @ReactProp(name = "srcW")
  fun setSrcW(v: SpriteView, w: Int) { v.srcW = w }

  @ReactProp(name = "srcH")
  fun setSrcH(v: SpriteView, h: Int) { v.srcH = h }

  @ReactProp(name = "width", defaultInt = -1)
  fun setDesiredWidth(v: SpriteView, w: Int) {
    v.desiredW = if (w >= 0) w else null
  }

  @ReactProp(name = "height", defaultInt = -1)
  fun setDesiredHeight(v: SpriteView, h: Int) {
    v.desiredH = if (h >= 0) h else null
  }
}