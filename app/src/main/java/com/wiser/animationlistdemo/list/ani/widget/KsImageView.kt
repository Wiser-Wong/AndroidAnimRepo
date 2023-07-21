package com.wiser.animationlistdemo.list.ani.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.list.ani.getBitmapFromVectorDrawable
import com.wiser.animationlistdemo.list.ani.scaleStyle2
import com.wiser.animationlistdemo.list.ani.scaleStyle3

/**
 * @date: 2023/3/13 11:07
 * @author: jiaruihua
 * @desc :
 *
 *  底部导航栏
 */
class KsImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {

//    var iconDst:Bitmap?=null

    var totalW: Float = 0f
    var totalH: Float = 0f

    var tw: Int = 0
    var th: Int = 0

    val iconDstBook by lazy { getBitmapFromVectorDrawable(context, R.drawable.cc_book) }
    val iconDstSport by lazy { getBitmapFromVectorDrawable(context, R.drawable.sportsq) }
    val iconDstApple by lazy { getBitmapFromVectorDrawable(context, R.drawable.apple) }
    val iconDstMine by lazy { getBitmapFromVectorDrawable(context, R.drawable.mine) }

    val iconDstBookSlected by lazy {
        getBitmapFromVectorDrawable(
            context,
            R.drawable.cc_book_selected
        )
    }
    val iconDstSportSlected by lazy {
        getBitmapFromVectorDrawable(
            context,
            R.drawable.sportsq_selected
        )
    }
    val iconDstAppleSlected by lazy {
        getBitmapFromVectorDrawable(
            context,
            R.drawable.appleselected
        )
    }
    val iconDstMineSlected by lazy { getBitmapFromVectorDrawable(context, R.drawable.mineselected) }

    val iconSrcBook by lazy {
        getBitmapFromVectorDrawable(
            context = context,
            R.drawable.blue_big_book_dot
        )
    }
    val iconSrcSport by lazy {
        getBitmapFromVectorDrawable(
            context = context,
            R.drawable.blue_big_sport_dot
        )
    }
    val iconSrcApple by lazy {
        getBitmapFromVectorDrawable(
            context = context,
            R.drawable.blue_big_apple_dot
        )
    }
    val iconSrcMine by lazy {
        getBitmapFromVectorDrawable(
            context = context,
            R.drawable.blue_big_mine_dot
        )
    }




    var rect = Rect()

    var type: Int = 3
    var isSelectedIcon = true

    var delayDrawDot = false


    fun setImageIcon(type: Int, selected: Boolean) {
        this.type = type
        this.isSelectedIcon = selected

        if (isSelectedIcon){
            val img = when(type){
                1-> R.drawable.sportsq_selected
                2-> R.drawable.appleselected
                3-> R.drawable.mineselected
                else->R.drawable.cc_book_selected
            }
            scaleStyle2(this@KsImageView,object :AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    setImageResource(img)
                    invalidate()

                }
            })
            scaleStyle3(this@KsImageView,object :AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
//                    setImageResource(img)

                }
            })


        }else{
            val img = when(type){
                1-> R.drawable.sportsq
                2-> R.drawable.apple
                3-> R.drawable.mine
                else->R.drawable.cc_book
            }
            setImageResource(img)

        }


//        invalidate()
    }

    fun drawSelectedDot(){
        if (isSelectedIcon){
            delayDrawDot=true
            invalidate()
        }
    }

    val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            isDither = true
            color = ContextCompat.getColor(context, R.color.dark_blue)
            isFilterBitmap = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        rect.left = 0
        rect.top = 0
        rect.right = tw
        rect.bottom = th
    }




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawDot()


//            val iconDst = when (type) {
//                1 -> if (isSelectedIcon) iconDstSportSlected else iconDstSport
//                2 -> if (isSelectedIcon) iconDstAppleSlected else iconDstApple
//                3 -> if (isSelectedIcon) iconDstMineSlected else iconDstMine
//                else -> if (isSelectedIcon) iconDstBookSlected else iconDstBook
//            }
//            drawBitmap(iconDst, 0f, 0f, paint)


        }

    }

    private fun Canvas.drawDot() {
        if (isSelectedIcon) {
    //                paint.xfermode = xfermode
            when (type) {

                1 -> {

                    drawBitmap(iconSrcSport, 28f, 10f, paint)

                }
                2 -> {
                    drawBitmap(
                        iconSrcApple,
                        (iconDstAppleSlected.width -context.resources.getDimension(R.dimen.ksl_dp_17)),
                        context.resources.getDimension(R.dimen.ksl_dp_10),
                        paint
                    )
                }
                3 -> {
                    drawBitmap(iconSrcMine, 30f, 14f, paint)
                }

                else -> {
                    drawBitmap(iconSrcBook, 23f, 32f, paint)
                }
            }

//            paint.xfermode = null
        }
    }

}