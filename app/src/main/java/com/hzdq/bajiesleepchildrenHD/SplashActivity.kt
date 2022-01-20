package com.hzdq.bajiesleepchildrenHD

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val splashLogo = findViewById<ImageView>(R.id.splash_logo)
        val splashMotion = findViewById<MotionLayout>(R.id.splash_motion)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()


        if (Shp(this).getToken()?.length!! > 0 ){
            lifecycleScope.launch {
                delay(100)
                splashMotion.transitionToEnd()
                delay(700)
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }else {
            lifecycleScope.launch {
                delay(100)
                splashMotion.transitionToEnd()
                delay(700)
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }


    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }
}