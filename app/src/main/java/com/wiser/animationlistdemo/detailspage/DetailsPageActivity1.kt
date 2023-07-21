package com.wiser.animationlistdemo.detailspage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.activity_details_page1.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.android.synthetic.main.page_above_controller1.*

/**
 ***************************************
 * 项目名称:AndroidAnimRepo
 * @Author wangxy
 * 邮箱：wangxiangyu@ksjgs.com
 * 创建时间: 2023/6/27     17:33
 * 用途: 更新说明
 ***************************************
 */
class DetailsPageActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_page1)

        val view: View? = LayoutInflater.from(this).inflate(R.layout.page_above_controller1, null)
        val rlvList: CustomRecyclerView1? = view?.findViewById(R.id.rlv_list)
        rlvList?.apply {
            layoutManager = LinearLayoutManager(this@DetailsPageActivity1)
            adapter =
                StoryListAdapter(getAboveStoryData())
        }

        scroll_view.addAboveView(view)
        scroll_view.addAboveScrollListView(0, rlvList)
        scroll_view.addBelowView(LayoutInflater.from(this).inflate(R.layout.page_below_controller1,null))

        iv_back.setOnClickListener {
            finish()
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
                    LayoutInflater.from(this@DetailsPageActivity1)
                        .inflate(R.layout.pager_story1, container, false)
                val rlvList = view.findViewById<CustomRecyclerView1>(R.id.rlv_list)
                rlvList?.apply {
                    layoutManager = LinearLayoutManager(this@DetailsPageActivity1)
                    adapter =
                        StoryListAdapter(if (position == 0) getAboveStoryData() else getAboveCommentData())
                }

                scroll_view.addAboveScrollListView(position, rlvList)
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                scroll_view.setAboveCurrentListViewIndex(position)
                updateTitle(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        tv_catalogue.setOnClickListener {
            view_pager.currentItem = 0
//            if (controller.isAboveBottom()) {
//                controller.startAboveToggleAnim(isOpen = true)
//            }
        }
        tv_comment.setOnClickListener {
            view_pager.currentItem = 1
//            if (controller.isAboveBottom()) {
//                controller.startAboveToggleAnim(isOpen = true)
//            }
        }

        updateTitle(0)
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