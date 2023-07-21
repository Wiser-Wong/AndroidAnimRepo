package com.wiser.animationlistdemo.list.ani

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.wiser.animationlistdemo.R
import com.wiser.animationlistdemo.home.DataSource
import com.wiser.animationlistdemo.list.ani.adaper.HAdaper
import com.wiser.animationlistdemo.list.ani.adaper.TransAniStyle1
import com.wiser.animationlistdemo.list.ani.adaper.TransAniStyle2
import com.wiser.animationlistdemo.list.ani.adaper.VAdaper
import com.wiser.animationlistdemo.list.ani.bean.ColorItem
import kotlinx.android.synthetic.main.home_act.topView12
import kotlinx.android.synthetic.main.home_act.topView13
import kotlinx.android.synthetic.main.home_act.topView14
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * @date: 2023/3/13 10:49
 * @author: jiaruihua
 * @desc :
 *
 */
class HomeAct : ComponentActivity() {

    val itemList = mutableListOf<ColorItem>()

    val mHAdaper = HAdaper{

    }.apply {
        adapterAnimation = TransAniStyle1()
        animationEnable = true


    }
    val mVAdaper = VAdaper().apply {
        adapterAnimation = TransAniStyle2()
        animationEnable = true


    }

    val topView by lazy {  findViewById<ShapeableImageView>(R.id.topView)}
    val topView2 by lazy {  findViewById<View>(R.id.topView2)}
    val topView3 by lazy {  findViewById<View>(R.id.topView3)}

    val rcList by lazy { findViewById<RecyclerView>(R.id.rcList)}
    val rcList2 by lazy { findViewById<RecyclerView>(R.id.rcList2)}


    val mLayoutManager = LinearLayoutManager(this@HomeAct,LinearLayoutManager.HORIZONTAL,false)
    val mLayoutManager2 = GridLayoutManager(this@HomeAct,2,GridLayoutManager.VERTICAL,false)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_act)

        DataSource.getImageResource().get(0).let {
            topView.setImageResource(it.src)
        }
        DataSource.getImageResource().get(1).let {
            topView12.setImageResource(it.src)
        }
        DataSource.getImageResource().get(2).let {
            topView13.setImageResource(it.src)
        }
        DataSource.getImageResource().get(3).let {
            topView14.setImageResource(it.src)
        }
        topView.alpha = 0F
        topAni1(topView,100L)

        rcList.run {
            adapter = mHAdaper
            layoutManager =  mLayoutManager
        }

        rcList2.run {
            adapter = mVAdaper
            layoutManager =  mLayoutManager2
        }

        initData()

//        mHAdaper.setNewInstance(itemList)
//        rcList.alpha = 0F
//        rcList.visibility = View.GONE
//        rcList.postDelayed({rcList.visibility = View.VISIBLE},200)
//        topAni(rcList,300L)
        topAni(topView12,200L)
        topAni(topView13,300L)
        topAni(topView14,400L)



    }


    fun initData(){

        lifecycleScope.launch {
            repeat(100){
//                itemList.add(ColorItem(getAColor()))
                launch {
                    delay(it*150L)
                    mHAdaper.addData(ColorItem(getAColor()))
                }

            }
        }

        mHAdaper.setNewInstance(itemList)

//        lifecycleScope.launch {
//            delay(2000)
//            repeat(100){
//                mHAdaper.addData(ColorItem(getAColor()))
//            }
//        }
//        mHAdaper.setOnItemClickListener { adapter, view, position ->
//
//            topAniHide(rcList,0){
//
//            }
//            topAniHide(topView,200){
////                topAni1(topView2,100L)
////                topAni1(topView3,200L)
//                mHAdaper.setNewInstance(mutableListOf())
//                topAni1(rcList2,100L)
//                initData2()
//            }
//
//        }

        mHAdaper.setOnItemClickListener{
            _,_,_ ->

                        topAniHide(rcList,0){

            }
            topAniHide(topView,200){
//                topAni1(topView2,100L)
//                topAni1(topView3,200L)
                mHAdaper.setNewInstance(mutableListOf())
                topAni1(rcList2,100L)
                initData2()
            }
            topAniHide(topView12,200){}
            topAniHide(topView13,200){}
            topAniHide(topView14,200){}
        }

    }


    fun initData2(){

        lifecycleScope.launch {
            repeat(30){
//                itemList.add(ColorItem(getAColor()))
                launch {
                    delay(it*150L)
                    mVAdaper.addData(ColorItem(getAColor()))
                }

            }


        }

        lifecycleScope.launch {
            delay(2000)
            repeat(100){
                mVAdaper.addData(ColorItem(getAColor()))
            }
        }

        mVAdaper.setOnItemClickListener { adapter, view, position ->

            topAniHide(rcList2,0){
                mVAdaper.setNewInstance(mutableListOf())
            }

            topAni1(topView,100L)
            topAni1(rcList,200L)
            topAni1(topView12,300L)
            topAni1(topView13,400L)
            topAni1(topView14,500L)
            initData()
        }

    }

    val random = Random(256)
    fun getAColor():Int{

        return  Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))
    }


}