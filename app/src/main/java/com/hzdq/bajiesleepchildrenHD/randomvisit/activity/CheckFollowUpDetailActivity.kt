package com.hzdq.bajiesleepchildrenHD.randomvisit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckFollowUpDetailBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFollowUpDetail
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.activities.CheckOSA_Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.CheckPSQ_Activity
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckFollowUpDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckFollowUpDetailBinding
    private lateinit var randomViewModel: RandomViewModel
    private lateinit var retrofitSingleton: RetrofitSingleton
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp:Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_follow_up_detail)
        randomViewModel = ViewModelProvider(this).get(RandomViewModel::class.java)

        HideUI(this).hideSystemUI()
        retrofitSingleton = RetrofitSingleton.getInstance(this)
        ActivityCollector2.addActivity(this)
        val intent = intent
        val id = intent.getIntExtra("id",0)

        shp = Shp(this)

        getFollowUpDetail(retrofitSingleton,id)

        binding.back.setOnClickListener {
            finish()
        }
        randomViewModel.height.observe(this, Observer {
            if (it.equals("")){
                binding.height.text = "--"
            }else {
                binding.height.text = it
            }
        })

        randomViewModel.weight.observe(this, Observer {
            if (it.equals("")){
                binding.weight.text = "--"
            }else {
                binding.weight.text = it
            }
        })

        randomViewModel.bmi.observe(this, Observer {
            if (it.equals("")){
                binding.bmi.text = "--"
            }else {
                binding.bmi.text = it
            }
        })

        randomViewModel.oahi.observe(this, Observer {
            if (it.equals("")){
                binding.oahi.text = "--"
            }else {
                binding.oahi.text = it
            }
        })

        randomViewModel.neck.observe(this, Observer {
            if (it.equals("")){
                binding.neck.text = "--"
            }else {
                binding.neck.text = it
            }
        })

        val map = mapOf(
            1 to  binding.osaResult,
            2 to binding.psqResult
        )
        randomViewModel.assessmentList.observe(this, Observer {
            map.forEach {
                it.value.text = "--"
            }
            for (i in 0..it.size-1){
                if (it[i].name.equals("OSA-18")){
                    binding.osaResult.text = "得分：${it[i].score}；结果：${it[i].result}"
                    if (it[i].id == 0){
                        binding.osaButton.visibility = View.GONE
                    }else {
                        binding.osaButton.visibility = View.VISIBLE
                        randomViewModel.osaTaskId.value = it[i].id
                    }

                }else if (it[i].name.equals("PSQ")){
                    binding.psqResult.text = "得分：${it[i].score}；结果：${it[i].result}"
                    if (it[i].id == 0){
                        binding.psqButton.visibility = View.GONE
                    }else {
                        binding.psqButton.visibility = View.VISIBLE
                        randomViewModel.psqTaskId.value = it[i].id
                    }
                }
            }
        })

        randomViewModel.suspend.observe(this, Observer {
            when(it){
                1 -> {
                    binding.suspend.text ="否"
                    binding.textViewReason.text = "下次随访："
                    binding.reason.text = "${timestamp2Date("${randomViewModel.next.value.toString()}","yyyy年MM月dd日")}"
                }
                2 -> {
                    binding.suspend.text ="是"
                    binding.textViewReason.text = "终止原因："

                    binding.reason.text = randomViewModel.reason.value
                }
                else -> {
                    binding.suspend.text ="--"
                }
            }
        })



        randomViewModel.createTime.observe(this, Observer {
            if (it == 0){
                binding.time.text = "--"
            }else {
                binding.time.text = "${timestamp2Date(it.toString(),"yyyy/MM/dd HH:mm:ss")}"
            }

//            binding.time.text = "${timestamp2Date(it,"yyyy/MM/dd HH:mm:ss")}"
        })

        randomViewModel.doctorName.observe(this, Observer {
            if (it.equals("")){
                binding.doctor.text = "管理医生：--"
            }else {
                binding.doctor.text = "管理医生：${it}"
            }
        })

        binding.osaButton.setOnClickListener {
            val intent = Intent(this,CheckOSA_Activity::class.java)
            intent.putExtra("taskid", randomViewModel.osaTaskId.value)
            startActivity(intent)
        }

        binding.psqButton.setOnClickListener {
            val intent = Intent(this,CheckPSQ_Activity::class.java)
            intent.putExtra("taskid", randomViewModel.psqTaskId.value)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

   fun getFollowUpDetail(retrofitSingleton: RetrofitSingleton,id:Int){
       retrofitSingleton.api().getFollowUpInfo(id).enqueue(object :Callback<DataClassFollowUpDetail>{
           override fun onResponse(
               call: Call<DataClassFollowUpDetail>,
               response: Response<DataClassFollowUpDetail>
           ) {



               if (response.body()?.code == 0){
                   randomViewModel.height.value = response.body()?.data?.height
                   randomViewModel.weight.value = response.body()?.data?.weight
                   randomViewModel.neck.value = response.body()?.data?.neck

                   randomViewModel.oahi.value = response.body()?.data?.oahi

                   randomViewModel.bmi.value =  response.body()?.data?.bmi

                   randomViewModel.assessmentList.value = response.body()?.data?.assessment
                   randomViewModel.reason.value = response.body()?.data?.reason
                   randomViewModel.next.value = response.body()?.data?.next.toString()



                   randomViewModel.createTime.value = response.body()?.data?.createTime

                   randomViewModel.doctorName.value = response.body()?.data?.doctorName


                   randomViewModel.suspend.value = response.body()?.data?.suspend
               }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                   if (tokenDialog == null) {
                       tokenDialog = TokenDialog(this@CheckFollowUpDetailActivity, object : TokenDialog.ConfirmAction {
                           override fun onRightClick() {
                               shp.saveToSp("token", "")
                               shp.saveToSp("uid", "")


                               startActivity(
                                   Intent(this@CheckFollowUpDetailActivity,
                                       LoginActivity::class.java)
                               )
                               ActivityCollector2.finishAll()
                           }

                       })
                       tokenDialog!!.show()
                       tokenDialog?.setCanceledOnTouchOutside(false)
                   } else {
                       tokenDialog!!.show()
                       tokenDialog?.setCanceledOnTouchOutside(false)
                   }
               }else {
                   ToastUtils.showTextToast(this@CheckFollowUpDetailActivity,"${response.body()?.msg}")
               }
           }

           override fun onFailure(call: Call<DataClassFollowUpDetail>, t: Throwable) {
               ToastUtils.showTextToast(this@CheckFollowUpDetailActivity,"随访详情网络请求失败")
           }

       })
   }
}