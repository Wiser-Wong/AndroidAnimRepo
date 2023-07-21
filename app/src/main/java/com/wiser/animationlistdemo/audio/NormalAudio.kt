package com.wiser.animationlistdemo.audio

import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.wiser.animationlistdemo.databinding.ActNormalBinding

/**
 * @date: 2023/6/9 14:35
 * @author: jiaruihua
 * @desc :
 *
 */
class NormalAudio: AppCompatActivity() {
    private val binding by lazy { ActNormalBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val src = intent.getIntExtra("src",0)

        val bitmap = BitmapFactory.decodeResource(resources, src)

        val palette = Palette.from(bitmap).generate()

        val vibrantSwatch = palette.lightVibrantSwatch

        val mainColor = vibrantSwatch?.rgb ?: Color.parseColor("#450099")

        binding.root.setBackgroundColor(mainColor)

        binding.cover.setBackgroundResource(src)

        ObjectAnimator.ofFloat(binding.root,"alpha",0.3f,1f).apply {
            duration = 500
          start()
        }

        binding.back.setOnClickListener { onBackPressed() }

    }
}