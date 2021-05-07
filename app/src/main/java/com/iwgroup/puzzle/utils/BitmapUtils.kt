package com.iwgroup.puzzle.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.IntRange
import androidx.core.content.res.ResourcesCompat
import java.io.FileNotFoundException
import kotlin.math.max


fun Bitmap.setShadow(
    context: Context,
    angleRadius: Int = 20
): Bitmap? {
    val newBitmap: Bitmap?
    var newShadowBitmap: Bitmap? = null
    try {
        val w = this.width
        val h = this.height
        var config: Bitmap.Config? = this.config
        if (config == null) {
            config = Bitmap.Config.ARGB_8888
        }
        newBitmap = Bitmap.createBitmap(w, h, config)
        val newCanvas = Canvas(newBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        val frame = Rect(
            (w * 1.05).toInt(), (w * 1.05).toInt(),
            (w * 1.95).toInt(), (h * 1.95).toInt()
        )
        val frameF = RectF(frame)
        newCanvas.drawRect(frameF, paint)
        newCanvas.drawBitmap(this, 0f, 0f, paint)

        /*
		 * Create shadow like outer frame
		 */

        // create BLACK bitmap with same size of the image
        val bitmapFullGray = Bitmap.createBitmap(w, h, config)
        val canvasFullGray = Canvas(bitmapFullGray)

        canvasFullGray.drawColor(-0x7f7f80)

        // create bigger bitmap with shadow frame
        val shadowThick = 2
        val shadowRadius = 6
        newShadowBitmap = Bitmap.createBitmap(
            w + shadowThick
                    + shadowRadius, h + shadowThick + shadowRadius, config
        )
        val newShadowCanvas = Canvas(newShadowBitmap)
        newShadowCanvas.drawColor(Color.WHITE)

        // generate shadow
        val paintShadow = Paint()
        paintShadow.setShadowLayer(
            shadowRadius.toFloat(), shadowThick.toFloat(), shadowThick.toFloat(),
            -0x1000000
        )
        newShadowCanvas.drawBitmap(bitmapFullGray, 0f, 0f, paintShadow)

        // Place the image
        paintShadow.clearShadowLayer()
        newShadowCanvas.drawBitmap(newBitmap, 0f, 0f, paintShadow)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return newShadowBitmap
}

fun Bitmap.makeTransparent(@IntRange(from = 0, to = 100) value: Int): Bitmap {
    val alpha = 255 * value / 100
    val _width = width
    val _height = height
    val transBitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(transBitmap)
    val paint = Paint()
    paint.alpha = alpha
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return transBitmap
}

fun Bitmap.makeRoundedCorners(
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    rx: Float,
    ry: Float
): Bitmap {
    val bitmap = this
    val transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(transBitmap)
    val rect = Rect(0, 0, width, height)
    val rectF = RectF(rect)
    val angleRadius =
        max(max(topLeftRadius, topRightRadius), max(bottomLeftRadius, bottomRightRadius)).toPx()
            .toFloat()
    val path = getPathOfRoundedRectF(
        rectF,
        bottomLeftRadius = bottomLeftRadius.toPx().toFloat(),
        bottomRightRadius = bottomRightRadius.toPx().toFloat(),
        topLeftRadius = topLeftRadius.toPx().toFloat(),
        topRightRadius = topRightRadius.toPx().toFloat()
    )
    path.addRoundRect(rectF, rx, ry, Path.Direction.CW)
    canvas.clipPath(path)
    canvas.drawBitmap(bitmap, rect, rect, Paint(Paint.ANTI_ALIAS_FLAG))
    return transBitmap
}

fun Context.getBitmap(id: Int, _width: Int, _height: Int): Bitmap? {
    ResourcesCompat.getDrawable(resources, id, theme)?.let {
        if (it is BitmapDrawable) return it.bitmap
        val bitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        return bitmap
    }
    return null
}

fun getPathOfRoundedRectF(
    rect: RectF,
    topLeftRadius: Float = 0f,
    topRightRadius: Float = 0f,
    bottomRightRadius: Float = 0f,
    bottomLeftRadius: Float = 0f
): Path {
    val tlRadius = topLeftRadius.coerceAtLeast(0f)
    val trRadius = topRightRadius.coerceAtLeast(0f)
    val brRadius = bottomRightRadius.coerceAtLeast(0f)
    val blRadius = bottomLeftRadius.coerceAtLeast(0f)

    with(Path()) {
        moveTo(rect.left + tlRadius, rect.top)

        //setup top border
        lineTo(rect.right - trRadius, rect.top)

        //setup top-right corner
        arcTo(
            RectF(
                rect.right - trRadius * 2f,
                rect.top,
                rect.right,
                rect.top + trRadius * 2f
            ), -90f, 90f
        )

        //setup right border
        lineTo(rect.right, rect.bottom - trRadius)

        //setup bottom-right corner
        arcTo(
            RectF(
                rect.right - brRadius * 2f,
                rect.bottom - brRadius * 2f,
                rect.right,
                rect.bottom
            ), 0f, 90f
        )

        //setup bottom border
        lineTo(rect.left + blRadius, rect.bottom)

        //setup bottom-left corner
        arcTo(
            RectF(
                rect.left,
                rect.bottom - blRadius * 2f,
                rect.left + blRadius * 2f,
                rect.bottom
            ), 90f, 90f
        )

        //setup left border
        lineTo(rect.left, rect.top + tlRadius)

        //setup top-left corner
        arcTo(
            RectF(
                rect.left,
                rect.top,
                rect.left + tlRadius * 2f,
                rect.top + tlRadius * 2f
            ),
            180f,
            90f
        )

        close()

        return this
    }
}