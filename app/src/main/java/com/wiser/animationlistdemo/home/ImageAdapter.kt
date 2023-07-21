package com.wiser.animationlistdemo.home

import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.scale

/**
 * @date: 2023/5/10 10:31
 * @author: jiaruihua
 * @desc :
 *
 */
class ImageAdapter(
    val onItemClick: () -> Unit,
    val skip: (v2: View, v: View, cover: Int, position: Int) -> Unit
) : RecyclerView.Adapter<VH>() {

    val dataSource = DataSource.getImageResource()


    val picViews = mutableSetOf<View>()

    fun reset(){
        picViews.forEach {
            it.rotation=0f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_imge, parent, false))
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        dataSource.getOrNull(position)?.let { data ->

//            holder.cover?.let {
////                it.post {
////                    it.translationX = it.width*0.3f
////                    it.translationY = -it.height*0.15f
////                }
//                Glide.with(it).load(data.src).into(it)
//
//                picViews.add(it)
//
//
//            }


            holder.cover.post {

//                Glide.with(holder.cover).load(data.src).into(holder.cover)
                holder.cover.setImageResource(data.src)
                holder.cover.run {
                    val dw = drawable.intrinsicWidth
                    val dh = drawable.intrinsicHeight

                    val sw = width.toFloat()/dw
                    val sh = height.toFloat()/dh

                    val m = Matrix().apply {
                        setScale(sw,sh)
                    }
                    imageMatrix = m
                    this.invalidate()
                    picViews.add(this)
                }

            }

            holder.bookMark.setColor(data.bgColor)
            var mark = when (position) {
                1 -> ""
                3 -> "故事"
                5 -> "阅读"
                else -> "故事"
            }
            holder.tvBookMark.text = data.getTvBookMark()
            holder.tvTitle.text = data.title

        }



        holder.card.setOnClickListener {
            scale(holder.card, 0.8f, stiffness = 200f) {
                skip.invoke(
                    holder.cover,
                    holder.card,
                    dataSource.getOrNull(position)?.src ?: 0,
                    position
                )
                scale(holder.card, 1f, stiffness = 200f) {

                }

                onItemClick?.invoke()
            }

        }


    }
}


class VH(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cover: ImageView = itemView.findViewById(R.id.cover)
    val card: View = itemView.findViewById(R.id.cardView)
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvSubTitle: TextView = itemView.findViewById(R.id.tvSubTitle)
    val tvBookMark: TextView = itemView.findViewById(R.id.tvBookMark)
    val bookMark: BookMarkView = itemView.findViewById(R.id.bookMark)


}