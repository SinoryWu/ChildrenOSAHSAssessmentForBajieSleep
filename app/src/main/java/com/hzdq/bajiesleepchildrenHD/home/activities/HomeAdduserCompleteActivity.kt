package com.hzdq.bajiesleepchildrenHD.home.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class HomeAdduserCompleteActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private var tokenDialog: TokenDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_adduser_complete)
        navController = Navigation.findNavController(this,R.id.complete_fragment)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        ActivityCollector2.removeActivity(this)
        homeBindDeviceViewModel = ViewModelProvider(this).get(HomeBindDeviceViewModel::class.java)
        val intent = intent
        val uid = intent.getIntExtra("uid",0)
        val name = intent.getStringExtra("name")
        homeBindDeviceViewModel.name.value = name!!
        homeBindDeviceViewModel.uid.value = uid

    }

    override fun onNavigateUp(): Boolean {
        if (navController.currentDestination?.id == R.id.homeAddUserCompleteFragment){
            ActivityCollector2.removeActivity(this)
            finish()
        }else{
            navController.navigateUp()
        }
        return super.onNavigateUp()

    }

    override fun onBackPressed() {
        onNavigateUp()
    }
}