package com.wiser.animationlistdemo.recyclerviewanim

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.recyclerviewanim.data.AnimBean

class RecyclerViewAnimActivity : AppCompatActivity() {

    private val rlv: RecyclerView? by lazy { findViewById(R.id.rlv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_anim_page)

        rlv?.layoutManager = LinearLayoutManager(this)
        rlv?.adapter = MainAdapter(getData())
        rlv?.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    RotateAnimationZ.angle = 45
                } else {
                    RotateAnimationZ.angle = -45
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager is LinearLayoutManager) {
                    //获取最后一个可见view的位置
                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                    //获取第一个可见view的位置
                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                    for (i in firstItemPosition..lastItemPosition) {
                        val view = layoutManager.findViewByPosition(i)
                    }
                }
            }
        })
    }

    private fun getData(): MutableList<AnimBean>? {
        val list = mutableListOf<AnimBean>()
        val animBean0 = AnimBean()
        animBean0.ipHeader = R.drawable.ip_0
        animBean0.background = R.drawable.shape_66cdaa_radius
        animBean0.backgroundColor = Color.parseColor("#66CDAA")
        animBean0.title = "肚大嘟"
        animBean0.subTitle = "肚大嘟是一只豚鼠,他要保持自己高度的神秘感,所以不会透露自己的身高年龄等信息。遇到不会的问题,会翻书找到答案去解决。"
        animBean0.detailsResId = R.drawable.ip_details_0
        val animBean1 = AnimBean()
        animBean1.ipHeader = R.drawable.ip_1
        animBean1.background = R.drawable.shape_yellow_radius
        animBean1.backgroundColor = Color.parseColor("#FFC125")
        animBean1.title = "董晓雨"
        animBean1.subTitle = "用知识指路，就不会犯错！好学上进，有同情心随身携带《野外生存手册》。"
        animBean1.detailsResId = R.drawable.ip_details_1
        val animBean2 = AnimBean()
        animBean2.ipHeader = R.drawable.ip_2
        animBean2.background = R.drawable.shape_ff8247_radius
        animBean2.backgroundColor = Color.parseColor("#ff8247")
        animBean2.title = "张小胖"
        animBean2.subTitle = "面对困难，笑一笑就有希望！力气大，胆小但乐观幽默会照顾同伴。"
        animBean2.detailsResId = R.drawable.ip_details_1
        val animBean3 = AnimBean()
        animBean3.ipHeader = R.drawable.ip_3
        animBean3.background = R.drawable.shape_8b6969_radius
        animBean3.backgroundColor = Color.parseColor("#8B6969")
        animBean3.title = "烈火号"
        animBean3.subTitle = "机甲护卫队里麻虎的座驾，拥有粒子屏蔽、点击重锤等技能。"
        animBean3.detailsResId = R.drawable.ip_details_2
        val animBean4 = AnimBean()
        animBean4.ipHeader = R.drawable.ip_4
        animBean4.background = R.drawable.shape_66cdaa_radius
        animBean4.backgroundColor = Color.parseColor("#66CDAA")
        animBean4.title = "艾晓波"
        animBean4.subTitle = "与鸡飞飞组成了“口袋神探”的组合，小坡负责勘查现场、逻辑推理，而鸡飞飞负责提供破局的科学知识。"
        animBean4.detailsResId = R.drawable.ip_details_3
        val animBean5 = AnimBean()
        animBean5.ipHeader = R.drawable.ip_5
        animBean5.background = R.drawable.shape_yellow_radius
        animBean5.backgroundColor = Color.parseColor("#FFC125")
        animBean5.title = "鸡飞飞"
        animBean5.subTitle = "与艾小坡互为穿越星际的“生命对应体”，他们可以用脑电波沟通。小坡学习知识、动脑破案，就可以给鸡飞飞积累思维能量。"
        animBean5.detailsResId = R.drawable.ip_details_3
        val animBean6 = AnimBean()
        animBean6.ipHeader = R.drawable.ip_6
        animBean6.background = R.drawable.shape_ff8247_radius
        animBean6.backgroundColor = Color.parseColor("#ff8247")
        animBean6.title = "孙悟空"
        animBean6.subTitle = "孙悟空，是在小说《西游记》中登场的主要角色之一，为一只道行高深的猴子，别名孙行者。自封美猴王、齐天大圣"
        animBean6.detailsResId = R.drawable.ip_details_4
        list.add(animBean0)
        list.add(animBean1)
        list.add(animBean2)
        list.add(animBean3)
        list.add(animBean4)
        list.add(animBean5)
        list.add(animBean6)
        for (i in 0..50) {
            val animBean = AnimBean()
            if (i % 2 == 1) {
                animBean.ipHeader = R.drawable.ip_0
                animBean.background = R.drawable.shape_66cdaa_radius
                animBean.backgroundColor = Color.parseColor("#66CDAA")
                animBean.title = "肚大嘟"
                animBean.subTitle = "肚大嘟是一只豚鼠,他要保持自己高度的神秘感,所以不会透露自己的身高年龄等信息。遇到不会的问题,会翻书找到答案去解决。"
                animBean.detailsResId = R.drawable.ip_details_0
            } else if (i / 2 == 1) {
                animBean.ipHeader = R.drawable.ip_2
                animBean.background = R.drawable.shape_ff8247_radius
                animBean.backgroundColor = Color.parseColor("#FFC125")
                animBean.title = "张小胖"
                animBean.subTitle = "面对困难，笑一笑就有希望！力气大，胆小但乐观幽默会照顾同伴。"
                animBean.detailsResId = R.drawable.ip_details_1
            } else if (i / 3 == 1) {
                animBean.ipHeader = R.drawable.ip_3
                animBean.background = R.drawable.shape_8b6969_radius
                animBean.backgroundColor = Color.parseColor("#8B6969")
                animBean.title = "烈火号"
                animBean.subTitle = "机甲护卫队里麻虎的座驾，拥有粒子屏蔽、点击重锤等技能。"
                animBean.detailsResId = R.drawable.ip_details_2
            } else {
                animBean.ipHeader = R.drawable.ip_5
                animBean.background = R.drawable.shape_yellow_radius
                animBean.backgroundColor = Color.parseColor("#FFC125")
                animBean.title = "鸡飞飞"
                animBean.subTitle = "与艾小坡互为穿越星际的“生命对应体”，他们可以用脑电波沟通。小坡学习知识、动脑破案，就可以给鸡飞飞积累思维能量。"
                animBean.detailsResId = R.drawable.ip_details_3
            }
            list.add(animBean)
        }
        return list
    }
}

class MainAdapter(private val list: MutableList<AnimBean>?) : RecyclerView.Adapter<MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_recyclerview_anim_item, parent, false)
        )
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val animBean = list?.get(position)
        animBean?.apply {
            holder.ipHeader?.setImageResource(ipHeader)
            holder.backgroundView?.setBackgroundResource(background)
            holder.title?.text = title ?: ""
            holder.tvSubtitle?.text = subTitle ?: ""

            holder.itemView.setOnClickListener {
                val i = Intent(holder.itemView.context, DetailsActivity::class.java)
                i.putExtra("background", background)
                i.putExtra("backgroundColor", backgroundColor)
                i.putExtra("ipHeader", ipHeader)
                i.putExtra("title", title)
                i.putExtra("detailsResId", detailsResId)
                val pairIpHeader: androidx.core.util.Pair<View, String> =
                    androidx.core.util.Pair(holder.ipHeader, "ipHeader")
                val pairBackground: androidx.core.util.Pair<View, String> =
                    androidx.core.util.Pair(holder.backgroundView, "background")
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as AppCompatActivity,
                        pairBackground, pairIpHeader
                    )
                startActivity(holder.itemView.context, i, optionsCompat.toBundle())
            }
        }
    }

    override fun onViewAttachedToWindow(holder: MainHolder) {
        super.onViewAttachedToWindow(holder)
//        holder.itemView.clearAnimation()
        val animZ = RotateAnimationZ()
        animZ.duration = 800
        animZ.isZhengfangxiang = true
        animZ.interpolator = AccelerateDecelerateInterpolator()
        holder.itemView.startAnimation(animZ)
    }
}

class MainHolder(itemView: View) : ViewHolder(itemView) {
    val backgroundView: FrameLayout? = itemView.findViewById(R.id.fl_background)
    val ipHeader: AppCompatImageView? = itemView.findViewById(R.id.iv_ip)
    val title: AppCompatTextView? = itemView.findViewById(R.id.tv_title)
    val tvSubtitle: AppCompatTextView? = itemView.findViewById(R.id.tv_subtitle)
}