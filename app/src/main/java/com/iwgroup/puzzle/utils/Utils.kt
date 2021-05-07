package com.iwgroup.puzzle.utils

import android.content.res.Resources

fun Int.toPx(): Int = (toFloat() * Resources.getSystem().displayMetrics.density).toInt()
fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.toDp() : Int = (toFloat() / Resources.getSystem().displayMetrics.density).toInt()
fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)