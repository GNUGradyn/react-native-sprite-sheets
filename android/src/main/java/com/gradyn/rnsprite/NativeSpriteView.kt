package com.margelo.nitro.rnsprite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View

class NativeSpriteView(context: Context) : View(context) {
  companion object {
    private const val TAG = "NativeSpriteView"
  }

  private val paint = Paint(Paint.FILTER_BITMAP_FLAG)

  /** the full sheet, set from JS-side module */
  var fullSheet: Bitmap? = null
    set(value) {
      field = value
      Log.d(TAG, "sheet set = ${value?.width}×${value?.height}")
      invalidate()
    }

  /** source rectangle within that sheet */
  var srcX = 0; var srcY = 0
  var srcW = 0; var srcH = 0

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val bmp = fullSheet
    if (bmp == null) {
      // nothing yet
      return
    }
    if (srcW <= 0 || srcH <= 0 || width == 0 || height == 0) {
      Log.d(TAG, "draw aborted: view=(${width}×${height}), src=(${srcW}×${srcH})")
      return
    }

    // only ever draw the tiny sub‐rect
    val src = Rect(srcX, srcY, srcX + srcW, srcY + srcH)
    val dst = Rect(0, 0, width, height)
    canvas.drawBitmap(bmp, src, dst, paint)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    Log.d(TAG, "view laid out = ${w}×${h}")
    // trigger a redraw with the new size
    invalidate()
  }
}
