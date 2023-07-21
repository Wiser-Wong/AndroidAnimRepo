package com.wiser.animationlistdemo.ticket

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_HIGH
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_LOW
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_MEDIUM
import androidx.lifecycle.lifecycleScope
import com.wiser.animationlistdemo.audio.transY
import com.wiser.animationlistdemo.databinding.TicketDetailBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @date: 2023/5/17 16:41
 * @author: jiaruihua
 * @desc :
 *
 */
class TicketDetail : AppCompatActivity()  {

    var binding:TicketDetailBinding ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TicketDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tickets?.updateTitle(intent.getStringExtra("title")?:"")
        binding?.ivback?.setOnClickListener { finish() }


        val h = resources.displayMetrics.heightPixels*1.5
        binding?.wx?.translationY = h.toFloat()
        binding?.py?.translationY = h.toFloat()
        lifecycleScope.launch {
            delay(600)


            binding?.wx?.let {



                ObjectAnimator.ofFloat(it,"translationY",h.toFloat(),0f).apply {
                    duration = 500
                    interpolator = OvershootInterpolator(0.7f)
                    start()
                }

            }
        }
        lifecycleScope.launch {
            delay(800)
            binding?.py?.let {

                ObjectAnimator.ofFloat(it,"translationY",h.toFloat(),0f).apply {
                    duration = 500
                    interpolator = OvershootInterpolator(0.7f)
                    start()
                }

            }
        }

    }
}