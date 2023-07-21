package com.wiser.animationlistdemo.ticket

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.rotationY
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.home.DataSource
import com.wiser.animationlistdemo.home.HomeActivity
import com.wiser.animationlistdemo.home.ImageData
import java.util.*

/**
 * @date: 2023/5/15 14:14
 * @author: jiaruihua
 * @desc :
 *
 */
class TicketAdapter(val context: Context,val stackView: StackView) : BaseAdapter() {

    val dataSource = DataSource.getBook().subList(0,4)

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): ImageData {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id.toLong()
    }

    @SuppressLint("ViewHolder", "MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false)



        fun currentPositionCard() = count - stackView.childCount
        val item = getItem(position)
        view.findViewById<TextView>(R.id.tvText).text = "${item.title}"
        view.findViewById<ImageView>(R.id.cover).let {

            Glide.with(it).load(item.cover).into(it)
        }

        val second = view.findViewById<BuyView>(R.id.secondView)
        val firstView = view.findViewById<View>(R.id.firstView)
        second.updateTitle(item.title,item.cover!!)
        second.setOnTouchListener { v, event ->
            println("aaaaaaaa")
            true }
        second.registerSkip {
            if (position != currentPositionCard())return@registerSkip

            val cls = if (position%2==0) ShareAct::class.java else TicketDetail::class.java
            context.startActivity(Intent(context, cls).apply {
                putExtra("title", item.title)
            })
        }

        second.registerClose {
            if (position != currentPositionCard()) return@registerClose
            val rotationY = ObjectAnimator.ofFloat(view, "rotationY", 180F, 360F)
            val alphaA = ObjectAnimator.ofFloat(firstView, "alpha", 0F, 1F)
            val alphaB = ObjectAnimator.ofFloat(second, "alpha", 1F, 0F)


            AnimatorSet().run {
                duration = 400
                interpolator = AccelerateInterpolator()
                playTogether(rotationY,alphaA,alphaB)
                doOnStart {
                    second.visibility = View.INVISIBLE
                    view.rotationY = 0f
                }

                start()
            }
        }
        view.findViewById<View>(R.id.btnBuy).setOnClickListener {

            println("---------currentPositionCard = ${currentPositionCard()}---cp=$position")
            if (position != currentPositionCard()) return@setOnClickListener

            val rotationY = ObjectAnimator.ofFloat(view, "rotationY", 0F, 180F)
            val alphaA = ObjectAnimator.ofFloat(firstView, "alpha", 1F, 0F)
            val alphaB = ObjectAnimator.ofFloat(second, "alpha", 0F, 1F)


            AnimatorSet().run {
                duration = 400
                interpolator = AccelerateInterpolator()
                playTogether(rotationY,alphaA,alphaB)
                doOnStart {
                    second.visibility = View.VISIBLE
                    second.rotationY = -180f
                }

                start()
            }


        }

        return view
    }




    fun getRandomColor(): Int {
        val random = Random()
        //设置透明度为255（不透明）
        val alpha = 255
        //生成随机的RGB颜色
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        //合并ARGB值
        val color = alpha shl 24 or (red shl 16) or (green shl 8) or blue

        println("color-------$color")
        return color
    }
}