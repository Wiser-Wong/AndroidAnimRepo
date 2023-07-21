package com.wiser.animationlistdemo.list.ani.adaper

import androidx.cardview.widget.CardView
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

class VAdaper : BaseQuickAdapter<ColorItem, BaseViewHolder>(R.layout.v_item) {
    override fun convert(holder: BaseViewHolder, item: ColorItem) {
        holder.itemView.setOnClickListener {
            setOnItemClick(it, holder.getAdapterPosition())
        }


        holder.getView<ShapeableImageView>(R.id.shape).let { iv->
            val color = item.color

//            DataSource.getImageResource().random().let {
//                iv.setImageResource(it.src)
//            }

            DataSource.getBook().let {
                val itemPosition = holder.absoluteAdapterPosition%it.size
                val data = it.get(itemPosition)
                Glide.with(iv).load(data.cover).into(iv)
            }
        }
    }

}