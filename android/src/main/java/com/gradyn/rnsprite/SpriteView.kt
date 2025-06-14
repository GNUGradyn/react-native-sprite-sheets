package com.gradyn.rnsprite

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SpriteView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  var pack: String = ""
    set(v) { field = v; invalidate(); requestLayout() }
  var srcX: Int = 0
    set(v) { field = v; invalidate() }
  var srcY: Int = 0
    set(v) { field = v; invalidate() }
  var srcW: Int = 0
    set(v) { field = v; requestLayout(); invalidate() }
  var srcH: Int = 0
    set(v) { field = v; requestLayout(); invalidate() }

  var desiredW: Int? = null
    set(v) { field = v; requestLayout() }
  var desiredH: Int? = null
    set(v) { field = v; requestLayout() }

  override fun onMeasure(wm: Int, hm: Int) {
    val w = desiredW ?: srcW
    val h = desiredH ?: srcH
    if (w > 0 && h > 0) setMeasuredDimension(w, h)
    else super.onMeasure(wm, hm)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val bmp = SpriteModule.bitmapCache[pack] ?: return
    val src = Rect(srcX, srcY, srcX + srcW, srcY + srcH)
    val dst = Rect(0, 0, width, height)
    canvas.drawBitmap(bmp, src, dst, null)
  }
}