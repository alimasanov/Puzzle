package com.iwgroup.puzzle

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.iwgroup.puzzle.utils.*


class view(context: Context, attrs: AttributeSet) : View(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        makeBitmap()?.let { canvas?.drawBitmap(it, 50f, 50f, Paint(Paint.ANTI_ALIAS_FLAG)) }
    }

    fun makeBitmap(): Bitmap? {
        val angleRadius = 20
        val shadowSize = 5.toPx()
        var image = context.getBitmap(
            R.drawable.ic_launcher_background,
            100.toPx(),
            80.toPx()
        )
        image = image?.makeRoundedCorners(
            bottomRightRadius = angleRadius,
            rx = angleRadius.toFloat(),
            ry = angleRadius.toFloat()
        )
        image = image?.setShadow(
            context = context,
            angleRadius = angleRadius
        )
        image = image?.makeRoundedCorners(
            bottomRightRadius = angleRadius,
            rx = angleRadius.toFloat() * 2.3.toFloat(),
            ry = angleRadius.toFloat() * 2.3.toFloat()
        )
        return image
    }
}