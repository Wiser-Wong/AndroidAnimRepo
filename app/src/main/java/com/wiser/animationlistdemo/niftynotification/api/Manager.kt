package com.wiser.animationlistdemo.niftynotification.api

import android.annotation.TargetApi
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class Manager private constructor() : Handler() {
    private val notifyQueue: Queue<NiftyNotificationView?> = LinkedBlockingQueue()

    private object Messages {
        const val ADD_TO_VIEW = 538183700
        const val DISPLAY_NOTIFICATION = 538183699
        const val REMOVE_NOTIFICATION = 538183701
        const val REMOVE_NOTIFICATION_VIEW = 538183702
    }

    fun add(crouton: NiftyNotificationView?) {
        if (notifyQueue.size < 1) {
            notifyQueue.add(crouton)
            displayNotify()
        }
    }

    private fun calculateCroutonDuration(notify: NiftyNotificationView?): Long {
        return notify!!.dispalyDuration + notify.effects.animator.duration
    }

    private fun sendMessage(crouton: NiftyNotificationView?, messageId: Int) {
        val message = obtainMessage(messageId)
        message.obj = crouton
        sendMessage(message)
    }

    fun sendMessageDelayed(crouton: NiftyNotificationView?, messageId: Int, delay: Long) {
        val message = obtainMessage(messageId)
        message.obj = crouton
        sendMessageDelayed(message, delay)
    }

    override fun handleMessage(msg: Message) {
        val notify = msg.obj as NiftyNotificationView
        if (notify != null) {
            when (msg.what) {
                Messages.DISPLAY_NOTIFICATION -> displayNotify()
                Messages.ADD_TO_VIEW -> addNotifyToView(notify)
                Messages.REMOVE_NOTIFICATION -> removeNotify(notify)
                Messages.REMOVE_NOTIFICATION_VIEW -> removeNotifyView(notify)
                else -> super.handleMessage(msg)
            }
            super.handleMessage(msg)
        }
    }

    private fun displayNotify() {
        if (!notifyQueue.isEmpty()) {
            val currentNotify = notifyQueue.peek()
            if (currentNotify!!.activity == null) {
                notifyQueue.poll()
            }
            if (!currentNotify.isShowing) {
                sendMessage(currentNotify, Messages.ADD_TO_VIEW)
            } else {
                sendMessageDelayed(
                    currentNotify,
                    Messages.DISPLAY_NOTIFICATION,
                    calculateCroutonDuration(currentNotify)
                )
            }
        }
    }

    private fun addNotifyToView(notify: NiftyNotificationView) {
        if (!notify.isShowing) {
            val notifyView = notify.view
            if (notifyView?.parent == null) {
                var params = notifyView?.layoutParams
                if (params == null) {
                    params = MarginLayoutParams(-1, -2)
                }
                if (notify.viewGroup == null) {
                    val activity = notify.activity
                    if (activity != null && !activity.isFinishing) {
                        activity.addContentView(notifyView, params)
                    } else {
                        return
                    }
                } else if (notify.viewGroup is FrameLayout) {
                    notify.viewGroup?.addView(notifyView, params)
                } else {
                    notify.viewGroup?.addView(notifyView, 0, params)
                }
            }
            notifyView?.requestLayout()
            val observer = notifyView?.viewTreeObserver
            observer?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                @TargetApi(16)
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        notifyView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        notifyView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    notify.configuration?.animDuration?.let {
                        notify.effects.animator.setDuration(it).`in`(notify.view!!)
                    }
                    this@Manager.sendMessageDelayed(
                        notify,
                        Messages.REMOVE_NOTIFICATION,
                        notify.dispalyDuration + notify.getInDuration()
                    )
                }
            })
        }
    }

    fun removeNotify(notify: NiftyNotificationView) {
        notify.view?.let {
            notify.configuration?.animDuration?.let { it1 ->
                notify.effects.animator.setDuration(it1).out(
                    it
                )
            }
        }
        sendMessageDelayed(notify, Messages.REMOVE_NOTIFICATION_VIEW, notify.getOutDuration())
        sendMessageDelayed(notify, Messages.DISPLAY_NOTIFICATION, notify.getOutDuration())
    }

    fun removeNotifyView(notify: NiftyNotificationView) {
        val notifyView = notify.view
        val notifyParentView = notifyView?.parent as? ViewGroup
        val removed = notifyQueue.poll()
        notifyParentView?.removeView(notifyView)
        if (removed != null) {
            removed.detachActivity()
            removed.detachViewGroup()
        }
    }

    companion object {
        private var INSTANCE: Manager? = null

        @JvmStatic
        @get:Synchronized
        val instance: Manager?
            get() {
                var manager: Manager?
                synchronized(Manager::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Manager()
                    }
                    manager = INSTANCE
                }
                return manager
            }
    }
}