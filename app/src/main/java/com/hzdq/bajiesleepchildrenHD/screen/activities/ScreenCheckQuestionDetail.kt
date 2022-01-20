package com.hzdq.bajiesleepchildrenHD.screen.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckQuestionDetailBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI

class ScreenCheckQuestionDetail : AppCompatActivity() {
    private lateinit var binding:ActivityCheckQuestionDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_question_detail)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

}