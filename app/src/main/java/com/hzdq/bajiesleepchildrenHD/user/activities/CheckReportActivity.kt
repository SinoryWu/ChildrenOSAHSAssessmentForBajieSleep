package com.hzdq.bajiesleepchildrenHD.user.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckReportBinding
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.DataCleanManagerKotlin
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class CheckReportActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckReportBinding
    private lateinit var userViewModel:UserViewModel
    private lateinit var navController: NavController
    private var tokenDialog: TokenDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_report)
        ActivityCollector2.addActivity(this)
        navController = Navigation.findNavController(this,R.id.user_check_report_fragment)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val intent = intent
        val id =  intent.getIntExtra("id",0)
        userViewModel.uid.value = id
        val name =  intent.getStringExtra("name")
        userViewModel.name.value = name



    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()

        DataCleanManagerKotlin.cleanInternalCache(applicationContext)
    }
}