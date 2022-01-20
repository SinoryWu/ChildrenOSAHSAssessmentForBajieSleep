package com.hzdq.bajiesleepchildrenHD.user.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityAddTreatmentRecordBinding
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.EPSoftKeyBoardListener
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import java.io.File
import java.util.*

class AddTreatmentRecordActivity : AppCompatActivity() {


    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:ActivityAddTreatmentRecordBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_treatment_record)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
//        EPSoftKeyBoardListener.setListener(this,object :EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener{
//            override fun keyBoardShow(height: Int) {
//                binding.addTreatmentMotion.transitionToEnd()
//            }
//
//            override fun keyBoardHide(height: Int) {
//                binding.addTreatmentMotion.transitionToStart()
//            }
//
//        })
        binding.addTreatmentBack.setOnClickListener {
            finish()
        }

        val map = mapOf(
            1 to binding.normal,
            2 to binding.light,
            3 to binding.moderate,
            4 to binding.severe
        )

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        userViewModel.addTreatmentDegree.observe(this, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 -> {
                    binding.normal.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                2 -> {
                    binding.light.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                3 -> {
                    binding.moderate.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                4 -> {
                    binding.severe.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.normal.setOnClickListener { userViewModel.addTreatmentDegree.value = 1 }
        binding.light.setOnClickListener { userViewModel.addTreatmentDegree.value = 2 }
        binding.moderate.setOnClickListener { userViewModel.addTreatmentDegree.value = 3 }
        binding.severe.setOnClickListener { userViewModel.addTreatmentDegree.value = 4 }
    }

    override fun onDestroy() {
        ActivityCollector2.addActivity(this)
        super.onDestroy()
    }
}