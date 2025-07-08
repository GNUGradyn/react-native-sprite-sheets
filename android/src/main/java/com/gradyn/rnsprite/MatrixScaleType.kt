package com.gradyn.rnsprite

import android.graphics.Matrix
import android.graphics.Rect
import com.facebook.drawee.drawable.ScalingUtils

// This is here because RN uses fresco 1.x so there is no matrix scale type

class MatrixScaleType(
  private val matrix: Matrix
) : ScalingUtils.ScaleType {

  override fun getTransform(
    outTransform: Matrix,
    parentBounds: Rect,
    childWidth: Int,
    childHeight: Int,
    focusX: Float,
    focusY: Float
  ): Matrix {
    outTransform.set(matrix)
    return outTransform
  }
}
