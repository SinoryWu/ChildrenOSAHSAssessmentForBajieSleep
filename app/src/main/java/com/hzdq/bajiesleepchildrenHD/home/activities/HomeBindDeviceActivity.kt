package com.hzdq.bajiesleepchildrenHD.home.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class HomeBindDeviceActivity : AppCompatActivity() {
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private var tokenDialog: TokenDialog? = null
    private lateinit var controller:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_bind_device)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
        homeBindDeviceViewModel = ViewModelProvider(this).get(HomeBindDeviceViewModel::class.java)
        controller = Navigation.findNavController(this,R.id.home_bind_device_fragment)
    }

    override fun onNavigateUp(): Boolean {
        //如果在第一个fragment页面再按一次返回键就关闭当前activity
        if (controller.currentDestination?.id == R.id.homeBindDeviceDeviceFragment){
            ActivityCollector2.removeActivity(this)
            finish()
        }else{
            controller.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onNavigateUp()

    }
}