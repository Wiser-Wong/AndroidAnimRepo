package com.wiser.animationlistdemo.ticket

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.transY

/**
 * @date: 2023/5/17 17:05
 * @author: jiaruihua
 * @desc :
 *
 */
class Tickets :
    FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        setPadding(30, 30, 30, 30)
        addTickets()
    }

    init {

    }

    fun updateTitle(title:String){
        for (i in 0 until  childCount)
        {
//            getChildAt(i).findViewById<TextView>(R.id.tvTitle).text = title

        }
    }
    val count = 2

    val params by lazy { LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT) }
    fun addTickets(){
        for (i in 0 until count){
            val ticketView = TicketView(context)
            ticketView.scaleX = 0.85f
            ticketView.scaleY = 0.85f
            ticketView.alpha =0f
            translationY = i* resources.getDimension(R.dimen.ksl_dp_10)
            addView(ticketView,params)
        }

        post {
            for (i in 0 until  childCount)
            {

                println("------------i=$i---height=$height")
                getChildAt(i)?.let {
                    var delay = i * 200
                    it.rotation = -(i-1)*5f
                    transY(it, height.toFloat(), i* resources.getDimension(R.dimen.ksl_dp_10), delay = delay.toLong(), onStart = {
                        it.alpha = 1f
                        it.visibility = VISIBLE
                    })
                }
            }
        }






    }
}