package com.wiser.animationlistdemo.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.Explode
import android.transition.Transition
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.animation.ValueAnimator
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.databinding.CardDetailFourBinding

/**
 * @date: 2023/5/11 16:28
 * @author: jiaruihua
 * @desc :
 *
 */
class CardDetail4 : AppCompatActivity() {
    val binding: CardDetailFourBinding by lazy {
        CardDetailFourBinding.inflate(
            LayoutInflater.from(
                this
            )
        )
    }
    val screenHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }
    val screenWith by lazy { resources.displayMetrics.widthPixels.toFloat() }

    /**
     * 手指触摸屏幕动画
     */
    var downScaleOver = true
    private var mDownX = 0f
    private var mDownY = 0f
    private var mPointerId = 0
    var totalScrollY = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        setContentView(binding?.root)

        window.sharedElementExitTransition = AutoTransition()

//        val changeTransform = ChangeTransform()
////        changeTransform.addTarget(binding.coverIv)
//
//        val changeImageTransform = CustomChangeTransform()
//        changeImageTransform.addTarget(binding.coverIv)

////        val cus = RotationTransition()
////        cus.addTarget(binding.coverIv)

//        val transitions = TransitionSet()
//        val t: Transition =
//            transitions.addTransition(changeImageTransform)
//                .addTransition(changeImageTransform)
//                .addTransition(cus)
//
//        window.sharedElementEnterTransition = t

//        val rotation = intent.getFloatExtra("rotation", 0f)
//        ViewCompat.animate(binding.coverIv)
//            .rotation(rotation)
//            .setDuration(1000)
//            .setInterpolator(AccelerateDecelerateInterpolator())
//            .start()

        binding?.cardBg?.post {
            binding?.cardBg?.let {
                it.setBackgroundColor(intent.getIntExtra("bgColor", Color.parseColor("#cce3c6")))

                val aniReveal = ViewAnimationUtils.createCircularReveal(
                    it,
                    screenWith.toInt(),
                    0,
                    0f,
                    screenHeight
                ).apply {

                    duration = 500
                    interpolator = LinearInterpolator()

                }

                aniReveal.start()

            }
        }


        binding.coverIv.post{

//            binding?.coverIv?.doOnPreDraw {
                supportStartPostponedEnterTransition()

                binding?.coverIv?.setImageResource(intent.getIntExtra("cover", R.drawable.a))

//            }
            binding.coverIv.run {
                val dw = drawable.intrinsicWidth
                val dh = drawable.intrinsicHeight

                val sw = width.toFloat()/dw
                val sh = height.toFloat()/dh

                val m = Matrix().apply {
                    setScale(sw,sh)
                }
                imageMatrix = m
                invalidate()

            }
            ObjectAnimator.ofFloat(binding.coverIv,"alpha",0f,1f).apply {
                duration=200
                start()
            }
        }




        binding?.content?.findViewById<View>(R.id.cTitle)?.visibility = View.GONE

        binding?.back?.setOnClickListener { onBackPressed() }


        setSupportActionBar(binding?.tb)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding?.tb?.setNavigationOnClickListener { onBackPressed() }


        binding?.colToolbarLayout?.run {
            title = "将进酒"
            setExpandedTitleColor(Color.parseColor("#ffffff"))
            setCollapsedTitleTextColor(Color.parseColor("#ffffff"))
        }


        ValueAnimator.ofFloat(0F, resources.getDimension(R.dimen.ksl_dp_95)).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                binding?.colToolbarLayout?.setExpandedTitleMargin(
                    resources.getDimension(R.dimen.ksl_dp_45).toInt(), 0, 0,
                    (it.animatedValue as Float).toInt()
                )
            }
            start()
        }



        binding?.content?.let {
            it.alpha = 1f
            var beginX = binding?.coverIv?.x ?: 0f
            transY(it, screenHeight * 2, 0f, stiffness = 45f)

            it.updateLayoutParams {
                height = screenHeight.toInt()
            }



            it.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                totalScrollY +=scrollY-oldScrollY
                binding?.coverIv?.rotation = scrollY.toFloat() / 15

                println("beginX--$beginX")
                binding?.coverIv?.translationX = beginX - resources.getDimension(R.dimen.ksl_dp_15)

            }


        }


        setEnterSharedElementCallback(ShareCall())

    }
    override fun onBackPressed() {
        if (totalScrollY>0){
            binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (verticalOffset==0){
                    super.onBackPressed()
                }
            }
            binding.appBar.setExpanded(true,true)
            binding?.content?.smoothScrollTo(0,0)

            return
        }
        super.onBackPressed()
    }


}
