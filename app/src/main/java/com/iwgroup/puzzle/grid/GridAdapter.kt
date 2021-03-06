package com.iwgroup.puzzle.grid

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iwgroup.puzzle.*
import com.iwgroup.puzzle.utils.*

class GridAdapter(
    private val context: Context,
    var listItem: MutableList<Pair<Bitmap, Boolean>>?,
    private val spanCount: Int
) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private fun changeItem(position: Int) {
        listItem?.let { it.set(position, it[position].first to !it[position].second) }
        notifyItemChanged(position)
    }

    fun activateItem(position: Int) {
        listItem?.let {
            if (!it[position].second) {
                it[position] = it[position].first to true
                notifyItemChanged(position)
            }
        }
    }

    fun reset() {
        listItem?.let {
            it.forEachIndexed { index, _ ->
                it[index] = it[index].first to false
                notifyItemChanged(index)
            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listItem?.let {
            val item = it[position]
            val angleRadius = 20
            var image: Bitmap? = it[position].first
            image = when (position) {
                0 -> image?.makeTopLeftImage(context, angleRadius, item.second)
                spanCount - 1 -> image?.makeTopRightImage(context, angleRadius, item.second)
                itemCount - 1 -> image?.makeBottomRightImage(context, angleRadius, item.second)
                itemCount - spanCount -> image?.makeBottomLeftImage(
                    context,
                    angleRadius,
                    item.second
                )
                else -> image?.makeImage(context, item.second)
            }
            holder.ivImage.setImageBitmap(image)
            holder.tvNumber.text = position.toString()

            holder.view.setOnClickListener {
                changeItem(position)
            }
        }
    }

    private fun Bitmap.makeTopLeftImage(
        context: Context,
        angleRadius: Int,
        isActive: Boolean
    ): Bitmap? {
        var image: Bitmap? = this

        fun Bitmap.round() = this.makeRoundedCorners(
            topLeftRadius = angleRadius,
            rx = angleRadius.toFloat(),
            ry = angleRadius.toFloat()
        )

        image = image?.round()
        image = if (isActive) image?.setShadow(
            context = context,
            angleRadius = angleRadius
        )?.round()
        else image?.makeTransparent(40)?.addPadding(4, 4, 4, 4)

        return image
    }

    private fun Bitmap.makeTopRightImage(
        context: Context,
        angleRadius: Int,
        isActive: Boolean
    ): Bitmap? {
        var image: Bitmap? = this

        image = image?.makeRoundedCorners(
            topRightRadius = angleRadius,
            rx = angleRadius.toFloat(),
            ry = angleRadius.toFloat()
        )
        image = if (isActive) image?.setShadow(
            context = context,
            angleRadius = angleRadius
        )?.makeRoundedCorners(
            topRightRadius = (angleRadius * 3.5).toInt(),
            rx = angleRadius.toFloat() * 1.5.toFloat(),
            ry = angleRadius.toFloat() * 2.1.toFloat()
        ) else image?.makeTransparent(40)?.addPadding(4, 4, 4, 4)

        return image
    }

    private fun Bitmap.makeBottomLeftImage(
        context: Context,
        angleRadius: Int,
        isActive: Boolean
    ): Bitmap? {
        var image: Bitmap? = this

        image = image?.makeRoundedCorners(
            bottomLeftRadius = angleRadius,
            rx = angleRadius.toFloat(),
            ry = angleRadius.toFloat()
        )
        image = if (isActive) image?.setShadow(
            context = context,
            angleRadius = angleRadius
        )?.makeRoundedCorners(
            bottomLeftRadius = (angleRadius * 3.5).toInt(),
            rx = angleRadius.toFloat() * 1.9.toFloat(),
            ry = angleRadius.toFloat() * 1.6.toFloat()
        ) else image?.makeTransparent(40)?.addPadding(4, 4, 4, 4)

        return image
    }

    private fun Bitmap.makeBottomRightImage(
        context: Context,
        angleRadius: Int,
        isActive: Boolean
    ): Bitmap? {
        var image: Bitmap? = this

        image = image?.makeRoundedCorners(
            bottomRightRadius = angleRadius,
            rx = angleRadius.toFloat(),
            ry = angleRadius.toFloat()
        )
        image = if (isActive) image?.setShadow(
            context = context,
            angleRadius = angleRadius
        )?.makeRoundedCorners(
            bottomRightRadius = angleRadius * 2,
            rx = angleRadius.toFloat() * 2.25.toFloat(),
            ry = angleRadius.toFloat() * 2.25.toFloat()
        ) else image?.makeTransparent(40)?.addPadding(4, 4, 4, 4)

        return image
    }

    private fun Bitmap.makeImage(
        context: Context,
        isActive: Boolean
    ): Bitmap? {
        var image: Bitmap? = this

        image = if (isActive) image?.setShadow(context, 0)
        else image?.makeTransparent(40)?.addPadding(4, 4, 4, 4)

        return image
    }

    override fun getItemCount(): Int = listItem?.size ?: 0
}