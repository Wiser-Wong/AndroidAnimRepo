package com.wiser.animationlistdemo.list.ani.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/3/13 11:07
 * @author: jiaruihua
 * @desc :
 *
 *  底部导航栏
 */
class KsNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    val book by lazy { findViewById<NavImageView>(R.id.book) }
    val sport by lazy { findViewById<NavImageView>(R.id.sport) }
    val apple by lazy { findViewById<NavImageView>(R.id.apple) }
    val mine by lazy { findViewById<NavImageView>(R.id.mine) }
    init {
        inflate(context, R.layout.nav_bar, this)

        selectBook()

        book.setOnClickListener { selectBook() }
        sport.setOnClickListener { selectSport() }
        apple.setOnClickListener { selectApple() }
        mine.setOnClickListener { selectMine() }

    }

    fun reset(){
        book.setImageIcon(0, false)
        sport.setImageIcon(1, false)
        apple.setImageIcon(2, false)
        mine.setImageIcon(3, false)
    }

    private fun selectBook() {
        book.setImageIcon(0, true)
        sport.setImageIcon(1, false)
        apple.setImageIcon(2, false)
        mine.setImageIcon(3, false)
    }

    private fun selectSport() {
        book.setImageIcon(0, false)
        sport.setImageIcon(1, true)
        apple.setImageIcon(2, false)
        mine.setImageIcon(3, false)
    }
    private fun selectApple() {
        book.setImageIcon(0, false)
        sport.setImageIcon(1, false)
        apple.setImageIcon(2, true)
        mine.setImageIcon(3, false)
    }
    private fun selectMine() {
        book.setImageIcon(0, false)
        sport.setImageIcon(1, false)
        apple.setImageIcon(2, false)
        mine.setImageIcon(3, true)
    }
}