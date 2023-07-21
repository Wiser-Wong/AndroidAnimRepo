package com.wiser.animationlistdemo.ticket

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.scale
import androidx.core.text.underline
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.databinding.ShareDetailBinding
import com.wiser.animationlistdemo.home.DataSource
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @date: 2023/6/14 16:59
 * @author: jiaruihua
 * @desc :
 *
 */
class ShareAct : AppCompatActivity() {

    private val binding by lazy { ShareDetailBinding.inflate(layoutInflater) }
    private val adapter by lazy { CardStackAdapter() }
    private val cardStackView by lazy { binding.tickets }
    private val manager by lazy {
        CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
                val card = DataSource.getCards().get(position)
                view?.findViewById<TextView>(R.id.desc)?.let { tvDesc ->
                    val text = tvDesc.text
                    tvDesc.textSize = 18F
                    tvDesc.setTextColor(Color.parseColor("#4a4a4a"))
                    //使用一个CharSequence包装文本，实现一个字符一个字符的动画效果
                    val cs = SpannableStringBuilder()

                    val startIndex = text.indexOf(card.title)
                    val endIndex = startIndex+card.title.length

                    text.forEachIndexed { i, c ->
                        MainScope().launch {
                            delay(i * 100L)
                            if (card.title.contains("$c")&&(i in startIndex .. endIndex)) {
                                cs.bold {
                                    color(ContextCompat.getColor(this@ShareAct,R.color.color_13be58)) {
                                        scale(1.3f) {
                                            underline { cs.append("$c") }

                                        }
                                    }

                                }
                            } else {
                                cs.append("$c")
                            }

                            tvDesc.setText(cs, TextView.BufferType.NORMAL)
                        }
                    }
                }

                view?.findViewById<ImageView>(R.id.ivBottom)?.let {
                    val height = resources.displayMetrics.heightPixels // 获取屏幕高度
//                    val anim = SpringAnimation(it, DynamicAnimation.TRANSLATION_Y)
//                    val spring = SpringForce()
////                    anim.setMaxValue(-resources.getDimension(R.dimen.ksl_dp_60))
////                    anim.setMinValue(-resources.getDimension(R.dimen.ksl_dp_160))
////                    anim.setStartValue(-resources.getDimension(R.dimen.ksl_dp_160))
////                    anim.animateToFinalPosition(-resources.getDimension(R.dimen.ksl_dp_60))
//                    anim.animateToFinalPosition(height.toFloat())
//                    spring.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
//                    spring.stiffness = SpringForce.STIFFNESS_LOW
//                    anim.spring = spring
//                    anim.start()

                    ObjectAnimator.ofFloat(it,"translationY",-resources.getDimension(R.dimen.ksl_dp_120),-resources.getDimension(R.dimen.ksl_dp_60)).apply {
                        duration = 600
                        interpolator = BounceInterpolator()
                        start()
                    }
                }

            }

            override fun onCardDisappeared(view: View?, position: Int) {


                if (position >DataSource.getCards().size - 2) {
                    op()
                }
            }

        })
    }

    fun op() {
        manager.cardStackSetting.canScrollHorizontal = false
        manager.cardStackSetting.canScrollVertical = false
        manager.cardStackSetting.swipeableMethod = SwipeableMethod.None
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding?.ivback?.setOnClickListener { finish() }


        val h = resources.displayMetrics.heightPixels * 1.5
        binding?.wx?.translationY = h.toFloat()
        binding?.py?.translationY = h.toFloat()
        lifecycleScope.launch {
            delay(600)


            binding?.wx?.let {


                ObjectAnimator.ofFloat(it, "translationY", h.toFloat(), 0f).apply {
                    duration = 500
                    interpolator = OvershootInterpolator(0.7f)
                    start()
                }

            }
        }
        lifecycleScope.launch {
            delay(800)
            binding?.py?.let {

                ObjectAnimator.ofFloat(it, "translationY", h.toFloat(), 0f).apply {
                    duration = 500
                    interpolator = OvershootInterpolator(0.7f)
                    start()
                }

            }
        }

        initialize()
    }


    private fun initialize() {
        manager.setStackFrom(StackFrom.Left)
        manager.setVisibleCount(5)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }
}