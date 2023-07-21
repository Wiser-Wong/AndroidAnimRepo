package com.wiser.animationlistdemo.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.audio.scale
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.databinding.CardDetailSecondBinding

/**
 * @date: 2023/5/11 16:28
 * @author: jiaruihua
 * @desc :
 *
 */
class CardDetail2 : AppCompatActivity() {
    var binding: CardDetailSecondBinding? = null
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
        window.sharedElementExitTransition = AutoTransition()
        binding = CardDetailSecondBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        val rotation = intent.getFloatExtra("rotation", 0f)
        binding?.coverIv?.setImageResource(intent.getIntExtra("cover", R.drawable.a))
        binding?.coverIv?.let{
            it.translationZ = resources.getDimension(R.dimen.ksl_dp_30)
        }
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

                    duration = 800
                    interpolator = LinearInterpolator()

                }

                aniReveal.start()

            }
        }

        setEnterSharedElementCallback(ShareCall())
        binding?.back?.setOnClickListener { onBackPressed() }

        binding?.content?.let {
//            it.translationY = screenHeight
            it.alpha=1f
            transY(it, screenHeight*2, 0f, stiffness = 45f)

            it.updateLayoutParams {
                height = screenHeight.toInt()
            }





            it.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                totalScrollY +=scrollY-oldScrollY

                println("-----------totalScrollY=$totalScrollY")
//                if (downScaleOver) {
//                    binding?.coverIv?.let {
//                        downScaleOver = false
//                        scale(it, 0.98f) {
//                            scale(it, 1f) {
//                                it.postDelayed({ downScaleOver = true},100)
//
//                            }
//                        }
//                    }
//                }

                binding?.coverIv?.rotation = scrollY.toFloat()/15

            }



        }




    }


    override fun onBackPressed() {
//        if (totalScrollY>0){
//            binding?.content?.smoothScrollTo(0,0)
//        }
        super.onBackPressed()
//        finish()
    }

}
