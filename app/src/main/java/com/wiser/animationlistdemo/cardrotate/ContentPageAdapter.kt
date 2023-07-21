package com.wiser.animationlistdemo.cardrotate

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.cardrotate.view.CustomScrollView
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wiser.animationlistdemo.cardrotate.view.ShadowContainer
import jp.wasabeef.glide.transformations.BlurTransformation

class ContentPageAdapter(val context: Context, private val photosModels: List<PhotosModel>?) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = photosModels?.get(position)
        val view: View? =
            LayoutInflater.from(context).inflate(R.layout.item_card_page, container, false)
        val ivCover: ImageView? = view?.findViewById(R.id.iv_cover)
        val shadowCover: ShadowContainer? = view?.findViewById(R.id.shadow_cover)
        val ivBackgroundPage: ImageView? = view?.findViewById(R.id.iv_background_page)
        val scrollViewPage: CustomScrollView? = view?.findViewById(R.id.scroll_view_page)
        val clTopViewPage: ConstraintLayout? = view?.findViewById(R.id.cl_top_view_page)
        val tvTitle: AppCompatTextView? = view?.findViewById(R.id.tv_title)
        val llFunctionPage: LinearLayout? = view?.findViewById(R.id.ll_function_page)
        val ivDetails: AppCompatImageView? = view?.findViewById(R.id.iv_details)
//        ivCover?.setImageResource(item?.resId ?: 0)
        ivCover?.let { Glide.with(it).load(photosModels?.get(position)?.url?:"").transform(
            RoundedCorners(30)
        ).into(it) }
        ivCover?.setOnClickListener {
            (context as? CardRotateActivity)?.apply {
                setTransitionAnimBack()
            }
        }
        ivDetails?.setImageResource(photosModels?.get(position)?.detailsResId?:0)
        ivBackgroundPage?.let {
//            Glide.with(context).load(item?.resId ?: 0)
//                .apply(bitmapTransform(BlurTransformation(50)))
//                .into(object : CustomTarget<Drawable>() {
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        transition: Transition<in Drawable>?
//                    ) {
//                        it.setImageDrawable(resource)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//
//                })
            Glide.with(context).load(item?.url ?: "")
                .apply(bitmapTransform(BlurTransformation(50)))
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        it.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                })
        }
        scrollViewPage?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            ivCover?.isClickable = scrollY <= 0
            val height = clTopViewPage?.height ?: 0
            if (scrollY > height) {
                shadowCover?.apply {
                    scaleX = 0f
                    scaleY = 0f
                }
                clTopViewPage?.translationY = height.toFloat()
            } else {
                shadowCover?.apply {
                    scaleX = 1f - scrollY.toFloat() / height
                    scaleY = 1f - scrollY.toFloat() / height
                }
                clTopViewPage?.translationY = scrollY.toFloat()
            }
            tvTitle?.apply {
                if ((1 - scrollY.toFloat() / height) > 0.3f && (1 - scrollY.toFloat() / height) <= 1f) {
                    scaleX = 1f - scrollY.toFloat() / height
                    scaleY = 1f - scrollY.toFloat() / height
                } else {
                    scaleX = 0.3f
                    scaleY = 0.3f
                }
            }

            if (scrollY > (ScreenUtil.getScreenHeight(context) - height - (llFunctionPage?.height
                    ?: 0) / 2)
            ) {
                llFunctionPage?.translationY =
                    (ScreenUtil.getScreenHeight(context) - height - 30 - (llFunctionPage?.height
                        ?: 0) / 2).toFloat() + scrollY
            } else {
                llFunctionPage?.translationY = scrollY * 2f
            }

            (context as? CardRotateActivity)?.apply {
                if (scrollY > getDotHeight()) {
                    setCanScrollViewPager(false)
                    setDotShowStatus(false)
                } else {
                    setCanScrollViewPager(true)
                    setDotShowStatus(true)
                }
            }
        })

        container.addView(view)
        return view!!
    }

    override fun getCount(): Int {
        return photosModels?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}