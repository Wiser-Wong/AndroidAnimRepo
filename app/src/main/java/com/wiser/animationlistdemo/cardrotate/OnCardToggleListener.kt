package com.wiser.animationlistdemo.cardrotate

interface OnCardToggleListener {

    /**
     * 卡片展开状态开关
     */
    fun onCardToggle(isClose: Boolean)

    /**
     * 卡片展开关闭开始动画监听
     */
    fun onCardToggleStart(isClose: Boolean)

}