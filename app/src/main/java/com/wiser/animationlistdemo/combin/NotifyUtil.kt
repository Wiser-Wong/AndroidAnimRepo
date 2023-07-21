package com.wiser.animationlistdemo.combin

import android.app.Activity
import android.content.Context
import android.view.View
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.niftynotification.api.Configuration
import com.wiser.animationlistdemo.niftynotification.api.Effects
import com.wiser.animationlistdemo.niftynotification.api.NiftyNotificationView

/**
 *
 * @Author qingyang
 * 邮箱：qingyang@ksjgs.com
 * 创建时间: 2023/6/16  16:10
 * 用途: 弹出通知动画
 * **************************************
 */
object NotifyUtil {

    fun notifyStandardAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.standard,
            R.id.notify_rl,
            Configuration.Builder().setBackgroundColor("#00fff0").setTextColor("#ff00ff").build()
        )
            .setIcon(R.drawable.ip_6)
            .show()
    }

    fun notifySlideAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.thumbSlider,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_0)
            .show()
    }

    fun notifySlideInAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.slideIn,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_1)
            .show()
    }

    fun notifySlideOnTopAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.slideOnTop,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_2)
            .show()
    }

    fun notifyFlipAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.flip,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_3)
            .show()
    }

    fun notifyJellyAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.jelly,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_4)
            .show()
    }

    fun notifyScaleAnimClick(context: Activity) {
        NiftyNotificationView.build(
            context,
            "开始播放张栋梁--当你孤单你会想起谁。",
            Effects.scale,
            R.id.notify_rl
        )
            .setIcon(R.drawable.ip_5)
            .show()
    }
}