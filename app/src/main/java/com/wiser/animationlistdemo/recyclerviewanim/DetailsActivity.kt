package com.wiser.animationlistdemo.recyclerviewanim

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.activity_recyclerview_anim_details.*
import kotlinx.android.synthetic.main.activity_recyclerview_anim_item.fl_background
import kotlinx.android.synthetic.main.activity_recyclerview_anim_item.iv_ip
import kotlinx.android.synthetic.main.activity_recyclerview_anim_item.tv_title

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_anim_details)
        val ipHeader: Int = intent?.getIntExtra("ipHeader", 0) ?: 0
        val background: Int = intent?.getIntExtra("background", 0) ?: 0
        val backgroundColor: Int = intent?.getIntExtra("backgroundColor", 0) ?: 0
        val title: String? = intent?.getStringExtra("title")
        val detailsResId: Int = intent?.getIntExtra("detailsResId",0)?: 0
        iv_ip?.setImageResource(ipHeader)
        fl_background?.setBackgroundResource(background)
//        tv_title?.setBackgroundColor(backgroundColor)
        tv_title?.text = title ?: ""
        iv_details?.setImageResource(detailsResId)

        iv_details?.startAnimation(AnimationUtils.loadAnimation(this, androidx.appcompat.R.anim.abc_slide_in_bottom))

        iv_back.setOnClickListener {
            super.onBackPressed()
        }
    }

}