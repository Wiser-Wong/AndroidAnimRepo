package com.wiser.animationlistdemo.detailspage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.wiser.animationlistdemo.R

class StoryListAdapter(private var list: MutableList<Int>? = null) :
    RecyclerView.Adapter<StoryListHolder>() {

    fun setData(list: MutableList<Int>?) {
        this.list = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListHolder {
        return StoryListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        )
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: StoryListHolder, position: Int) {
        holder.ivStory?.setImageResource(list?.get(position) ?: 0)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "===>>$position", Toast.LENGTH_LONG).show()
        }
    }

}

class StoryListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivStory: AppCompatImageView? by lazy { itemView.findViewById(R.id.iv_story) }
}