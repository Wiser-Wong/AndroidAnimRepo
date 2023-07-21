package com.wiser.animationlistdemo.menu


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.databinding.MenuLayBinding
import kotlin.math.*


/**
 * @date: 2023/4/7 09:31
 * @author: jiaruihua
 * @desc :
 *
 */
class MenuAct : AppCompatActivity() {

    val binding by lazy { MenuLayBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.mMenuIcon.setOnClickListener {
            toggle()

        }
        binding.root.setOnClickListener { toggle() }

        binding.mm1.alpha=0f


    }

    var isOpen = false

    fun toggle(){

            startAni(binding.m1, 200, 0,isOpen)
            startAni(binding.m2, 210, 1,isOpen)
            startAni(binding.m3, 220, 2,isOpen)

    }

    private fun menuAni(close: Boolean=false) {

        println("--------close=$close------isOpen=$isOpen")

        if (isOpen){
            val ro1 = ObjectAnimator.ofFloat(binding.mMenuIcon, "rotation", 45f, 0f).apply {
                duration = 250
                doOnStart {
                    binding.mMenuIcon.setImageResource(R.drawable.iconp3)
                }
                doOnEnd {
                    binding.mMenuIcon.setImageResource(R.drawable.iconp1)
                }
            }
            val ro2 = ObjectAnimator.ofFloat(binding.mMenuIcon, "alpha", 0.5f, 1f).apply {
                duration = 250

                doOnEnd {
                    isOpen=false
                }
            }

            AnimatorSet().apply {
                playSequentially(ro1,ro2)
            }.start()
            return
        }
        val ro1 = ObjectAnimator.ofFloat(binding.mMenuIcon, "rotation", 0f, 45f).apply {
            duration = 200
            doOnStart {
                binding.mMenuIcon.setImageResource(R.drawable.iconp1)

            }
            doOnEnd { binding.mMenuIcon.setImageResource(R.drawable.iconp3)  }
        }
        val ro2 = ObjectAnimator.ofFloat(binding.mMenuIcon, "alpha", 0.5f, 1f).apply {
            duration = 200
            doOnEnd {
                isOpen=true
            }
        }

        AnimatorSet().apply {
            playSequentially(ro1,ro2)
        }.start()
    }

    private fun startAni(
        imageView: View,
        delay: Long = 0,
        index: Int = 0,
        close: Boolean=false
    ) {
        imageView.postDelayed({
            when (index) {
                0 -> {
                    menuAni()
                    animate(imageView)
                }
                1 -> animate2(imageView)
                else -> {
                    animate3(imageView)
                }
            }

        }, delay)

    }

    val left = 0.8f

    val DURATION: Long =  600
    val DURATION_CLOSE: Long =  200
    val mInterpolator =  CustomBounceInterpolator(1f)
//    CustomBounceInterpolator(1f)
    val mInterpolatorClose = LinearInterpolator()

    fun animate(v: View, close: Boolean = false) {
        val radius = resources.getDimension(R.dimen.ksl_dp_100)
        if (isOpen){

            val ox = ObjectAnimator.ofFloat(v, "translationX", radius * 0.8f,0f, -radius * 0.8f, 0f)
            val oy = ObjectAnimator.ofFloat(v, "translationY", -radius * 0.5f,-radius, -radius * 0.25f,0f)
            val oi = ObjectAnimator.ofFloat(binding.mm1, "alpha", 1f, 0f)
            val obj = AnimatorSet().apply {
                interpolator = mInterpolatorClose
                duration = DURATION_CLOSE
                doOnEnd {
                }

            }
            obj.playTogether(ox, oy, oi)
            obj.start()

            return
        }

        val ox = ObjectAnimator.ofFloat(v, "translationX", 0f, -radius * 0.8f, 0f, radius * 0.8f)
        val oy = ObjectAnimator.ofFloat(v, "translationY", 0f, -radius * 0.25f, -radius, -radius * 0.5f)
        val oi = ObjectAnimator.ofFloat(binding.mm1, "alpha", 0f, 1f)
        val obj = AnimatorSet().apply {
            interpolator = mInterpolator
            duration = DURATION

        }
        obj.playTogether(ox, oy, oi)
        obj.start()

//
//        //创建SpringSystem对象
//        val springSystem = SpringSystem.create()
//        //添加到弹簧系统
//        val spring: Spring = springSystem.createSpring()
//        spring.currentValue = 1.0
//        //tension(拉力)值改成100(拉力值越小，晃动越慢)，friction(摩擦力)值改成1(摩擦力越小，弹动大小越明显)
//        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100.0, 1.0))
//
//        //与上面一样，回弹效果更明显，但速度慢
////        spring.setSpringConfig(new SpringConfig(100, 1));
//        spring.addListener(object : SimpleSpringListener() {
//            override fun onSpringUpdate(spring: Spring) {
//                val scale = 1f - spring.getCurrentValue() * 0.5f
//                imageView.setScaleX(scale)
//                imageView.setScaleY(scale)
//            }
//        })
//        spring.setEndValue(0.1)


    }

    fun animate2(v: View,close: Boolean = false) {
        val radius = resources.getDimension(R.dimen.ksl_dp_100)
        if (isOpen){


            val ox = ObjectAnimator.ofFloat(v, "translationX", 0f, -radius * 0.85f, 0f)
            val oy = ObjectAnimator.ofFloat(v, "translationY",  -radius, -radius * 0.333f,0f)

            val oi = ObjectAnimator.ofFloat(binding.mm2, "alpha", 0f, 1f)

            val obj = AnimatorSet().apply {
                interpolator = mInterpolatorClose
                duration = DURATION_CLOSE

            }


            obj.playTogether(ox, oy, oi)

            obj.start()
            return
        }


        val ox = ObjectAnimator.ofFloat(v, "translationX", 0f, -radius * 0.85f, 0f)
        val oy = ObjectAnimator.ofFloat(v, "translationY", 0f, -radius * 0.333f, -radius)

        val oi = ObjectAnimator.ofFloat(binding.mm2, "alpha", 0f, 1f)

        val obj = AnimatorSet().apply {
            interpolator = mInterpolator
            duration = DURATION

        }


        obj.playTogether(ox, oy, oi)

        obj.start()


    }



    fun animate3(v: View,close: Boolean = false) {
        val radius = resources.getDimension(R.dimen.ksl_dp_100)
        if (isOpen){
            val ox = ObjectAnimator.ofFloat(v, "translationX", -radius * 0.8f,-radius,0f  )
            val oy = ObjectAnimator.ofFloat(v, "translationY", -radius * 0.5f,-radius * 0.3f,0f)
            val oi = ObjectAnimator.ofFloat(binding.mm3, "alpha", 1f, 0f)

            val obj = AnimatorSet().apply {
                interpolator = mInterpolatorClose
                duration = DURATION_CLOSE

            }

            obj.playTogether(ox, oy, oi)
            obj.start()
            return
        }
        val ox = ObjectAnimator.ofFloat(v, "translationX", 0f, -radius, -radius * 0.8f)
        val oy = ObjectAnimator.ofFloat(v, "translationY", 0f, -radius * 0.3f, -radius * 0.5f)
        val oi = ObjectAnimator.ofFloat(binding.mm3, "alpha", 0f, 1f)

        val obj = AnimatorSet().apply {
            interpolator = mInterpolator
            duration = DURATION

        }

        obj.playTogether(ox, oy, oi)
        obj.start()


    }


}


/**
 *
 * @property bounceScale Float
 * @property amplitude Double 振幅
 * @property frequency Double 频率
 * @constructor
 */
class CustomBounceInterpolator(private val bounceScale: Float) : BounceInterpolator() {
    private val amplitude = 0.15 //振幅
    private val frequency = 10.0  //频率
    override fun getInterpolation(t: Float): Float {
        // 重写 getInterpolation 方法并使用自定义值修改弹跳大小


        // _b(t) = t * t * 8
        // bs(t) = _b(t) for t < 0.3535
        // bs(t) = _b(t - 0.54719) + 0.7 for t < 0.7408
        // bs(t) = _b(t - 0.8526) + 0.9 for t < 0.9644
        // bs(t) = _b(t - 1.0435) + 0.95 for t <= 1.0
        // b(t) = bs(t * 1.1226)

        return (-1 * Math.pow(Math.E, -t / amplitude) * Math.cos(frequency * t) + 1).toFloat()
    }


    fun bounce( t :Float):Float {
        return t * t * 8.0f;
    }
}