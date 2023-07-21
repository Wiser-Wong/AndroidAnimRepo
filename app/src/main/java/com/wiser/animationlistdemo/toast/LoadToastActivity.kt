package com.wiser.animationlistdemo.toast

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wiser.animationlistdemo.R


class LoadToastActivity : AppCompatActivity() {

    private val handler: Handler? by lazy { Handler() }

    private val successRunnable: Runnable = Runnable { loadToast?.success() }

    private val errorRunnable: Runnable = Runnable { loadToast?.error() }

    private val loadToast: LoadToast? by lazy {
        LoadToast(this)
            .setProgressColor(Color.parseColor("#cb5ed9"))
            .setText("今天要闻，chatGpt4.0发布了")
            .setTranslationY(100)
            .setBorderColor(Color.LTGRAY)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_toast)
    }

    fun showToSuccess(view: View) {
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.show()
        handler?.postDelayed(successRunnable, 3000)
    }

    fun showToError(view: View) {
        loadToast?.hide()
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.show()
        handler?.postDelayed(errorRunnable, 3000)
    }

    fun show(view: View) {
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.show()
    }

    fun error(view: View) {
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.error()
    }

    fun success(view: View) {
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.success()
    }

    fun hide(view: View) {
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
        loadToast?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(successRunnable)
        handler?.removeCallbacks(errorRunnable)
    }
}