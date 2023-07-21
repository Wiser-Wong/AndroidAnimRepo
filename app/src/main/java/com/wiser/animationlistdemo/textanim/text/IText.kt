package com.wiser.animationlistdemo.textanim.text

import android.graphics.Canvas

interface IText {
    fun onPrepareAnimate()
    fun startAnimator()
    fun drawFrame(canvas: Canvas?)
    fun setTextAnim(text: CharSequence?)
}