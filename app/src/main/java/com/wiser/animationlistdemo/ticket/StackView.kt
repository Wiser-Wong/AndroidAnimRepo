package com.wiser.animationlistdemo.ticket

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.rotationCenter
import com.wiser.animationlistdemo.audio.trans
import com.wiser.animationlistdemo.audio.transY
import java.util.*

/**
 * @date: 2023/5/15 14:05
 * @author: jiaruihua
 * @desc :
 *
 */
class StackView :
    FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        setPadding(30, 30, 30, 30)
    }

    private var adapter: BaseAdapter? = null

    private val ROTATION_DEGREE = -8f

    private val showMax = 4

    private var currentIndex = 0

    private val TRANS_DURATION = 250L

    private val SCALE = 0.85F

    /**
     * 控制 第二个view 的旋转 临界点
     */
    val swipeProgress = 0.8f


    fun currentPosition() = currentIndex

    private var nextViewProgress = -1f
        set(value) {
            println("StackView---------value=${value}---filed=$field")

            if (value!=-1f){
                if (value < swipeProgress && field > swipeProgress) {

                    onReset()
                }

                if (value > swipeProgress && field < swipeProgress) {
                    rotationRight()
                }
            }

            field = value

        }

    private val marginStart by lazy { resources.getDimension(R.dimen.ksl_dp_40) }


    private val snapHelper by lazy { SnapHelper(this) }
    fun onSwipeProgress(progresss: Float) {
        println("StackView---------progresss=${progresss}")
        nextViewProgress = progresss
    }


    fun onReset() {
        println("StackView---------onReset")
        if (childCount >= currentIndex) {
            val childIndex  = currentIndex - 1
            val nextChild = getChildAt(childIndex) ?: return
            rotationCenter(nextChild, 0f, getChildRotation(childIndex))
        }
    }

    fun rotationRight() {

        println("StackView---------rotationRight")
        if (childCount >= currentIndex) {
            val childIndex  = currentIndex - 1
            val nextChild = getChildAt(childIndex) ?: return
            rotationCenter(nextChild, getChildRotation(childIndex), 0f)
        }
    }

    fun onSwipeEnd() {

    }

    fun onNextShow() {
        removeViewAt(currentIndex)
        nextViewProgress=-1f
        if (childCount>1){
            currentIndex = childCount-1
            getChildAt(currentIndex)?.let {
                snapHelper.registerObservedView(it, it.x, it.y)
            }
        }

    }

    override fun addView(child: View?) {
        super.addView(child)
    }


    fun setDataAdapter(adapter: TicketAdapter) {
        this.adapter = adapter
        addItems(adapter)
    }


    private fun addItems(adapter: TicketAdapter) {

        var showCount = adapter.count
        if (adapter.count > showMax) {
            showCount = showMax
        }


        if (showCount > 0) {

            for (i in 0 until showCount) {
                val childView = adapter.getView(showCount - i - 1, null, this).apply {
                    scaleX = SCALE
                    scaleY = SCALE
                    visibility = View.INVISIBLE
                }
                addViewInLayout(childView, i, childView.layoutParams)
            }
        }

        doOnPreDraw {
            for (i in 0 until childCount) {

                getChildAt(i)?.translationX = when (i) {
                    1 -> getChildAt(0).width * (1 - SCALE) * 0.5f + resources.getDimension(R.dimen.ksl_dp_25)
                    else -> {
                        getChildAt(0).width * (1 - SCALE) * 0.5f + resources.getDimension(R.dimen.ksl_dp_15)
                    }
                }

                getChildAt(i)?.rotation = getChildRotation(i)


                getChildAt(i)?.let {
                    var delay = i * TRANS_DURATION
                    if (childCount >= 2) {
                        delay = when (i) {
                            childCount - 2 -> 0
                            childCount - 1 -> TRANS_DURATION
                            else -> (childCount - i - 1) * TRANS_DURATION
                        }
                    }

                    val end = when (i) {
                        0 -> {
                            it.scaleX = SCALE
                            it.scaleY = SCALE
                            -resources.getDimension(R.dimen.ksl_dp_5)
                        }
                        1 -> {
                            -resources.getDimension(R.dimen.ksl_dp_10)
                        }

                        else -> {
                            -resources.getDimension(R.dimen.ksl_dp_25)
                        }
                    }
                    println("delay-------------i=$i---$delay")
                    transY(it, height.toFloat(), end.toFloat(), delay = delay.toLong(), onStart = {
                        it.visibility = View.VISIBLE
                    })
                }

                if (i == childCount - 1) {
                    getChildAt(i)?.let { topView ->
                        currentIndex = i
                        snapHelper.registerObservedView(topView, topView.x, topView.y)

                    }
                }

            }
        }


    }

     fun getChildRotation(i: Int) = when (i) {

        3 -> 0f
        2 -> -15f
        1 -> -30f
        0 -> -45f

        else -> (childCount - 1 - i) * ROTATION_DEGREE
    }

}