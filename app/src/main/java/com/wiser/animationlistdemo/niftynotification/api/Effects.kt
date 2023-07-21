package com.wiser.animationlistdemo.niftynotification.api

import com.wiser.animationlistdemo.niftynotification.api.effects.*

enum class Effects(private val effectsClazz: Class<out BaseEffect>) {
    standard(Standard::class.java), slideOnTop(
        SlideOnTop::class.java
    ),
    flip(Flip::class.java), slideIn(SlideIn::class.java), jelly(
        Jelly::class.java
    ),
    thumbSlider(ThumbSlider::class.java), scale(Scale::class.java);

    val animator: BaseEffect
        get() = try {
            effectsClazz.newInstance() as BaseEffect
        } catch (e: ClassCastException) {
            throw Error("Can not init animatorClazz instance")
        } catch (e2: InstantiationException) {
            throw Error("Can not init animatorClazz instance")
        } catch (e3: IllegalAccessException) {
            throw Error("Can not init animatorClazz instance")
        }
}