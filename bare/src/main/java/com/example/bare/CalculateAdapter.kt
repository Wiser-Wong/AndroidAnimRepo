package com.example.bare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LayoutAnimationController
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Calculate adapter.
 *
 * @constructor Create empty constructor for calculate adapter
 */
class CalculateAdapter(val activity2: MainActivity2) : Adapter<CalcuHolder>() {
    private val data = arrayListOf<Int>()
    private var checkIndex = -1
    private var needAni = true
    private var lock = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalcuHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.calcu_btn, parent, false)
        return CalcuHolder(inflate as ViewGroup)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CalcuHolder, position: Int) {
        holder.bindData(data[position],position == checkIndex || checkIndex == -1,needAni)
        holder.itemView.setOnClickListener {
            if (lock) return@setOnClickListener
            lock = true
            needAni = false
            check(position)
            activity2.onCheckAni(data[position] == 39)
        }
    }

    fun setData(newData:List<Int>){
        needAni = true
        lock = false
        checkIndex  = -1
        data.apply {
            clear()
            addAll(newData)
            notifyDataSetChanged()
        }
    }

    fun check(index:Int){
        checkIndex = index
        notifyDataSetChanged()
    }
}

class CalcuHolder(itemView: ViewGroup) : ViewHolder(itemView) {
    private val textView:TextView by lazy { itemView.findViewById(R.id.text_result) }

    fun bindData(data: Int, isChecked: Boolean,needAni:Boolean) {
        if (needAni){
            val animation: Animation = ScaleAnimation(0f,1f,0f,1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f,)
            animation.duration = 400
            animation.fillAfter = true
            animation.interpolator = OvershootInterpolator()
            itemView.startAnimation(animation)
        }

        textView.setText(data.toString())
        itemView.alpha = if (isChecked) 1f else 0.5f
    }
}