package com.wiser.animationlistdemo.home

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.graphics.drawable.toDrawable
import androidx.core.transition.addListener
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wiser.animationlistdemo.audio.rotationCenter
import com.wiser.animationlistdemo.audio.rotationY
import com.wiser.animationlistdemo.audio.scale
import com.wiser.animationlistdemo.audio.transANI
import com.wiser.animationlistdemo.databinding.ActivityHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @date: 2023/5/9 18:41
 * @author: jiaruihua
 * @desc :
 *
 */
class HomeActivity : AppCompatActivity() {

    val screenHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }
    val screenWith by lazy { resources.displayMetrics.widthPixels.toFloat() }
    var binding: ActivityHomeBinding? = null
    var scrollX = 0

    //大于 0 手指 左滑  小于0 右滑
    var dix = 0


    /**
     * 背景旋转动画是否结束
     */
    var isBgAniOver = true

    /**
     * 手指触摸屏幕动画
     */
    var downScaleOver = true


    private val imageAdapter by lazy {
        ImageAdapter(onItemClick = ::onItemClick, skip = ::onSkip)
    }
    var lastColor: Int = imageAdapter.dataSource.first().bgColor

    fun onItemClick() {
        binding?.bgView?.let {
            scale(it, 0.95f) {
                scale(it, 1f) {

                }
            }
        }
    }


    fun onSkip(v: View, v2: View, src: Int, position: Int) {


        var transName = "cover"


        val skipClass = when (position % 3) {
            0 -> {
                transName = "cover3"
                CardDetail4::class.java
            }

            1 -> {
                transName = "cover3"
                CardDetail3::class.java
            }

            else -> CardDetail2::class.java
        }

        v.transitionName = transName

        val cop = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@HomeActivity,
            Pair(v, v.transitionName)
        )


////        rotationCenter(v,v.rotation,v.rotation+15,stiffness=15f)
        startActivity(Intent(this@HomeActivity, skipClass).apply {
            putExtra("cover", src)
            putExtra("bgColor", imageAdapter.dataSource.get(position).bgColor)
            putExtra("rotation", v.rotation)
        }, cop.toBundle())

        lifecycleScope.launch {
            delay(300)
            imageAdapter.reset()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        window.reenterTransition = Explode().apply { duration=600 }
//        window.exitTransition = Explode().apply { duration=500 }
        transANI(window, isOpenExit = false)

        binding?.vtest?.let { view ->
            ObjectAnimator.ofArgb(view, "backgroundColor", Color.parseColor("#ffffff"), lastColor)
                .apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    doOnEnd {
                        lastColor = imageAdapter.dataSource.first().bgColor
                    }
                }.start()
        }

        binding?.tablayout?.run {
            addTab(newTab().setText("推荐"))
            addTab(newTab().setText("故事"))
            addTab(newTab().setText("听书"))
            addTab(newTab().setText("国学"))
            addTab(newTab().setText("诗词"))
            val smoothScroller = SmoothScroller(context)
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    smoothScroller.targetPosition = tab?.position ?: 0
                    binding?.rcList?.layoutManager?.startSmoothScroll(smoothScroller)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

        }





        binding?.rcList?.let { rc ->
            rc.layoutManager =
                LinearLayoutManager(rc.context, LinearLayoutManager.HORIZONTAL, false)
            rc.adapter = imageAdapter


            rc.addOnScrollListener(object : OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    println("aaaaaaaaa-----------newState=$newState")
                    when (newState) {
                        SCROLL_STATE_IDLE -> {
                            //停止
                            isBgAniOver = true

                            changeBgColor(rc)


                        }

                        SCROLL_STATE_DRAGGING -> {
                            //正在 滑动
                            if (downScaleOver) {
                                binding?.bgView?.let {
                                    downScaleOver = false
                                    scale(it, 0.98f) {
                                        scale(it, 1f) {

                                            downScaleOver = true
                                        }
                                    }
                                }
                            }

                        }

                        SCROLL_STATE_SETTLING -> {
                            // 手指离开 到 停止

                        }
                    }
                }


                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    startBgAni(dx)

                    imgeRotation(dx)

                }
            })


        }


    }

    private fun changeBgColor(rc: RecyclerView) {
        rc.layoutManager?.let {

            if (it is LinearLayoutManager) {

                val firstPosition = it.findFirstVisibleItemPosition()
                val lastPosition = it.findLastVisibleItemPosition()

                var tempW = 0
                var tempPosition = 0

                for (i in firstPosition..lastPosition) {
                    it.findViewByPosition(i)?.let { v ->
                        val rect = Rect()
                        v.getGlobalVisibleRect(rect)

                        if (rect.width() > tempW) {
                            tempPosition = i
                            tempW = rect.width()
                        }
                    }
                }


                val imageData = imageAdapter.dataSource.get(tempPosition)

                binding?.vtest?.let { view ->
                    ObjectAnimator.ofArgb(view, "backgroundColor", lastColor, imageData.bgColor)
                        .apply {
                            duration = 1000
                            interpolator = AccelerateDecelerateInterpolator()
                            doOnEnd {
                                lastColor = imageData.bgColor
                            }
                        }.start()
                }

            }

        }
    }


    private fun startBgAni(dx: Int) {
        if (isBgAniOver) {
            dix = dx

            if (dix < 0) {
                binding?.bgView?.let {
                    isBgAniOver = false
                    rotationY(it, -3f) {
                        rotationY(it, 0f) {

                        }
                    }

                }
            }

            if (dix > 0) {
                isBgAniOver = false
                binding?.bgView?.let {

                    rotationY(it, 3f) {
                        rotationY(it, 0f) { }
                    }

                }
            }
        }
    }

    private fun imgeRotation(dx: Int) {
        scrollX += dx

        imageAdapter.picViews.filter {
            it.isAttachedToWindow
        }.onEach {
            it.rotation = scrollX / 10f
        }
    }


}