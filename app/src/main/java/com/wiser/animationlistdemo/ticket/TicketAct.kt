package com.wiser.animationlistdemo.ticket

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.wiser.animationlistdemo.databinding.ActTicketBinding

/**
 * @date: 2023/5/15 11:07
 * @author: jiaruihua
 * @desc :
 *
 */
class TicketAct: AppCompatActivity() {
    private var binding:ActTicketBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActTicketBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.stackView?.let {
            it.setDataAdapter(TicketAdapter(this@TicketAct,it))
        }

        binding?.title?.let {

            ObjectAnimator.ofFloat(it,"alpha",0f,1f).apply {
                duration = 2500
                interpolator = DecelerateInterpolator()
            }.start()
        }

    }
}