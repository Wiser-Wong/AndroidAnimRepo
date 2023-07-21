package com.wiser.animationlistdemo.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.databinding.CardDetailThirdBinding

/**
 * @date: 2023/5/11 16:28
 * @author: jiaruihua
 * @desc :
 *
 */
class CardDetail3 : AppCompatActivity() {
    var binding: CardDetailThirdBinding? = null
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
        window.sharedElementExitTransition = AutoTransition()
        binding = CardDetailThirdBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        binding?.coverIv?.doOnPreDraw {
            supportStartPostponedEnterTransition()

        }
        val rotation = intent.getFloatExtra("rotation", 0f)
        binding?.cardBg?.post {
        binding?.coverIv?.setImageResource(intent.getIntExtra("cover", R.drawable.a))


            binding?.cardBg?.let {
                it.setBackgroundColor(intent.getIntExtra("bgColor", Color.parseColor("#cce3c6")))

                val aniReveal = ViewAnimationUtils.createCircularReveal(
                    it,
                    screenWith.toInt(),
                    0,
                    0f,
                    screenHeight
                ).apply {

                    duration = 800
                    interpolator = LinearInterpolator()

                }

                aniReveal.start()

            }
        }


        binding?.back?.setOnClickListener { onBackPressed() }


        binding?.content?.let {
            it.alpha = 1f
            var beginX = binding?.coverIv?.x?:0f
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


//        setEnterSharedElementCallback(ShareCall())

    }
    override fun onBackPressed() {
        if (totalScrollY>0){
            binding?.appBar?.setExpanded(true,true)
            binding?.content?.smoothScrollTo(0,0)
//            super.onBackPressed()
            return
        }
        super.onBackPressed()
//        finish()
    }


}
