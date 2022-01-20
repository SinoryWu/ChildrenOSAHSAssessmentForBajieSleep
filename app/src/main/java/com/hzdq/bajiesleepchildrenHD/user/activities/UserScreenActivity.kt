package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUserScreenBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI


class UserScreenActivity : AppCompatActivity() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:ActivityUserScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_screen)
        ActivityCollector2.addActivity(this)
        ActivityCollector.addActivity(this)
        val intent = intent
        val uid = intent.getIntExtra("uid",0)
        val name = intent.getStringExtra("name")

        binding.userScreenOSA.setOnClickListener {
            val intent = Intent(this,UserScreenAddActivity::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("addScreen",1)
            startActivityForResult(intent,1)
//            startActivity(Intent(this,CheckOSA_Activity::class.java))
        }

        binding.userScreenBack.setOnClickListener {
            finish()
            ActivityCollector.removeActivity(this)
        }

        binding.userScreenPSQ.setOnClickListener {
            val intent = Intent(this,UserScreenAdd2Activity::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("addScreen",1)
            startActivityForResult(intent,1)
//            startActivity(Intent(this,CheckPSQ_Activity::class.java))
        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            1 -> {
                if (resultCode == RESULT_OK){
                    setResult(RESULT_OK)
                    ActivityCollector2.removeActivity(this)
                    finish()
                }
            }

        }
    }
}