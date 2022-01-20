package com.hzdq.bajiesleepchildrenHD.device.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class BindUserActivity : AppCompatActivity() {
    private lateinit var controller: NavController
    private lateinit var deviceViewModel:DeviceViewModel
    private var tokenDialog: TokenDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_user)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        controller = Navigation.findNavController(this,R.id.device_fragment)
        val intent = intent
        val deviceSN = intent.getStringExtra("sn")
        val monitor = intent.getStringExtra("monitor")


//
        deviceViewModel.editDeviceSN.value = deviceSN

    }

    override fun onSupportNavigateUp(): Boolean {
        //如果在第一个fragment页面再按一次返回键就关闭当前activity
        if (controller.currentDestination?.id == R.id.bindUserFragment){
            ActivityCollector2.removeActivity(this)
            finish()

        }else{
            controller.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
    }



}