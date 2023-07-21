package com.wiser.animationlistdemo.list.ani.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.wiser.animationlistdemo.list.ani.scaleStyle1
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/3/13 11:07
 * @author: jiaruihua
 * @desc :
 *
 *  底部导航栏
 */
class NavImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

//    var iconDst:Bitmap?=null

    val ksImageView by  lazy { findViewById<KsImageView>(R.id.navIcon) }
    val leftDot by  lazy { findViewById<ImageView>(R.id.leftDot) }
    val topDot by  lazy { findViewById<ImageView>(R.id.topDot) }
    val rightDot by  lazy { findViewById<ImageView>(R.id.rightDot) }

    init {
        inflate(context,R.layout.nav_imge,this)
    }

    val S_BOOK = 0
    val S_SPORT = 1
    val S_APPLE = 2
    val S_MINE = 3
    var type:Int = 3
    var isSelectedIcon = true

    fun setImageIcon(type:Int,selected:Boolean){
        this.type = type
        this.isSelectedIcon = selected


        ksImageView.setImageIcon(type,isSelectedIcon)




        if (isSelectedIcon){
            leftDot.visibility = View.VISIBLE
            topDot.visibility = View.VISIBLE
            rightDot.visibility = View.VISIBLE

            scaleStyle1(leftDot,50L,null)
            scaleStyle1(topDot,0L,null)

            scaleStyle1(rightDot, 10L,null)
        }else{
            leftDot.visibility = View.INVISIBLE
            topDot.visibility = View.INVISIBLE
            rightDot.visibility = View.INVISIBLE
        }

    }


}