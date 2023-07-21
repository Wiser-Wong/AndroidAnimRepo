package com.wiser.animationlistdemo.ticket

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.changeColor
import com.wiser.animationlistdemo.audio.scale
import com.wiser.animationlistdemo.audio.scale2
import com.wiser.animationlistdemo.databinding.ActTicketBinding
import com.wiser.animationlistdemo.databinding.TicketSeatBinding

/**
 * @date: 2023/5/17 14:11
 * @author: jiaruihua
 * @desc :
 *
 */
class TicketSeat : AppCompatActivity() {


    /**
     *
     * @property state Int 1 实心 2 空心 3 透明
     * @property title String
     * @constructor
     */
    data class Seat(val state: Int, val title: String)

    private var binding: TicketSeatBinding? = null

    val rcData by lazy { getData() }

    fun getData(): List<Seat> {

        val list = mutableListOf<Seat>()
        list.add(Seat(1, "A1"))
        list.add(Seat(1, "A2"))
        list.add(Seat(2, "A3"))
        list.add(Seat(2, "A4"))
        list.add(Seat(2, "A5"))
        list.add(Seat(1, "A6"))


        list.add(Seat(1, "B1"))
        list.add(Seat(1, "B2"))
        list.add(Seat(2, "B3"))
        list.add(Seat(2, "B4"))
        list.add(Seat(1, "B5"))
        list.add(Seat(1, "B6"))

        list.add(Seat(1, "C1"))
        list.add(Seat(1, "C2"))
        list.add(Seat(1, "C3"))
        list.add(Seat(2, "C4"))
        list.add(Seat(2, "C5"))
        list.add(Seat(1, "C6"))


        list.add(Seat(2, "D1"))
        list.add(Seat(2, "D2"))
        list.add(Seat(2, "D3"))
        list.add(Seat(1, "D4"))
        list.add(Seat(1, "D5"))
        list.add(Seat(1, "D6"))

        list.add(Seat(1, "E1"))
        list.add(Seat(2, "E2"))
        list.add(Seat(3, "E3"))
        list.add(Seat(3, "E4"))
        list.add(Seat(1, "E5"))
        list.add(Seat(1, "E6"))

        list.add(Seat(1, "F1"))
        list.add(Seat(2, "F2"))
        list.add(Seat(3, "F3"))
        list.add(Seat(3, "F4"))
        list.add(Seat(3, "F5"))
        list.add(Seat(1, "F6"))
        return list

    }

    var checkIndex = 0

    private val mAdapter by lazy { TAdapter(rcData) {
        if (checkIndex==0)
        {
            checkIndex++
            changeColor(Color.parseColor("#bcc5e6"), Color.parseColor("#1e2034"), dtime = 500) {
                binding?.btnCard?.setCardBackgroundColor(it)
            }
        }

    } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TicketSeatBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.ivback?.setOnClickListener { finish() }

        binding?.cardTitle?.let {
            it.text = intent.getStringExtra("title")
            ObjectAnimator.ofFloat(it, "alpha", 0f, 1f).apply {
                duration = 2500
                interpolator = DecelerateInterpolator()
            }.start()
        }

        binding?.rcList?.run {

            adapter = mAdapter
            itemAnimator = CustomItemAnimator()
            layoutManager = GridLayoutManager(this@TicketSeat, 6, GridLayoutManager.VERTICAL, false)


        }

        binding?.btnCard?.setOnClickListener {
            scale(it, 0.90f, stiffness = 500F) {
                scale(it, 1f, stiffness = 500F) {
                    startActivity(Intent(this@TicketSeat,TicketDetail::class.java).putExtra("title",intent.getStringExtra("title")))
                }
            }

        }

    }

    class TAdapter(val data: List<Seat>, private val onSelect: (position: Int) -> Unit) :
        RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

            return VH(LayoutInflater.from(parent.context).inflate(R.layout.seat_tv, parent, false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = data.get(position)
            holder.itemV.alpha=0f
            ObjectAnimator.ofFloat(holder.itemV,"alpha",0f,1f).apply {
                duration=200
                startDelay= (position*30).toLong()
                interpolator = AccelerateInterpolator()
                start()
            }

            when (item.state) {
                1 -> {
                    holder.tv.setBackgroundColor(Color.parseColor("#d4dbfc"))
                }
                2 -> {
                    holder.tv.setBackgroundColor(Color.parseColor("#ffffff"))
                    holder.card.strokeColor = Color.parseColor("#d8dffd")
                    holder.card.strokeWidth =
                        holder.itemV.resources.getDimension(R.dimen.ksl_dp_2).toInt()

                }
                else -> {
                    holder.itemV.visibility = View.INVISIBLE
                }
            }

            changeColor(holder.tv.currentTextColor, Color.WHITE) {
                holder.tv.setTextColor(it)
            }
            if (item.state == 2) {
                holder.tv.setOnClickListener {
                    holder.card.strokeWidth = 0
                    holder.tv.text = item.title
                    scale2(holder.card, 0.85f,stiffness=500f) {
                        scale2(holder.card, 1f,stiffness=520f) {
                        }
                    }


                    it.changeColor(durationTime = 400)

                    onSelect.invoke(position)
                }
            }

        }

    }


    class VH(val itemV: View) : RecyclerView.ViewHolder(itemV) {

        val tv = itemV.findViewById<TextView>(R.id.tv)
        val card = itemV.findViewById<MaterialCardView>(R.id.card)
    }




    class CustomItemAnimator : DefaultItemAnimator() {

        // 定义动画持续时间
        private val animationDuration = 500L

        override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
            // 定义透明度动画效果
            val alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation.duration = animationDuration
            holder.itemView.startAnimation(alphaAnimation)

            // 调用默认的动画效果
            return super.animateAdd(holder)
        }

        override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
            // 定义透明度动画效果
            val alphaAnimation = AlphaAnimation(1f, 0f)
            alphaAnimation.duration = animationDuration
            holder?.itemView?.startAnimation(alphaAnimation)

            // 调用默认的动画效果
            return super.animateRemove(holder)
        }
    }
}