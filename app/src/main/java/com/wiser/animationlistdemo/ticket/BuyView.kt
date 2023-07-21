package com.wiser.animationlistdemo.ticket

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.changeColor
import com.wiser.animationlistdemo.audio.scale
import com.wiser.animationlistdemo.home.Card
import com.wiser.animationlistdemo.home.DataSource
import jp.wasabeef.glide.transformations.BlurTransformation


/**
 * @date: 2023/5/16 17:48
 * @author: jiaruihua
 * @desc :
 *
 */
class BuyView : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }


    val rcList by lazy { findViewById<RecyclerView>(R.id.rcList) }
    val tvTitle by lazy { findViewById<TextView>(R.id.cardTitle) }
    val btnSelected by lazy { findViewById<TextView>(R.id.btnSelected) }
    val btnCard by lazy { findViewById<CardView>(R.id.btnCard) }

    val coverBgg by lazy { findViewById<ShapeableImageView>(R.id.coverBgg) }
    val ivClose by lazy { findViewById<View>(R.id.ivClose) }

    val rcData by lazy { getData() }
    var checkIndex = 0
    val mAdapter by lazy {
        TAdapter(rcData) {

            if (checkIndex == 0) {
                checkIndex++
                changeColor(Color.parseColor("#bcc5e6"), Color.parseColor("#1e2034"), dtime = 500) {
                    btnCard.setCardBackgroundColor(it)
                }
            }


        }
    }

    fun setTouchDelegate(view: View, expandTouchWidth: Int) {
        val parentView = view.parent as View
        parentView.post {
            val rect = Rect()
            view.getHitRect(rect) // view构建完成后才能获取，所以放在post中执行
            // 4个方向增加矩形区域
            rect.top -= expandTouchWidth
            rect.bottom += expandTouchWidth
            rect.left -= expandTouchWidth
            rect.right += expandTouchWidth
            parentView.touchDelegate = TouchDelegate(rect, view)
        }
    }
    var skipListener: ((v: View) -> Unit)? = null
    var closeListener: ((v: View) -> Unit)? = null

    fun registerSkip(skip: (v: View) -> Unit) {
        skipListener = skip
    }

    fun registerClose(close: (v: View) -> Unit){
        closeListener = close
    }

    fun getData(): List<Card> {

        val list = mutableListOf<String>()

        for (i in 1..12) {
            list.add("第${i}章")
        }

        return DataSource.getCards()

    }

    var coverBg:String?=null

    fun updateTitle(title: String,cover:String) {
        tvTitle.text = title
        coverBg = cover

        coverBg?.let {
            // 本地资源
            Glide.with(context).load(it)
                .diskCacheStrategy(
                    DiskCacheStrategy.RESOURCE
                ).dontAnimate().apply(
                    RequestOptions.bitmapTransform(BlurTransformation(50))
                ).into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        coverBgg.alpha = 0.8f
                        coverBgg.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                })
        }
    }

    init {

        inflate(context, R.layout.buy_view, this)

        rcList.run {

            adapter = mAdapter

            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)


        }

        btnCard.setOnClickListener {
            scale(it, 0.90f, stiffness = 500F) {
                scale(it, 1f, stiffness = 500F) {
                    skipListener?.invoke(btnSelected)
                }
            }
        }

        setTouchDelegate(ivClose,20)
        ivClose.setOnClickListener {
            closeListener?.invoke(ivClose)
        }



    }


    class TAdapter(val data: List<Card>, private val onSelect: (position: Int) -> Unit) :
        RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

            return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_tv, parent, false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
//            holder.tv.text = data.get(position)

            val item = data.get(position)


            Glide.with(holder.tv).load(item.smallCover).into(holder.tv)
            holder.tv.setOnClickListener {
                scale(it, 0.98f) {
                    scale(it, 1f) {
                    }
                }



//                changeColor(holder.tv.currentTextColor, Color.WHITE) {
//                    holder.tv.setTextColor(it)
//                }
                it.changeColor()
                holder.cc.setBackgroundColor(Color.parseColor("#66000000"))
                onSelect.invoke(position)
            }
        }

    }


    class VH(val itemV: View) : RecyclerView.ViewHolder(itemV) {

        val tv = itemV.findViewById<ShapeableImageView>(R.id.tv)
        val card = itemV.findViewById<CardView>(R.id.card)
        val cc = itemV.findViewById<View>(R.id.cc)
    }

}


fun View.changeColor(color: Int = Color.parseColor("#1e2034"), durationTime: Long = 300) {

    post {
        let {
            it.setBackgroundColor(color)

            val aniReveal = ViewAnimationUtils.createCircularReveal(
                it,
                width.toInt() / 2,
                height / 2,
                0f,
                width.toFloat()
            ).apply {
                duration = durationTime
                interpolator = LinearInterpolator()
            }

            aniReveal.start()

        }
    }
}

