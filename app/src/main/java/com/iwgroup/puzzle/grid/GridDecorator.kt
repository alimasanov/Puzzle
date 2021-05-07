package com.iwgroup.puzzle.grid

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class GridDecorator(private val context: Context, private val spacingWidthDp: Int) :
    ItemDecoration() {

    private val spacingWidthPx: Int

    /**
     * @param index           a 0 indexed value of the current item
     * @param numberOfColumns
     * @return a 0 indexed Point with the x & y location of the item in the grid
     */
    private fun getItemXY(index: Int, numberOfColumns: Int): Point {
        val x = index % numberOfColumns
        val y = index / numberOfColumns // NB: integer division
        return Point(x, y)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val columns = getTotalSpanCount(parent)
        val rows =
            Math.ceil(parent.childCount / columns.toDouble()).toInt() // NB: NOT integer division
        val spanSize = getItemSpanSize(parent, position)
        if (columns == spanSize) {
            return
        }
        val point: Point = getItemXY(position, columns)
        val firstMargin = spacingWidthPx * (columns - 1) / columns
        val secondMargin = spacingWidthPx - firstMargin
        val middleMargin = spacingWidthPx / 2
        when {
            point.x === 0 -> { // first column
                outRect.left = 0
                outRect.right = firstMargin
            }
            point.x === 1 -> { // second column
                outRect.left = secondMargin
                outRect.right = if (rows > 3) middleMargin else secondMargin
            }
            point.x - columns === -2 -> { // penultimate column
                outRect.left = if (rows > 3) middleMargin else secondMargin
                outRect.right = secondMargin
            }
            point.x - columns === -1 -> { // last column
                outRect.left = firstMargin
                outRect.right = 0
            }
            else -> { // middle columns
                outRect.left = middleMargin
                outRect.right = middleMargin
            }
        }
        when {
            point.y === 0 -> { // first row
                outRect.top = 0
                outRect.bottom = firstMargin
            }
            point.y === 1 -> { // second row
                outRect.top = secondMargin
                outRect.bottom = if (rows > 3) middleMargin else secondMargin
            }
            point.y - rows === -2 -> { // penultimate row
                outRect.top = if (rows > 3) middleMargin else secondMargin
                outRect.bottom = secondMargin
            }
            point.y - rows === -1 -> { // last row
                outRect.top = firstMargin
                outRect.bottom = 0
            }
            else -> { // middle rows
                outRect.top = middleMargin
                outRect.bottom = middleMargin
            }
        }
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) layoutManager.spanCount else 1
    }

    private fun getItemSpanSize(parent: RecyclerView, position: Int): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) layoutManager.spanSizeLookup
            .getSpanSize(
                position
            ) else 1
    }

    init {
        // Convert DP to pixels
        spacingWidthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, spacingWidthDp.toFloat(),
            context.getResources().getDisplayMetrics()
        ).toInt()
    }
}