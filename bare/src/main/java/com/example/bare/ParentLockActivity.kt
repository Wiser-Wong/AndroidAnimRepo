package com.example.bare

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import pl.droidsonroids.gif.GifImageView

class ParentLockActivity : AppCompatActivity() {
    private val pic1: GifImageView by lazy { findViewById<GifImageView>(R.id.pic1) }
    private val pic2: GifImageView by lazy { findViewById<GifImageView>(R.id.pic2) }
    private val pic3: GifImageView by lazy { findViewById<GifImageView>(R.id.pic3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.allowEnterTransitionOverlap = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_parent_lock)
        pic1.setImageResource(R.raw.start1)
        pic2.setImageResource(R.raw.icon1)
        pic3.setImageResource(R.raw.icon2)
        pic1.setOnClickListener {
            pic1.setImageResource(R.raw.start2)
            Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(this,MainActivity2::class.java)
                val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    android.util.Pair.create(pic1, "root_image"),

                    )
                startActivity(i, options.toBundle())
                pic1.setImageResource(R.raw.start1)
            },900)

        }
    }
}