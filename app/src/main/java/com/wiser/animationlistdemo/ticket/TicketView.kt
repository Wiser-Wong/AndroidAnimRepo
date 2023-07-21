package com.wiser.animationlistdemo.ticket

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.scale
import androidx.core.text.underline
import com.bumptech.glide.Glide
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.home.DataSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @date: 2023/5/17 16:54
 * @author: jiaruihua
 * @desc :
 *
 */
class TicketView:FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

//    val ldot by lazy { findViewById<View>(R.id.leftDot) }
//    val rdot by lazy { findViewById<View>(R.id.rightDot) }

    val tvDesc by lazy { findViewById<TextView>(R.id.desc) }
    val ivTop by lazy { findViewById<ImageView>(R.id.ivTop) }
    val ivSmall by lazy { findViewById<ImageView>(R.id.ivBottom) }

    init {

        inflate(context, R.layout.card_item,this)


        DataSource.getCards().random().let {
            val text = it.desc

            tvDesc.postDelayed( {
                //设置动画字体大小和颜色
                tvDesc.textSize = 20F
                tvDesc.setTextColor(Color.parseColor("#4a4a4a"))
                //使用一个CharSequence包装文本，实现一个字符一个字符的动画效果
                val cs = SpannableStringBuilder()
                text.forEachIndexed { i,c ->
                    MainScope().launch {
                        delay(i*100L)
                        println("----------------$c-")
                        val startIndex = text.indexOf(it.title)
                        val endIndex = startIndex+it.title.length
                        if (it.title.contains("$c")&&(i in startIndex .. endIndex)) {
                            cs.bold {
                                color(ContextCompat.getColor(context,R.color.color_13be58)) {
                                    scale(1.3f) {
                                        underline { cs.append("$c") }

                                    }
                                }

                            }
                        } else {
                            cs.append("$c")
                        }
                        tvDesc.setText(cs, TextView.BufferType.NORMAL)
                    }

                }



//                val charSequence: CharSequence = cs
//                val animation = ValueAnimator.ofInt( 0, charSequence.length)
//                animation.duration = 2000
//                animation.interpolator = AccelerateDecelerateInterpolator()
//                animation.addListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
//                        //动画结束后显示完整文本
//                        tvDesc.setText(text, TextView.BufferType.NORMAL)
//                    }
//                })
//                animation.start()
            },500)

            Glide.with(this).load(it.cover).into(ivTop)
            Glide.with(this).load(it.smallCover).into(ivSmall)

        }



//        ldot.post {
//            ldot.x = (-ldot.width/2).toFloat()
//        }
//
//        rdot.post {
//            rdot.x = rdot.x+(rdot.width/2).toFloat()
//        }

    }
}