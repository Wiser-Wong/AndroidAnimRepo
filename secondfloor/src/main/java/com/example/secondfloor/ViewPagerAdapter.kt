package com.example.secondfloor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(vm: MianVm) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData: MutableList<Int>
    private val vm: MianVm

    init {
        mData = ArrayList()
        mData.add(R.drawable.icon39)
        mData.add(R.drawable.icon38)
        this.vm = vm
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pager, parent, false)
            ViewHolder(view)
        } else {
            val myFramelayout = MyFramelayout(parent.context)
            myFramelayout.setVm(vm)
            ViewHolder2(myFramelayout)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.imageview.setImageResource(
                mData[position]
            )
        } else if (holder is ViewHolder2) {
            holder.c.imageView!!.setImageResource(mData[position])
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageview: ImageView

        init {
            imageview = itemView.findViewById(R.id.image)
        }
    }

    class ViewHolder2 internal constructor(var c: MyFramelayout) : RecyclerView.ViewHolder(
        c
    )
}