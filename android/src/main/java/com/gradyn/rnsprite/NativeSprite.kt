package com.yourapp.spritesheets

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.facebook.react.uimanager.ThemedReactContext
import com.margelo.nitro.HybridNativeSpriteSpec

class NativeSprite(context: ThemedReactContext) : HybridNativeSpriteSpec(context) {
  private var sheetBmp: Bitmap? = null

  override val view = object : AppCompatImageView(context) {
    override fun onDraw(canvas: Canvas) {
      sheetBmp?.let { bmp ->
        val src = Rect(
          srcX.toInt(),
          srcY.toInt(),
          (srcX + srcW).toInt(),
          (srcY + srcH).toInt()
        )
        val dst = Rect(0, 0, width, height)
        canvas.drawBitmap(bmp, src, dst, null)
      }
    }
  }

  override var assetID: Double = 0.0
    set(value) {
      field = value
      val resId = value.toInt()
      sheetBmp = SheetCache.getOrLoad(view.context, resId)
      view.invalidate()
    }

  override var srcX: Double = 0.0
  override var srcY: Double = 0.0
  override var srcW: Double = 0.0
  override var srcH: Double = 0.0  

  private object SheetCache {
    private val cache = mutableMapOf<Int, Bitmap>()

    fun getOrLoad(ctx: Context, resId: Int): Bitmap {
      return cache.getOrPut(resId) {
        BitmapFactory.decodeResource(ctx.resources, resId)
      }
    }
  }
}
