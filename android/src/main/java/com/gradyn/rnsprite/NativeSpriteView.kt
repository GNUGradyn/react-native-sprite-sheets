package com.gradyn.rnsprite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.widget.AppCompatImageView

class NativeSpriteView(context: Context) : AppCompatImageView(context) {

  /** Full sprite sheet bitmap, set by the view manager when loaded */
  var fullSheet: Bitmap? = null
    set(value) {
      field = value
      invalidate()
      updateMatrix()
    }

  /** Source rectangle in the sprite sheet */
  var srcX: Int = 0
    set(v) {
      field = v
      updateMatrix()
    }
  var srcY: Int = 0
    set(v) {
      field = v
      updateMatrix()
    }
  var srcW: Int = 0
    set(v) {
      field = v
      updateMatrix()
    }
  var srcH: Int = 0
    set(v) {
      field = v
      updateMatrix()
    }

  init {
    // Use matrix mode so we can pan/scale the sprite sheet ourselves
    scaleType = ScaleType.MATRIX
  }

  /**
   * Called when RN/Yoga lays out the view with its final width/height.
   * Trigger a matrix update to recalc the crop transform.
   */
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    updateMatrix()
  }

  /**
   * Compute and set the imageMatrix so only the sub-rect is drawn.
   */
  private fun updateMatrix() {
    val bmp = fullSheet ?: return
    if (width == 0 || height == 0 || srcW <= 0 || srcH <= 0) return

    // scale the src rectangle to match view size
    val scaleX = width / srcW.toFloat()
    val scaleY = height / srcH.toFloat()

    // build the matrix: scale, then translate the sheet so the sub-rect aligns
    val matrix = Matrix().apply {
      setScale(scaleX, scaleY)
      postTranslate(-srcX * scaleX, -srcY * scaleY)
    }

    imageMatrix = matrix
    invalidate()
  }
}

