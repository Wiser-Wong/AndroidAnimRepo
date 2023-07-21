package com.wiser.animationlistdemo.audio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.home.DataSource
import com.wiser.animationlistdemo.home.ImageData
import com.wiser.animationlistdemo.list.ani.bean.ColorItem

/**
 * @date: 2023/5/10 10:31
 * @author: jiaruihua
 * @desc :
 *
 */
class ImageAudioAdapter(
    val onItemClick: () -> Unit,
    val skip: (v2: View, v: View, cover: Int, position: Int) -> Unit
) : RecyclerView.Adapter<AudioVH>() {

    val dataSource = DataSource.getAudioResource()


    val picViews = mutableSetOf<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioVH {
        return AudioVH(LayoutInflater.from(parent.context).inflate(R.layout.item_audio_imge, parent, false))
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: AudioVH, position: Int) {
        dataSource.getOrNull(position)?.let { data ->

            holder.cover?.let {
                Glide.with(it).load(data.src).into(it)
                picViews.add(it)
            }

            holder.iView.updateLayoutParams {
                width = (holder.cover.resources.displayMetrics.widthPixels*0.7f).toInt()
            }
        }



        holder.cover.setOnClickListener {
            scale(holder.iView, 0.95f, stiffness = 200f) {
                skip.invoke(
                    holder.cover,
                    holder.iView,
                    dataSource.getOrNull(position)?.src ?: 0,
                    position
                )
                scale(holder.iView, 1f, stiffness = 200f) {

                }

                onItemClick?.invoke()
            }

        }


    }
}


class AudioVH(val iView: View) : RecyclerView.ViewHolder(iView) {

    val cover: ImageView = iView.findViewById(R.id.cover)
    val ivPlay: ImageView = iView.findViewById(R.id.ivPlay)



}


class HAudioAdaper( val onItemClick: () -> Unit,
                    val skip: (v2: View, v: View, cover: Int, position: Int) -> Unit) : BaseQuickAdapter<ImageData, BaseViewHolder>(R.layout.item_audio_imge) {
    override fun convert(holder: BaseViewHolder, item: ImageData) {

        holder.itemView.setOnClickListener {
            setOnItemClick(it, holder.getAdapterPosition())
        }

        holder.itemView.updateLayoutParams {
            width = (holder.itemView.resources.displayMetrics.widthPixels*0.7f).toInt()
        }

        holder.getView<ShapeableImageView>(R.id.cover).apply {
            if (!item.cover.isNullOrEmpty()){
                Glide.with(this).load(item.cover).into(this)

            }else{

            Glide.with(this).load(item.src).into(this)
            }
            setOnClickListener {
                scale(holder.itemView, 0.95f, stiffness = 200f) {
                    skip.invoke(
                        this,
                        holder.itemView,
                        item?.src ?: 0,
                        holder.adapterPosition
                    )
                    scale(holder.itemView, 1f, stiffness = 200f) {

                    }

                    onItemClick?.invoke()
                }
            }
        }


    }

}