package com.wiser.animationlistdemo.ticket

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/5/16 18:14
 * @author: jiaruihua
 * @desc :
 *
 */
class NineGridView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private var horizontalSpacing = 0
    private var verticalSpacing = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.NineGridView)
        horizontalSpacing = a.getDimensionPixelSize(R.styleable.NineGridView_horizontalSpacing, 0)
        verticalSpacing = a.getDimensionPixelSize(R.styleable.NineGridView_verticalSpacing, 0)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec)
        val mode = MeasureSpec.getMode(widthMeasureSpec)

        // 先计算一行的情况
        val oneRowCount = ((size - paddingLeft - paddingRight) / (measuredCellSize() + horizontalSpacing)).toInt()
        val finalRowCount = if (childCount <= oneRowCount) 1 else {
            val row = childCount / oneRowCount
            val column = childCount % oneRowCount
            if (column == 0) row else row + 1
        }

        val totalHeight = finalRowCount * measuredCellSize() + paddingTop + paddingBottom + ((finalRowCount - 1) * verticalSpacing)
        setMeasuredDimension(getMeasureSize(size, mode), totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var rowIndex = 0
        var columnIndex: Int
        var startX = paddingLeft
        var startY = paddingTop
        for (i in 0 until childCount) {
            val measuredSize = measuredCellSize()
            columnIndex = i % getColumnCount()
            if (i != 0 && columnIndex == 0) {
                rowIndex += 1
                startX = paddingLeft
                startY += measuredSize + verticalSpacing
            }
            val dx = measuredSize * columnIndex + horizontalSpacing * columnIndex
            val dy = measuredSize * rowIndex + verticalSpacing * rowIndex
            getChildAt(i)?.layout(startX + dx, startY + dy, startX + dx + measuredSize, startY + dy + measuredSize)
        }
    }

    private fun measuredCellSize(): Int {
        return ((measuredWidth - paddingLeft - paddingRight - (getColumnCount() - 1) * horizontalSpacing) / getColumnCount())
    }

    private fun getColumnCount(): Int {
        return 3
    }

    private fun getMeasureSize(size: Int, mode: Int): Int {
        return when (mode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> size
            else -> context.resources.displayMetrics.widthPixels
        }
    }
}

fun View.measuredWidth(): Int {
    measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    return measuredWidth
}

fun View.measuredHeight(): Int {
    measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    return measuredHeight
}