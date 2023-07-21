package com.wiser.animationlistdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.wiser.animationlistdemo.cardrotate.CardRotateActivity
import com.wiser.animationlistdemo.detailspage.DetailsPageActivity
import com.wiser.animationlistdemo.download.DownloadActivity
import com.wiser.animationlistdemo.audio.AudioMainAct
import com.wiser.animationlistdemo.home.HomeActivity
import com.wiser.animationlistdemo.list.ani.HomeAct
import com.wiser.animationlistdemo.loginanim.LoginPageActivity
import com.wiser.animationlistdemo.menu.MenuAct
import com.wiser.animationlistdemo.niftynotification.NotifyActivity
import com.wiser.animationlistdemo.niftynotification.api.Configuration
import com.wiser.animationlistdemo.niftynotification.api.Effects
import com.wiser.animationlistdemo.niftynotification.api.NiftyNotificationView
import com.wiser.animationlistdemo.recyclerviewanim.RecyclerViewAnimActivity
import com.wiser.animationlistdemo.search.JellyToolBarActivity
import com.wiser.animationlistdemo.textanim.TextAnimActivity
import com.wiser.animationlistdemo.toast.LoadToastActivity
import com.wiser.animationlistdemo.ticket.TicketAct

class MainActivity : AppCompatActivity() {

    private val TAG = "anim_Main"
    private var norificationView: NiftyNotificationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: 执行onResume 函数")
        norificationView?.hide()
    }

    fun skipLoginAnimPage(view: View) {
        startActivity(Intent(this, LoginPageActivity::class.java))
    }
    fun skipListAnimPage(view: View) {
        startActivity(Intent(this, RecyclerViewAnimActivity::class.java))
    }

    fun skipNotifyAnimPage(view: View) {
        startActivity(Intent(this, NotifyActivity::class.java))
    }

    fun skipSearchPage(view: View) {
        startActivity(Intent(this, JellyToolBarActivity::class.java))
    }

    fun skipToastPage(view: View) {
        startActivity(Intent(this, LoadToastActivity::class.java))
    }

    fun skipTextAnimPage(view: View) {
        startActivity(Intent(this, TextAnimActivity::class.java))
    }

    fun skipDownloadAnimPage(view: View) {
        startActivity(Intent(this, DownloadActivity::class.java))
    }

    fun skipCard3DRotate(view: View) {
        startActivity(Intent(this, CardRotateActivity::class.java))
    }


    fun skipAudioAnimPage(v:View){
        startActivity(Intent(this, AudioMainAct::class.java))

    }

    fun menu(v:View){
        startActivity(Intent(this, MenuAct::class.java))

    }

    fun home(v:View){
        startActivity(Intent(this,HomeActivity::class.java))
    }

    fun ticket(v:View){
        startActivity(Intent(this,TicketAct::class.java))
    }

    fun list(v:View){
        startActivity(Intent(this,HomeAct::class.java))
    }

    fun permission(v: View) {
        when  {
            ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "permission: 权限已申请成功.")
            }
            else -> {
                val customView = LayoutInflater.from(this).inflate(R.layout.view_permission_notification, null)
                norificationView = NiftyNotificationView.build(
                    this,
                    Effects.slideOnTop,
                    R.id.notify_rl,
                    customView as ViewGroup,
                    Configuration.Builder().setBackgroundColor("#00fff0").setTextColor("#ff00ff").build()
                )
                    .setIcon(R.drawable.notify_bg)
                norificationView?.show()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100002)
            }
        }
    }

    fun skipDetailsPage(view: View) {
        startActivity(Intent(this, DetailsPageActivity::class.java))
    }
}