package com.wiser.animationlistdemo.audio

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wiser.animationlistdemo.databinding.ActFullBinding

/**
 * @date: 2023/6/8 17:44
 * @author: jiaruihua
 * @desc :
 *
 */
class FullScreenAudio : AppCompatActivity() {

    val binding: ActFullBinding by lazy { ActFullBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        setContentView(binding.root)
        val src = intent.getIntExtra("src",0)


        val img = "https://cdn.kaishuhezi.com/kstory/androidapkdir/image/a8c4a84c-8ca5-4270-85ee-33af555e0124_info_w=750_h=750_s=725107.png"

//        binding.ivCover.setImageResource(src)
        binding.ivCover.doOnPreDraw {
            Glide.with(binding.ivCover).load(img)
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        binding.ivCover.setImageDrawable(resource)
                        supportStartPostponedEnterTransition()

                        return false
                    }

                })
                .into(binding.ivCover)

        }



        binding.back.setOnClickListener { onBackPressed() }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.ivCover.destory()
    }
}