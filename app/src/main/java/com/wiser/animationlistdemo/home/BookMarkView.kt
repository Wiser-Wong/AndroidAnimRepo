package com.wiser.animationlistdemo.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.wiser.animationlistdemo.R


/**
 * @date: 2023/6/2 10:44
 * @author: jiaruihua
 * @desc :
 *
 */
class BookMarkView: View {

    constructor(context: Context) : super(context){
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        initPaint()
    }

    private var mPaint: Paint = Paint()
    private var path: Path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        path.reset()
        path.moveTo(0f,0f)
        path.lineTo(0f, height.toFloat())
        path.lineTo(width/2f, Math.max(height.toFloat()*0.93f,height-resources.getDimension(R.dimen.ksl_dp_25)))
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), 0f)
        path.close()

        val radius = 8f
        val cornerPathEffect = CornerPathEffect(radius)
        mPaint.pathEffect = cornerPathEffect
        canvas?.drawPath(path, mPaint)

    }


    fun setColor(color:Int)
    {
        mPaint.color = color
        invalidate()
    }

    private fun initPaint() {
        mPaint.apply {
            color = Color.RED
            style = Paint.Style.FILL
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND

        }
    }
}