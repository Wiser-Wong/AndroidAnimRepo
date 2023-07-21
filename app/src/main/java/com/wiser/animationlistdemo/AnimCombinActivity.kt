package com.wiser.animationlistdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.bullfrog.particle.Particles
import com.bullfrog.particle.animation.ParticleAnimation
import com.bullfrog.particle.particle.configuration.Shape
import com.custom.custombasebezier.activity.MainActivity2
import com.example.bare.ParentLockActivity
import com.wiser.animationlistdemo.audio.AudioMainAct
import com.wiser.animationlistdemo.cardrotate.CardRotateActivity
import com.wiser.animationlistdemo.combin.CollectionUtil
import com.wiser.animationlistdemo.combin.DeviceUtil
import com.wiser.animationlistdemo.combin.DiscoverIndexUtil
import com.wiser.animationlistdemo.combin.NotifyUtil
import com.wiser.animationlistdemo.combin.adapter.DiscoverLeve1Adapter
import com.wiser.animationlistdemo.combin.adapter.DiscoverLevel2Adapter
import com.wiser.animationlistdemo.combin.adapter.DiscoverLevel2Adapter.TYPE_ITEM_CONTAINER
import com.wiser.animationlistdemo.combin.bean.DiscoverIndexResult
import com.wiser.animationlistdemo.combin.bean.DiscoverOper
import com.wiser.animationlistdemo.combin.decoration.DiscoverIndexLevel2Decoration
import com.wiser.animationlistdemo.combin.manager.TopSnappedLayoutManager
import com.wiser.animationlistdemo.detailspage.DetailsPageActivity
import com.wiser.animationlistdemo.home.HomeActivity
import com.wiser.animationlistdemo.list.ani.HomeAct
import com.wiser.animationlistdemo.loginanim.LoginPageActivity
import com.wiser.animationlistdemo.recyclerviewanim.RecyclerViewAnimActivity
import com.wiser.animationlistdemo.textanim.TextAnimActivity
import com.wiser.animationlistdemo.textanim.text.AnimTextView
import com.wiser.animationlistdemo.textanim.text.AnvilText
import com.wiser.animationlistdemo.textanim.text.BaseText
import com.wiser.animationlistdemo.textanim.text.OnTextAnimatorListener
import com.wiser.animationlistdemo.ticket.TicketAct
import kotlinx.android.synthetic.main.activity_text_anim.*
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.IOException

/**
 *
 * @Author qingyang
 * 邮箱：qingyang@ksjgs.com
 * 创建时间: 2023/6/13  15:13
 * 用途: 组合动画入口
 * **************************************
 */
class AnimCombinActivity: AppCompatActivity() {

    private val DEFAULT_INT = -1024 // 默认位置
    private var targetPosition: Int = -1024 // 右侧默认位置
    private var mCurrentLeftPos = 0 // 当前左侧位置
    private var halfHeight: Float = -1.0f // 屏幕一半高度


    private var mLevel1Adapter: DiscoverLeve1Adapter ?= null
    private var mLevel2Adapter: DiscoverLevel2Adapter ?= null
    private var mLevel1LayoutMgr: LinearLayoutManager ?= null
    private var mLevel2LayoutMgr: TopSnappedLayoutManager? = null

    private var mErvLevel1: RecyclerView? = null
    private var mErvLevel2: RecyclerView? = null
    private var mBottomBar: AnimatedBottomBar? = null

    private lateinit var opers: List<DiscoverOper>

    private var sentences = arrayOf(
        "口袋神探",
        "摩登大自然",
        "小猴玛尼",
        "凯叔红楼梦",
        "麦小米的100个烦恼",
        "长安喵探妙狸花",
        "神奇图书馆"
    )

    private var mCounter = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim_combin)
        initViewById()
        initContentView()
    }

    private fun initViewById() {
        mErvLevel1 = findViewById(R.id.ervLevel1)
        mErvLevel2 = findViewById(R.id.ervLevel2)
        mBottomBar = findViewById(R.id.bottom_bar)
    }

    private fun initContentView() {
        mBottomBar?.setOnTabSelectListener(object: AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?, newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                Toast.makeText(this@AnimCombinActivity, "$newIndex", Toast.LENGTH_SHORT).show()
            }

        })
        initLevel1Views()
        initLevel2Views()
        initlidateContent()

        tv_anim_text?.setTextAnimatorListener(object: OnTextAnimatorListener {
            override fun onStart(textView: BaseText?) {
                tv_anim_text?.removeCallbacks(runnable)
                tv_anim_text?.visibility = View.VISIBLE
//                if (textView is AnvilText) {
//                    Particles.with(this@AnimCombinActivity, fl_anim_text) // container 是粒子动画的宿主父 ViewGroup
//                        .colorFromDrawable(R.drawable.ic_close)// 从 button 中采样颜色
//                        .particleNum(200)// 一共 200 个粒子
//                        .anchor(tv_anim_text)// 把 button 作为动画的锚点
//                        .shape(Shape.PENTACLE)// 粒子形状是圆形
//                        .radius(2, 6)// 粒子随机半径 2~6
//                        .anim(ParticleAnimation.FIREWORK)// 使用爆炸动画
//                        .start()
//                }
            }

            override fun onEnd(textView: BaseText?) {
                tv_anim_text?.postDelayed(runnable,1000)
            }

        })
    }

    val runnable: Runnable = Runnable {
        tv_anim_text?.visibility = View.GONE
    }

    private fun initlidateContent() {
        try {
            val jsonData: String? = getAssetsData("discover.json")
            val discoverIndexResult: DiscoverIndexResult = JSON.parseObject(jsonData, DiscoverIndexResult::class.java)
            opers = discoverIndexResult.discoverList
            discoverIndexResult.localLevel2List = DiscoverIndexUtil.getOperLevel2ListByParentId(opers, 1000)
            if (CollectionUtil.isEmpty(discoverIndexResult.discoverList) && CollectionUtil.isEmpty(discoverIndexResult.localLevel2List)) return
            invalidateLevel1View(discoverIndexResult.discoverList)
            scrollListItemLevel1ViewToCenter(0)
            invalidateLevel2View(discoverIndexResult.localLevel2List)
        } catch (ex: JSONException) {
            ex.printStackTrace()
        }
    }

    private fun invalidateLevel1View(discoverList: List<DiscoverOper>) {
        mLevel1Adapter?.data = discoverList
        mLevel1Adapter?.notifyDataSetChanged()
    }

    private fun invalidateLevel2View(localLevel2List: List<DiscoverOper>) {
        mLevel2Adapter?.data = localLevel2List
        mLevel2Adapter?.notifyDataSetChanged()
    }

    /**
     * 一级类目
     */
    private fun initLevel1Views() {
        mLevel1Adapter = DiscoverLeve1Adapter()
        mLevel1Adapter?.setOnExRvItemViewClickListener{view, dataPos ->
            onListItemLeve1ViewClick(dataPos)
        }
        mLevel1LayoutMgr = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mErvLevel1?.layoutManager = mLevel1LayoutMgr
        mErvLevel1?.adapter = mLevel1Adapter
        val vglp: ViewGroup.LayoutParams? = mErvLevel1?.layoutParams
        vglp?.width = (DeviceUtil.getScreenWidth() * 0.25).toInt()
    }

    private fun onListItemLeve1ViewClick(dataPos: Int) {
        // 选中列表Postition位置元素 并居中
        scrollListItemLevel1ViewToCenter(dataPos)

        val item = mLevel1Adapter?.getDataItem(dataPos) ?: return;
        val itemList  = DiscoverIndexUtil.getOperLevel2ListByParentId(opers, item.element_id)
        invalidateLevel2View(itemList)
    }

    private fun level2ListScrollToPosition(dataPos: Int) {
        val obj: Any = mLevel1Adapter!!.getDataItem(dataPos)
        if (obj is DiscoverOper) {
            targetPosition = mLevel2Adapter?.getSelectPosition(obj.element_id) ?: -1
            if (targetPosition > -1) mErvLevel2?.smoothScrollToPosition(targetPosition)
        }
    }

    /**
     * 二级类目
     */
    private fun initLevel2Views() {
        mLevel2Adapter = DiscoverLevel2Adapter()
        mLevel2Adapter?.setOnExRvItemViewClickListener{ view, dataPos ->
            onRvItemLevel2ViewClick(dataPos)
        }
        mLevel2LayoutMgr = TopSnappedLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        mLevel2LayoutMgr?.recycleChildrenOnDetach = true
        mLevel2LayoutMgr?.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(dataPos: Int): Int {
                val type: Int? = mLevel2Adapter?.getDataItemViewType(dataPos)
                return when (type) {
                    DiscoverLevel2Adapter.TYPE_ITEM_BANNER, DiscoverLevel2Adapter.TYPE_ITEM_TITLE -> 2
                    DiscoverLevel2Adapter.TYPE_ITEM_CONTAINER -> 1
                    else -> 1
                }
            }
        }
        mErvLevel2?.layoutManager = mLevel2LayoutMgr
        mErvLevel2?.setItemViewCacheSize(10)
        mErvLevel2?.addItemDecoration(DiscoverIndexLevel2Decoration())
        mErvLevel2?.adapter = mLevel2Adapter
        mErvLevel2?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) targetPosition = DEFAULT_INT
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (targetPosition == DEFAULT_INT) setLeftSelection()
            }
        })
        val recycledViewPool: RecycledViewPool? = mErvLevel2?.getRecycledViewPool()
        recycledViewPool?.setMaxRecycledViews(TYPE_ITEM_CONTAINER, 20)
        val vglp: ViewGroup.LayoutParams? = mErvLevel2?.layoutParams
        vglp?.width = (DeviceUtil.getScreenWidth() * 0.75).toInt()
    }

    private fun onRvItemLevel2ViewClick(dataPos: Int) {
        val item = mLevel2Adapter?.getDataItem(dataPos)

        if (item is DiscoverOper) {
            when (item.element_id) {
                //登录动画
                1001 -> startActivity(Intent(this, LoginPageActivity::class.java))
                //IP列表
                2001 -> startActivity(Intent(this, RecyclerViewAnimActivity::class.java))
                //频道列表
                2002 -> startActivity(Intent(this, HomeActivity::class.java))
                //播放器列表
                2003 -> startActivity(Intent(this, AudioMainAct::class.java))
                //播放器列表
                2004 -> startActivity(Intent(this, HomeAct::class.java))
                //3D详情页
                3001 -> startActivity(Intent(this, CardRotateActivity::class.java))
                //IP详情页
                3002 -> startActivity(Intent(this, RecyclerViewAnimActivity::class.java))
                //多级列表详情页 @xiangyu
                3003 -> startActivity(Intent(this, DetailsPageActivity::class.java))
                //图卡分享
                4001 -> startActivity(Intent(this, TicketAct::class.java))
                //组合专辑
                4002 -> startActivity(Intent(this, CardRotateActivity::class.java))
                //特效播放器
                5001 -> startActivity(Intent(this, AudioMainAct::class.java))
                //全屏播放
                5002 -> startActivity(Intent(this, AudioMainAct::class.java))
                //小屏播放
                5003 -> startActivity(Intent(this, AudioMainAct::class.java))
                //播放器故事详情
                5004 -> startActivity(Intent(this, AudioMainAct::class.java))
                //家长锁
                6001 -> startActivity(Intent(this, ParentLockActivity::class.java))
                //叫早哄睡
                6003 -> startActivity(Intent(this, MainActivity2::class.java))
                //通知动画1
                7001 -> NotifyUtil.notifyStandardAnimClick(this)
                //通知动画2
                7002 -> NotifyUtil.notifySlideAnimClick(this)
                //通知动画3
                7003 -> NotifyUtil.notifySlideInAnimClick(this)
                //通知动画4
                7004 -> NotifyUtil.notifySlideOnTopAnimClick(this)
                //通知动画5
                7005 -> NotifyUtil.notifyFlipAnimClick(this)
                //通知动画6
                7006 -> NotifyUtil.notifyJellyAnimClick(this)
                //通知动画7
                7007 -> NotifyUtil.notifyScaleAnimClick(this)
                //粒子动效
                8001 -> clickAnvil()
                //火花动效
                8002 -> clickSparkle()
                //高斯模糊
                8003 -> clickPixelate()
                //文字掉落
                8004 -> clickFall()
                //线性缩放
                8005 -> clickScale()
                //
                8006 -> clickEvaporate()
                //白花动效
                8007 -> clickBurn()
                //跑马灯
                8008 -> clickRainBow()
                //打印
                8009 -> clickPrint()
                //组合
                8010 -> startActivity(Intent(this, TextAnimActivity::class.java))
            }
        }
    }

    private fun setLeftSelection() {
        try {
            var leftPos: Int = setLeftPosition()
            if (leftPos == -1) return
            if (!mErvLevel2!!.canScrollVertically(1)) leftPos = mLevel1Adapter!!.dataLastItemPosition
            if (mLevel1LayoutMgr!!.findFirstVisibleItemPosition() > leftPos) mErvLevel1?.smoothScrollToPosition(leftPos) else if (mLevel1LayoutMgr!!.findLastVisibleItemPosition() < leftPos) mErvLevel1!!.smoothScrollToPosition(
                leftPos
            )
            if (mCurrentLeftPos != leftPos) {

                // 居中滚动
                scrollListItemLevel1ViewToCenter(leftPos)
                mCurrentLeftPos = leftPos
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun scrollListItemLevel1ViewToCenter(position: Int) {
        try {
            val childAt = mErvLevel1!!.getChildAt(position - mLevel1LayoutMgr!!.findFirstVisibleItemPosition())
            if (childAt != null) {
                val y = childAt.top - mErvLevel1!!.height / 2
                mErvLevel1!!.smoothScrollBy(0, y)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            mLevel1Adapter!!.setSelectPos(position)
        }
    }

    private fun setLeftPosition(): Int {
        val fristPosition = mLevel2LayoutMgr?.findFirstVisibleItemPosition() ?: 1
        val lastPosition = mLevel2LayoutMgr?.findLastVisibleItemPosition() ?: 1

        var halfH = -1.0f
        if (halfHeight == -1.0f) {
            halfH = mErvLevel2?.height?.toFloat() ?: 1 * 0.5f
        }

        for (i in fristPosition..lastPosition) {
            val obj = mLevel2Adapter?.getDataItem(i)
            if (obj is DiscoverOper) {
                val discoverOper: DiscoverOper = obj
                if (discoverOper.isTypeTitle) {
                    val currentTop = mErvLevel2?.getChildAt(i - fristPosition)?.top?.toFloat() ?: -1f

                    if (currentTop > halfH) { //小于屏幕一半  切换 Select
                        return discoverOper.parentPosition - 1
                    } else {
                        return discoverOper.parentPosition
                    }
                }
            }
        }
        return -1
    }

    /**
     * 加载资源文件
     *
     * @param path
     * @return
     */
    private fun getAssetsData(path: String): String? {
        var result: String? = ""
        return try {
            //获取输入流
            val mAssets = assets.open(path)
            //获取文件的字节数
            val lenght = mAssets.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer)
            mAssets.close()
            result = String(buffer)
            result
        } catch (e: IOException) {
            e.printStackTrace()
            result
        }
    }

    fun clickAnvil() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.ANVIL)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickSparkle() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.SPARKLE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickPixelate() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.PIXELATE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickFall() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.FALL)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickScale() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.SCALE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickEvaporate() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.EVAPORATE)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickBurn() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.BURN)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickRainBow() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.RAINBOW)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }

    fun clickPrint() {
        mCounter = if (mCounter >= sentences.size - 1) 0 else mCounter + 1
        tv_anim_text?.setAnimType(AnimTextView.AnimType.PRINT)
        tv_anim_text?.setTextAnim(sentences[mCounter])
    }
}