package com.hzdq.bajiesleepchildrenHD.evaluate.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityEvaluateBinding
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp

class EvaluateActivity : AppCompatActivity() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var navController: NavController
    private lateinit var evaluateViewModel:EvaluateViewModel
    private lateinit var binding:ActivityEvaluateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        HideUI(this).hideSystemUI()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_evaluate)
        ActivityCollector2.addActivity(this)
        val intent = intent
        val patientid = intent.getIntExtra("patientid",0)
        val name = intent.getStringExtra("name")
        navController = Navigation.findNavController(this,R.id.evaluate_fragment)
        evaluateViewModel = ViewModelProvider(this).get(EvaluateViewModel::class.java)
        binding.back.setOnClickListener {
            ActivityCollector2.removeActivity(this)
            finish()
        }

        evaluateViewModel.patient_id.value = patientid
        evaluateViewModel.name.value = name


    }

}