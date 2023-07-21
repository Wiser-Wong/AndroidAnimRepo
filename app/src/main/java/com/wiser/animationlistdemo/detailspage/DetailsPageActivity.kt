package com.wiser.animationlistdemo.detailspage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.activity_details_page.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.android.synthetic.main.page_above_controller.*
import kotlinx.android.synthetic.main.page_below_controller.*
import kotlinx.android.synthetic.main.page_below_controller.view.*

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/5     14:35
 * 用途: 更新说明
 ***************************************
 */
class DetailsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_page)

        controller?.setTopDistance(top_title.height.toFloat())?.buildView()

        val belowView =
            LayoutInflater.from(this).inflate(R.layout.page_below_controller, controller, false)
        controller.addBelowView(belowView)
        val aboveView =
            LayoutInflater.from(this).inflate(R.layout.page_above_controller, controller, false)
        controller.addAboveView(aboveView)
        controller.addBelowScrollListView(belowView.below_scroll_view)

        iv_back.setOnClickListener {
            finish()
        }
        img1.setOnClickListener {
            Toast.makeText(this,"image1",Toast.LENGTH_LONG).show()
        }
        img2.setOnClickListener {
            Toast.makeText(this,"image2",Toast.LENGTH_LONG).show()
        }
        img10.setOnClickListener {
            Toast.makeText(this,"image10",Toast.LENGTH_LONG).show()
        }
        img11.setOnClickListener {
            Toast.makeText(this,"image11",Toast.LENGTH_LONG).show()
        }
        view_pager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 2
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view =
                    LayoutInflater.from(this@DetailsPageActivity)
                        .inflate(R.layout.pager_story, container, false)
                val rlvList = view.findViewById<CustomRecyclerView>(R.id.rlv_list)
                rlvList?.apply {
                    layoutManager = LinearLayoutManager(this@DetailsPageActivity)
                    adapter =
                        StoryListAdapter(if (position == 0) getAboveStoryData() else getAboveCommentData())
                }

                controller.addAboveScrollListView(position, rlvList)
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }

        view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                controller.setAboveCurrentListViewIndex(position)
                updateTitle(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        tv_catalogue.setOnClickListener {
            view_pager.currentItem = 0
            controller.openAbove()
//            if (controller.isAboveBottom()) {
//                controller.startAboveToggleAnim(isOpen = true)
//            }
        }
        tv_comment.setOnClickListener {
            view_pager.currentItem = 1
            controller.openAbove()
//            if (controller.isAboveBottom()) {
//                controller.startAboveToggleAnim(isOpen = true)
//            }
        }

//        belowView.below_scroll_view.setOnVelocityScrollChangeListener(object :
//            OnVelocityScrollChangeListener {
//            override fun onScrollBottom(dy: Int, isScrollBottom: Boolean) {
//                controller.handleEdgeScrollBottom(dy, isScrollBottom)
//            }
//
//            override fun onScrollTop(dy: Int, isScrollTop: Boolean) {
//
//            }
//        })

        updateTitle(0)
    }

    private fun getMainColor(resId: Int) {
    }

    private fun updateTitle(position: Int) {
        when (position) {
            0 -> {
                tv_catalogue.setTextColor(Color.parseColor("#4a4a4a"))
                tv_comment.setTextColor(Color.parseColor("#9b9b9b"))
            }
            1 -> {
                tv_catalogue.setTextColor(Color.parseColor("#9b9b9b"))
                tv_comment.setTextColor(Color.parseColor("#4a4a4a"))
            }
        }
    }

    private fun getAboveStoryData(): MutableList<Int>? {
        val list = mutableListOf<Int>()
        list.add(R.drawable.story_pic1)
        list.add(R.drawable.story_pic2)
        list.add(R.drawable.story_pic3)
        list.add(R.drawable.story_pic4)
        list.add(R.drawable.story_pic5)
        list.add(R.drawable.story_pic6)
        list.add(R.drawable.story_pic7)
        return list
    }

    private fun getAboveCommentData(): MutableList<Int>? {
        val list = mutableListOf<Int>()
        list.add(R.drawable.details_comment_0)
        list.add(R.drawable.details_comment_1)
        list.add(R.drawable.details_comment_2)
        list.add(R.drawable.details_comment_3)
        list.add(R.drawable.details_comment_4)
        list.add(R.drawable.details_comment_5)
        list.add(R.drawable.details_comment_6)
        list.add(R.drawable.details_comment_7)
        return list
    }

}