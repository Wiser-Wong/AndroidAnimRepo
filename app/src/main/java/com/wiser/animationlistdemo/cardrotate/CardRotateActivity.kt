package com.wiser.animationlistdemo.cardrotate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.forEachIndexed
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wiser.animationlistdemo.R
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_card_rotate.*
import kotlin.collections.ArrayList

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/5/10     14:15
 * 用途: 更新说明
 ***************************************
 */
class CardRotateActivity : AppCompatActivity() {

    private val photosModels: MutableList<PhotosModel>? by lazy { getData() }

    private val photosAdapter: PhotosAdapter? by lazy { PhotosAdapter(photosModels) }

    private var topDistance: Int = 0

    private var isScroll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_rotate)
        supportFragmentManager.beginTransaction().replace(R.id.fl_content, PageContentFragment())
            .commit()
        slide_view.setOnCardPageChangeListener(object : OnCardPageChangeListener {
            override fun onPageSelect(position: Int) {
                updateShadowCheck(position)
                isScroll = true
                cube_pager.setCurrentItem(position, false)
                supportFragmentManager.beginTransaction().replace(R.id.fl_content1, PageContentFragment1.newInstance(photosModels?.get(position)?.detailsResId?:0))
                    .commit()
//                photosModels?.get(position)?.resId?.let {
//                    // 本地资源
//                    Glide.with(this@CardRotateActivity).load(it)
//                        .diskCacheStrategy(
//                            DiskCacheStrategy.RESOURCE
//                        ).dontAnimate().apply(
//                            bitmapTransform(BlurTransformation(50))
//                        ).into(object : CustomTarget<Drawable>() {
//                            override fun onResourceReady(
//                                resource: Drawable,
//                                transition: Transition<in Drawable>?
//                            ) {
//                                iv_background.setImageDrawable(resource)
//                            }
//
//                            override fun onLoadCleared(placeholder: Drawable?) {
//                            }
//
//                        })
//                }
                photosModels?.get(position)?.url?.let {
                    // 本地资源
                    Glide.with(this@CardRotateActivity).load(it)
                        .diskCacheStrategy(
                            DiskCacheStrategy.RESOURCE
                        ).dontAnimate().apply(
                            bitmapTransform(BlurTransformation(50))
                        ).into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                iv_background.setImageDrawable(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                        })
                }
            }
        })
        slide_view.setAdapter(photosAdapter)
        cube_pager.setPageTransformer(false, CubePageTransformer(45f))
        cube_pager.offscreenPageLimit = photosModels?.size ?: 0
        cube_pager.adapter = ContentPageAdapter(this, photosModels)

        cube_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                this@CardRotateActivity.isScroll = false
            }

            override fun onPageSelected(position: Int) {
                setDot(position)
                if (!isScroll) {
                    slide_view.setCurrentItem(position)
                    supportFragmentManager.beginTransaction().replace(R.id.fl_content1, PageContentFragment1.newInstance(photosModels?.get(position)?.detailsResId?:0))
                        .commit()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        // 卡片打开关闭监听
        slide_view.setOnCardToggleListener(object : OnCardToggleListener {
            override fun onCardToggle(isClose: Boolean) {
            }

            override fun onCardToggleStart(isClose: Boolean) {
                photosAdapter?.setOpen(!isClose)
                if (isClose) {
                    setTransitionAnim()
                }
            }
        })
        scroll_view.setOnScrollChangeListener(OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0) {
                photosAdapter?.setCanClick(false)
                slide_view.setCanTouchView(slide_view.getTopView(),false)
            } else {
                photosAdapter?.setCanClick(true)
                slide_view.setCanTouchView(slide_view.getTopView(),true)
            }
            val height = cl_top_view.height
            if (scrollY > height) {
                slide_view.scaleX = 0f
                slide_view.scaleY = 0f
                cl_top_view.translationY = height.toFloat()
            } else {
                slide_view.scaleX = 1f - scrollY.toFloat() / height
                slide_view.scaleY = 1f - scrollY.toFloat() / height
                cl_top_view.translationY = scrollY.toFloat()
            }

            if (scrollY > ScreenUtil.getScreenHeight(this@CardRotateActivity) - height) {
                ll_function.translationY = scrollY.toFloat()
            } else {
                ll_function.translationY = scrollY.toFloat() * 2
            }
        })

        // dot
        initDots()

    }

    private fun initDots() {
        photosModels?.forEachIndexed { index, photosModel ->
            val dotView = LayoutInflater.from(this).inflate(R.layout.view_dot, ll_dots, false)
            dotView.isSelected = index == cube_pager.currentItem
            ll_dots.addView(dotView)
        }
    }

    private fun setDot(position: Int) {
        ll_dots.forEachIndexed { index, view ->
            view.isSelected = index == position
        }
    }

    fun setDotShowStatus(isShow: Boolean) {
        if (isShow) {
            ll_dots.visibility = View.VISIBLE
        } else {
            ll_dots.visibility = View.INVISIBLE
        }
    }

    private fun updateShadowCheck(position: Int) {
        photosModels?.forEachIndexed { index, photosModel ->
            if (position == index) {
                val photoModel = photosAdapter?.getItem(position)
                photoModel?.isCheck = true
            } else {
                val photoModel = photosAdapter?.getItem(index)
                photoModel?.isCheck = false
            }
        }
        photosAdapter?.notifyDataAdapter()
    }

    fun setCanScrollViewPager(isCanScroll: Boolean) {
        cube_pager.setCanScroll(isCanScroll)
    }

    fun getDotHeight(): Int {
        val location = IntArray(2)
        ll_dots.getLocationOnScreen(location)
        val dotTopDistance = location[1]
        return if (topDistance > dotTopDistance) {
            topDistance - dotTopDistance - ll_dots.height
        } else {
            resources.getDimension(R.dimen.ksl_dp_30).toInt()
        }
    }

    fun setTransitionAnim() {
        val set = AnimatorSet()
        val searchAlphaAnimator = ObjectAnimator.ofFloat(et_search, "alpha", 1f, 0f)
        val titleAlphaAnimator = ObjectAnimator.ofFloat(tv_card_title, "alpha", 1f, 0f)
        val titleTranslationAnimator =
            ObjectAnimator.ofFloat(tv_card_title, "translationY", 0f, -70f)
        val titleScaleXAnimator = ObjectAnimator.ofFloat(tv_card_title, "scaleX", 1f, 0f)
        val titleScaleYAnimator = ObjectAnimator.ofFloat(tv_card_title, "scaleY", 1f, 0f)
        val pagerTranslationAnimator = ObjectAnimator.ofFloat(slide_view, "translationY", 0f, -120f)
        val location = IntArray(2)
        fl_content.getLocationOnScreen(location)
        topDistance = location[1]
        val contentTranslationAnimator = ObjectAnimator.ofFloat(
            fl_content,
            "translationY",
            0f,
            location[1].toFloat()
        )
        val contentAlphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 0.6f, 0f)
        val content1TranslationAnimator = ObjectAnimator.ofFloat(
            fl_content1,
            "translationY",
            location[1].toFloat(),
            0f
        )
        val content1AlphaAnimator = ObjectAnimator.ofFloat(fl_content1, "alpha", 0.6f, 1f)
        val functionTranslationAnimator = ObjectAnimator.ofFloat(
            ll_function,
            "translationY",
            location[1].toFloat() - 100,
            0f
        )
        val functionAlphaAnimator = ObjectAnimator.ofFloat(
            ll_function,
            "alpha",
            0f,
            1f
        )
        val dotsTranslationAnimator = ObjectAnimator.ofFloat(ll_dots, "translationY", 0f, -60f)
        val dotsAlphaAnimator = ObjectAnimator.ofFloat(ll_dots, "alpha", 0f, 1f)
        set.duration = slide_view.getCloseDuration()
        set.playTogether(
            searchAlphaAnimator,
            titleAlphaAnimator,
            titleTranslationAnimator,
            titleScaleXAnimator,
            titleScaleYAnimator,
            pagerTranslationAnimator,
            contentAlphaAnimator,
            contentTranslationAnimator,
            content1AlphaAnimator,
            content1TranslationAnimator,
            functionTranslationAnimator,
            functionAlphaAnimator,
            dotsTranslationAnimator,
            dotsAlphaAnimator
        )
        set.addListener(onStart = {
            fl_content1.visibility = View.VISIBLE
            ll_function.visibility = View.VISIBLE
            ll_dots.visibility = View.VISIBLE
        }, onEnd = {
            et_search.visibility = View.GONE
            fl_content.visibility = View.GONE
            fl_content1.visibility = View.GONE
            cube_pager.visibility = View.VISIBLE
            scroll_view.visibility = View.GONE
        })
        set.start()
    }

    fun setTransitionAnimBack() {
        val set = AnimatorSet()
        val searchAlphaAnimator = ObjectAnimator.ofFloat(et_search, "alpha", 0f, 1f)
        val titleAlphaAnimator = ObjectAnimator.ofFloat(tv_card_title, "alpha", 0f, 1f)
        val titleTranslationAnimator =
            ObjectAnimator.ofFloat(tv_card_title, "translationY", -70f, 0f)
        val titleScaleXAnimator = ObjectAnimator.ofFloat(tv_card_title, "scaleX", 0f, 1f)
        val titleScaleYAnimator = ObjectAnimator.ofFloat(tv_card_title, "scaleY", 0f, 1f)
        val pagerTranslationAnimator = ObjectAnimator.ofFloat(slide_view, "translationY", -120f, 0f)
        val location = IntArray(2)
        fl_content1.getLocationOnScreen(location)
        val contentTranslationAnimator = ObjectAnimator.ofFloat(
            fl_content,
            "translationY",
            location[1].toFloat(),
            0f
        )
        val contentAlphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 0.6f, 1f)
        val content1TranslationAnimator = ObjectAnimator.ofFloat(
            fl_content1,
            "translationY",
            0f,
            location[1].toFloat()
        )
        val content1AlphaAnimator = ObjectAnimator.ofFloat(fl_content1, "alpha", 0.6f, 1f)
        val functionTranslationAnimator = ObjectAnimator.ofFloat(
            ll_function,
            "translationY",
            0f,
            location[1].toFloat() - 100
        )
        val functionAlphaAnimator = ObjectAnimator.ofFloat(
            ll_function,
            "alpha",
            1f,
            0f
        )
        val dotsTranslationAnimator = ObjectAnimator.ofFloat(ll_dots, "translationY", -60f, 0f)
        val dotsAlphaAnimator = ObjectAnimator.ofFloat(ll_dots, "alpha", 1f, 0f)
        set.duration = slide_view.getCloseDuration()
        set.playTogether(
            searchAlphaAnimator,
            titleAlphaAnimator,
            titleTranslationAnimator,
            titleScaleXAnimator,
            titleScaleYAnimator,
            pagerTranslationAnimator,
            contentAlphaAnimator,
            contentTranslationAnimator,
            content1AlphaAnimator,
            content1TranslationAnimator,
            functionTranslationAnimator,
            functionAlphaAnimator,
            dotsTranslationAnimator,
            dotsAlphaAnimator
        )
        set.addListener(onStart = {
            cube_pager.visibility = View.INVISIBLE
            et_search.visibility = View.VISIBLE
            scroll_view.visibility = View.VISIBLE
            fl_content1.visibility = View.VISIBLE
            fl_content.visibility = View.VISIBLE
            slide_view.setOpenOtherAnim()
        }, onEnd = {
            ll_function.visibility = View.GONE
            fl_content1.visibility = View.GONE
            ll_dots.visibility = View.INVISIBLE
        })
        set.start()
    }

    private fun getData(): MutableList<PhotosModel>? {
        val photoModels: MutableList<PhotosModel> = ArrayList()
        val model0 = PhotosModel()
        model0.url = "https://cdn.kaishuhezi.com/kstory/story/image/d33a0dd4-8ebe-4567-9cb7-bd702ed04606_info_w=750_h=750_s=434502.jpg"
        model0.resId = R.drawable.card0
        model0.detailsResId = R.drawable.card_details_xyj_all
        model0.title = "西游记"
        model0.isCheck = true
        val model1 = PhotosModel()
        model1.url = "https://cdn.kaishuhezi.com/kstory/story/image/2f57ddac-b5a4-4dd3-846c-993c7790e4aa_info_w=750_h=750_s=487192.jpg"
        model1.resId = R.drawable.card1
        model1.detailsResId = R.drawable.card_details_kdst_all
        model1.title = "口袋神探"
        val model2 = PhotosModel()
        model2.url = "https://cdn.kaishuhezi.com/kstory/story/image/6ad8b393-22df-428d-b10c-3e1d75261537_info_w=450_h=450_s=324694.jpg"
        model2.resId = R.drawable.card2
        model2.detailsResId = R.drawable.card_details_hydmx_all
        model2.title = "荒野大冒险2"
        val model3 = PhotosModel()
        model3.url = "https://cdn.kaishuhezi.com/kstory/story/image/5100d613-b2d1-49a1-b4ec-7dbdfbd22233_info_w=450_h=450_s=79168.jpg"
        model3.resId = R.drawable.card3
        model3.detailsResId = R.drawable.card_details_hlm_all
        model3.title = "红楼梦"
        val model4 = PhotosModel()
        model4.url = "https://cdn.kaishuhezi.com/kstory/story/image/c3ac93e8-d27d-481d-9905-f2a351c5a16b_info_w=450_h=450_s=188145.jpg"
        model4.resId = R.drawable.card4
        model4.detailsResId = R.drawable.card_details_sgyy_all
        model4.title = "三国演义"
        val model5 = PhotosModel()
        model5.url = "https://cdn.kaishuhezi.com/kstory/story/image/98aceef7-25b2-45d1-bc62-5d4fb2774073_info_w=750_h=750_s=491918.jpg"
        model5.resId = R.drawable.card5
        model5.detailsResId = R.drawable.card_details_shz_all
        model5.title = "水浒传"
        photoModels.add(model0)
        photoModels.add(model1)
        photoModels.add(model2)
        photoModels.add(model3)
        photoModels.add(model4)
        photoModels.add(model5)
        return photoModels
    }

}