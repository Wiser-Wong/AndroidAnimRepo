package com.wiser.animationlistdemo.niftynotification.api

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.wiser.animationlistdemo.niftynotification.api.Manager.Companion.instance

class NiftyNotificationView {
    var activity: Activity?
        private set
    var configuration: Configuration? = null
        private set
    val effects: Effects
    private var iconDrawable: Drawable? = null
    private var iconRes = 0
    private var isDefault: Boolean
    private var notifyView: FrameLayout? = null
    private var onClickListener: View.OnClickListener? = null
    private var text: CharSequence? = null

    var viewGroup: ViewGroup?
        private set

    private var customViewGroup: ViewGroup? = null

    private constructor(
        activity2: Activity?,
        text2: CharSequence?,
        effects2: Effects,
        viewGroup2: ViewGroup
    ) {
        require(!(activity2 == null || text2 == null)) { NULL_PARAMETERS_ARE_NOT_ACCEPTED }
        isDefault = true
        activity = activity2
        text = text2
        effects = effects2
        viewGroup = viewGroup2
        configuration = Configuration.Builder().build()
        init(effects2)
    }

    private constructor(
        activity2: Activity?,
        text2: CharSequence?,
        effects2: Effects,
        viewGroup2: ViewGroup,
        configuration2: Configuration?
    ) {
        requireNotNull(activity2 == null || text2 == null || configuration2 == null) { NULL_PARAMETERS_ARE_NOT_ACCEPTED }
        isDefault = false
        activity = activity2
        text = text2 ?: ""
        effects = effects2
        viewGroup = viewGroup2
        configuration = configuration2
        init(effects2)
    }

    private constructor(
        activity2: Activity?,
        effects2: Effects,
        viewGroup2: ViewGroup,
        customViewGroup2: ViewGroup,
        configuration2: Configuration?
    ) {
        requireNotNull(activity2 == null || configuration2 == null || customViewGroup2 == null) { NULL_PARAMETERS_ARE_NOT_ACCEPTED }
        isDefault = false
        activity = activity2
        effects = effects2
        viewGroup = viewGroup2
        customViewGroup = customViewGroup2
        configuration = configuration2
        init(effects2)
    }

    private fun init(effects2: Effects) {
        iconDrawable = null
        iconRes = 0
    }

    val dispalyDuration: Long
        get() = configuration!!.dispalyDuration
    val isShowing: Boolean
        get() = activity != null && isNotifyViewNotNull
    private val isNotifyViewNotNull: Boolean
        private get() = !(notifyView == null || notifyView!!.parent == null)

    fun getInDuration(): Long {
        return effects.animator.duration
    }

    fun getOutDuration(): Long {
        return effects.animator.duration
    }
    fun detachActivity() {
        activity = null
    }

    fun detachViewGroup() {
        viewGroup = null
    }

    val view: View?
        get() {
            if (notifyView == null) {
                initializeNotifyView()
            }
            return notifyView
        }

    private fun initializeNotifyView() {
        notifyView = initializeCroutonViewGroup()

        if (customViewGroup != null) {
            if (onClickListener != null) {
                customViewGroup?.setOnClickListener(onClickListener)
            }
            notifyView?.addView(customViewGroup)
        } else {
            notifyView?.addView(initializeContentView())
        }
    }

    private fun initializeCroutonViewGroup(): FrameLayout {
        val notifyView2 = FrameLayout(activity!!)
        if (onClickListener != null) {
            notifyView2.setOnClickListener(onClickListener)
        }
        notifyView2.layoutParams = FrameLayout.LayoutParams(-1, -2)
        return notifyView2
    }

    private fun initializeContentView(): RelativeLayout {
        val contentView = RelativeLayout(activity)
        contentView.layoutParams = RelativeLayout.LayoutParams(-1, -1)
        var image: ImageView? = null
        if (!(iconDrawable == null && iconRes == 0)) {
            image = initializeImageView()
            contentView.addView(image, image.layoutParams)
        }
        val text2 = initializeTextView()
        val textParams = RelativeLayout.LayoutParams(-1, -2)
        if (image != null) {
            textParams.addRule(1, image.id)
        }
        textParams.addRule(13)
        contentView.addView(text2, textParams)
        return contentView
    }

    private fun initializeTextView(): TextView {
        val padding = px2dip(configuration!!.textPadding.toFloat())
        val viewHeight = px2dip(configuration!!.viewHeight.toFloat())
        val text2 = TextView(activity)
        text2.maxHeight = viewHeight
        text2.maxHeight = viewHeight
        text2.id = TEXT_ID
        text2.text = text
        text2.maxLines = configuration!!.textLines
        text2.ellipsize = TextUtils.TruncateAt.END
        text2.setPadding(padding * 2, padding, padding * 2, padding)
        text2.setTextColor(Color.parseColor(configuration!!.textColor))
        text2.setBackgroundColor(Color.parseColor(configuration!!.backgroundColor))
        if (iconDrawable == null && iconRes == 0) {
            text2.gravity = if (isDefault) 17 else configuration!!.textGravity
        } else {
            text2.minHeight = viewHeight
            text2.gravity = if (isDefault) 16 else configuration!!.textGravity
        }
        return text2
    }

    private fun initializeImageView(): ImageView {
        val maxValue = px2dip(configuration!!.viewHeight.toFloat())
        val image = ImageView(activity)
        image.minimumHeight = maxValue
        image.minimumWidth = maxValue
        image.maxWidth = maxValue
        image.maxHeight = maxValue
        image.id = IMAGE_ID
        image.setBackgroundColor(Color.parseColor(configuration!!.iconBackgroundColor))
        image.adjustViewBounds = true
        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        if (iconDrawable != null) {
            image.setImageDrawable(iconDrawable)
        }
        if (iconRes != 0) {
            image.setImageResource(iconRes)
        }
        val imageParams = RelativeLayout.LayoutParams(maxValue, maxValue)
        imageParams.addRule(9, -1)
        imageParams.addRule(15, -1)
        image.layoutParams = imageParams
        return image
    }

    fun px2dip(pxValue: Float): Int {
        return (pxValue * activity!!.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun setIcon(iconDrawable2: Drawable?): NiftyNotificationView {
        iconDrawable = iconDrawable2
        return this
    }

    fun setIcon(iconRes2: Int): NiftyNotificationView {
        iconRes = iconRes2
        return this
    }

    fun setOnClickListener(onClickListener2: View.OnClickListener?): NiftyNotificationView {
        onClickListener = onClickListener2
        return this
    }

    fun show() {
        instance!!.add(this)
    }

    fun hide() {
        instance!!.removeNotify(this)
    }

    companion object {
        private const val IMAGE_ID = 16908294
        private const val NULL_PARAMETERS_ARE_NOT_ACCEPTED = "Null parameters are not accepted"
        private const val TEXT_ID = 16908299
        fun build(
            activity2: Activity,
            text2: CharSequence?,
            effects2: Effects,
            viewGroupResId: Int
        ): NiftyNotificationView {
            return NiftyNotificationView(
                activity2,
                text2,
                effects2,
                activity2.findViewById<View>(viewGroupResId) as ViewGroup
            )
        }

        fun build(
            activity2: Activity,
            text2: CharSequence?,
            effects2: Effects,
            viewGroupResId: Int,
            configuration2: Configuration?
        ): NiftyNotificationView {
            return NiftyNotificationView(
                activity2,
                text2,
                effects2,
                activity2.findViewById<View>(viewGroupResId) as ViewGroup,
                configuration2
            )
        }

        fun build(
            activity: Activity,
            effects2: Effects,
            containerViewGroupId: Int,
            customViewGroup: ViewGroup,
            configuration2: Configuration? = null
        ): NiftyNotificationView {
            return NiftyNotificationView(
                activity,
                effects2,
                activity.findViewById<View>(containerViewGroupId) as ViewGroup,
                customViewGroup,
                configuration2
            )
        }
    }
}