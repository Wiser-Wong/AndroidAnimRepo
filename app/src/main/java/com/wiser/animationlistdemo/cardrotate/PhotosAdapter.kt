package com.wiser.animationlistdemo.cardrotate

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.cardrotate.view.ShadowContainer
import com.wiser.animationlistdemo.cardrotate.view.SlideLayout
import com.wiser.animationlistdemo.cardrotate.view.SlidePageAdapter

/**
 * @author Wiser
 */
class PhotosAdapter(private val photosModels: List<PhotosModel?>?) :
    SlidePageAdapter<PhotosModel?>() {
    private var isOpen = true

    private var isCanClick = true

    fun setOpen(isOpen: Boolean) {
        this.isOpen = isOpen
    }

    fun setCanClick(isCanClick: Boolean) {
        this.isCanClick = isCanClick
    }
    override val counts: Int
        get() = photosModels?.size ?: 0

    override fun getItem(position: Int): PhotosModel? {
        return photosModels?.get(position)
    }

    override fun getItemView(viewGroup: ViewGroup?, position: Int): View? {
        val view: SlideLayout? = inflater(viewGroup, R.layout.item_slide) as SlideLayout?
        val ivCard = view?.findViewById<ImageView>(R.id.iv_card)
        val cardShadow = view?.findViewById<ShadowContainer>(R.id.card_shadow)
//        ivCard?.setImageResource(photosModels?.get(position)?.resId ?: 0)
        ivCard?.let { Glide.with(it).load(photosModels?.get(position)?.url?:"").transform(RoundedCorners(30)).into(it) }
        ivCard?.setOnClickListener {
            if (view.isRunningAnim() == true || !isCanClick) return@setOnClickListener
            isOpen = !isOpen
            view.setToggleOtherAnim(isOpen)
        }
        if (getItem(position)?.isCheck == true) {
            cardShadow?.setShadowRadius(context()?.resources?.getDimension(R.dimen.ksl_dp_40)!!)
        } else {
            cardShadow?.setShadowRadius(0f)
        }
        return view
    }
}