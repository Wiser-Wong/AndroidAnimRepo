package com.wiser.animationlistdemo.niftynotification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.niftynotification.api.Configuration
import com.wiser.animationlistdemo.niftynotification.api.Effects
import com.wiser.animationlistdemo.niftynotification.api.NiftyNotificationView

class NotifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
    }

    fun notifyStandardAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.standard,
            R.id.notify_rl,
            Configuration.Builder().setBackgroundColor("#00fff0").setTextColor("#ff00ff").build()
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifySlideAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.thumbSlider,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifySlideInAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.slideIn,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifySlideOnTopAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.slideOnTop,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifyFlipAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.flip,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifyJellyAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.jelly,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }

    fun notifyScaleAnimClick(view: View) {
        NiftyNotificationView.build(
            this,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.scale,
            R.id.notify_rl
        )
            .setIcon(R.drawable.notify_bg)
            .show()
    }
}