package com.wiser.animationlistdemo.download

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wiser.animationlistdemo.R
import java.util.*

class DownloadActivity : AppCompatActivity() {

    private var count = 0
    private var progress = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val downloadBtn = findViewById<ArrowDownloadButton>(R.id.arrow_download_btn)
        downloadBtn?.setOnDownloadCompletedListener(object : OnDownloadCompletedListener {
            override fun onCompleted() {
                progress = 0f
            }
        })
        downloadBtn?.setOnClickListener {
            if ((count % 2) == 0) {
                downloadBtn.startAnimating()
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            progress += 1
                            downloadBtn.setProgress(progress)
                            if (progress == 100f) {
                                timer.cancel()
                            }
                        }
                    }
                }, 800, 20)
            } else {
                progress = 0f
                downloadBtn.reset()
            }
            count++
        }
    }

}