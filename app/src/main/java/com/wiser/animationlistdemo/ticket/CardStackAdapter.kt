package com.wiser.animationlistdemo.ticket

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_MEDIUM
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.home.DataSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @date: 2023/6/14 17:04
 * @author: jiaruihua
 * @desc :
 *
 */
class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    val source = DataSource.getCards()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDesc by lazy { view.findViewById<TextView>(R.id.desc) }
        val ivTop by lazy { view.findViewById<ImageView>(R.id.ivTop) }
        val ivSmall by lazy { view.findViewById<ImageView>(R.id.ivBottom) }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {

        return source.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        DataSource.getCards().get(position).let {
            val text = it.desc
            holder. tvDesc.text = text



//            holder.tvDesc.postDelayed( {
//                //设置动画字体大小和颜色
//                holder.tvDesc.textSize = 20F
//                holder.tvDesc.setTextColor(Color.parseColor("#4a4a4a"))
//                //使用一个CharSequence包装文本，实现一个字符一个字符的动画效果
//                val cs = SpannableStringBuilder()
//                text.forEachIndexed { i,c ->
//                    MainScope().launch {
//                        delay(i*100L)
//                        println("----------------$c-")
//                        cs.append("$c")
//                        holder. tvDesc.setText(cs, TextView.BufferType.NORMAL)
//                    }
//
//                }
//            },500)

            Glide.with(holder.ivTop).load(it.cover).into( holder.ivTop)
            Glide.with(holder.ivSmall).load(it.smallCover).into( holder.ivSmall)

//            holder.ivSmall.post {
//
//                val anim = SpringAnimation(holder.ivSmall, DynamicAnimation.TRANSLATION_Y,900f)
////            anim.animateToFinalPosition(holder.ivTop.resources.getDimension(R.dimen.ksl_dp_260))
////            anim.setStartValue(holder.ivTop.resources.getDimension(R.dimen.ksl_dp_150))
//                anim.spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
//                anim.spring.stiffness = STIFFNESS_MEDIUM
//                holder.ivSmall?.postDelayed({  anim.start()},200)
//            }




        }

    }
}