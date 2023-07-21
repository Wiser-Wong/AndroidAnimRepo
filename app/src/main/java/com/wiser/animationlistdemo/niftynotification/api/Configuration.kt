package com.wiser.animationlistdemo.niftynotification.api

class Configuration private constructor(builder: Builder) {
    @JvmField
    val animDuration: Long
    @JvmField
    val backgroundColor: String
    @JvmField
    val dispalyDuration: Long
    @JvmField
    val iconBackgroundColor: String
    @JvmField
    val textColor: String
    @JvmField
    val textGravity: Int
    @JvmField
    val textLines: Int
    @JvmField
    val textPadding: Int
    @JvmField
    val viewHeight: Int

    init {
        animDuration = builder.animDuration
        textColor = builder.textColor
        dispalyDuration = builder.dispalyDuration
        backgroundColor = builder.backgroundColor
        textPadding = builder.textPadding
        viewHeight = builder.viewHeight
        iconBackgroundColor = builder.iconBackgroundColor
        textGravity = builder.textGravity
        textLines = builder.textLines
    }

    class Builder {
        var animDuration: Long
        var backgroundColor: String
        var dispalyDuration: Long = 0
        var iconBackgroundColor: String
        var textColor: String
        var textGravity: Int
        var textLines: Int
        var textPadding: Int
        var viewHeight: Int

        constructor() {
            animDuration = 700
            dispalyDuration = 1500
            textColor = "#FF444444"
            backgroundColor = "#FFBDC3C7"
            textPadding = 5
            viewHeight = 48
            iconBackgroundColor = "#FFFFFFFF"
            textGravity = 17
            textLines = 2
        }

        constructor(baseStyle: Configuration) {
            animDuration = baseStyle.animDuration
            textColor = baseStyle.textColor
            backgroundColor = baseStyle.backgroundColor
            textPadding = baseStyle.textPadding
            viewHeight = baseStyle.viewHeight
            iconBackgroundColor = baseStyle.iconBackgroundColor
            textGravity = baseStyle.textGravity
            textLines = baseStyle.textLines
        }

        fun setAnimDuration(animDuration2: Long): Builder {
            animDuration = animDuration2
            return this
        }

        fun setDispalyDuration(dispalyDuration2: Long): Builder {
            dispalyDuration = dispalyDuration2
            return this
        }

        fun setTextColor(textColor2: String): Builder {
            textColor = textColor2
            return this
        }

        fun setBackgroundColor(backgroundColor2: String): Builder {
            backgroundColor = backgroundColor2
            return this
        }

        fun setTextPadding(textPadding2: Int): Builder {
            textPadding = textPadding2
            return this
        }

        fun setViewHeight(viewHeight2: Int): Builder {
            viewHeight = viewHeight2
            return this
        }

        fun setIconBackgroundColor(iconBackgroundColor2: String): Builder {
            iconBackgroundColor = iconBackgroundColor2
            return this
        }

        fun setTextGravity(textGravity2: Int): Builder {
            textGravity = textGravity2
            return this
        }

        fun setTextLines(textLines2: Int): Builder {
            textLines = textLines2
            return this
        }

        fun build(): Configuration {
            return Configuration(this)
        }
    }

    companion object {
        const val ANIM_DISPLAY_DURATION = 1500
        const val ANIM_DURATION = 700
    }
}