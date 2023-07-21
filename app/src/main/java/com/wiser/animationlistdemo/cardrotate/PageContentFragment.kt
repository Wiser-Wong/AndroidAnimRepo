package com.wiser.animationlistdemo.cardrotate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.item_card_content.*

class PageContentFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_card_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(iv_photo1).load("https://cdn.kaishuhezi.com/kstory/story/image/c3ac93e8-d27d-481d-9905-f2a351c5a16b_info_w=450_h=450_s=188145.jpg").transform(RoundedCorners(30)).into(iv_photo1)
        Glide.with(iv_photo2).load("https://cdn.kaishuhezi.com/kstory/story/image/98aceef7-25b2-45d1-bc62-5d4fb2774073_info_w=750_h=750_s=491918.jpg").transform(RoundedCorners(30)).into(iv_photo2)
        Glide.with(iv_photo3).load("https://cdn.kaishuhezi.com/kstory/story/image/d33a0dd4-8ebe-4567-9cb7-bd702ed04606_info_w=750_h=750_s=434502.jpg").transform(RoundedCorners(30)).into(iv_photo3)
    }
}