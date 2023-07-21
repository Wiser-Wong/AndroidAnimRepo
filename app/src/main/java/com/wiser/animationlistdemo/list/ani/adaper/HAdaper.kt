package com.wiser.animationlistdemo.list.ani.adaper

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.wiser.animationlistdemo.list.ani.bean.ColorItem
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.home.DataSource

/**
 * @date: 2023/3/14 16:41
 * @author: jiaruihua
 * @desc :
 *
 */

class HAdaper(val onClick:()->Unit) : BaseQuickAdapter<ColorItem, BaseViewHolder>(R.layout.h_item) {
    override fun convert(holder: BaseViewHolder, item: ColorItem) {

        holder.itemView.setOnClickListener {
            setOnItemClick(it, holder.getAdapterPosition())
        }

        holder.getView<ShapeableImageView>(R.id.shape).apply {
            val color = item.color
//            setBackgroundColor(color)
            DataSource.getBook().let {
                val itemPosition = holder.absoluteAdapterPosition%it.size
                val data = it.get(itemPosition)
                Glide.with(this).load(data.cover).into(this)
            }




//
//            val alpha = ObjectAnimator.ofFloat(this,"alpha",0f, 1f).apply {
////                startDelay = holder.absoluteAdapterPosition*200L
//
//            }
//            val transX = ObjectAnimator.ofFloat(this,"translationX",resources.getDimension(com.ks.ui.sw.R.dimen.ksl_dp_800), 0f).apply {
////                startDelay = holder.absoluteAdapterPosition*200L
//            }
//            val transY = ObjectAnimator.ofFloat(this,"translationY",resources.getDimension(com.ks.ui.sw.R.dimen.ksl_dp_100), 0f).apply {
////                startDelay = holder.absoluteAdapterPosition*200L
//            }
//
//
//            AnimatorSet().apply {
//                duration = 1500
//
//                playTogether(alpha,transX)
//
//            }.start()


//            animation = AnimationUtils.loadAnimation(context,R.anim.trans_alpha)
        }
    }

}