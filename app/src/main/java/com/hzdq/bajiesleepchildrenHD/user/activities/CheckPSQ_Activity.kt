package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckPsqBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataclassResultPSQDetail
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckPSQ_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckPsqBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp: Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_check_psq)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()

        binding.back.setOnClickListener {
            finish()
        }
        shp = Shp(this)
        val retrofitSingleton = RetrofitSingleton.getInstance(this)
        val intent = intent
        val task_id = intent.getIntExtra("taskid",0)
        Log.d("psqtaskid", "onCreate: $task_id")
        binding.questionDoctor.text = "管理医生：${shp.getDoctorName()}"

        if (task_id == 0){
            val bundle = intent.getBundleExtra("bundle")
            val measures = bundle?.getString("measures")
            val result = bundle?.getString("result")
            val total = bundle?.getInt("total")
            val createTime = bundle?.getInt("createTime")
            val name = bundle?.getString("name")
            val list = bundle?.getIntegerArrayList("list")

            binding.advice.text = measures
            binding.score.text = "$total"
            binding.result.text = result

            binding.createTime.text = "${timestamp2Date("$createTime","yyyy/MM/dd HH:mm")}"

            binding.questionChoiceName.text = name


//        val list = listOf<Int>(1,2,2,1,2,1,2,1,2,2,2,1,2,1,2,2,1,2,1,2,2,1)


            for (i in 0..list?.size!!){
                when(i){
                    0 -> setLineb1(list[i])
                    1 -> setLineb2(list[i])
                    2-> setLineb3(list[i])
                    3 -> setLineb4(list[i])
                    4 -> setLineb5(list[i])
                    5 -> setLineb6(list[i])
                    6 -> setLineb7(list[i])
                    7 -> setLineb8(list[i])
                    8 -> setLineb9(list[i])
                    9 -> setLineb10(list[i])
                    10 -> setLineb11(list[i])
                    11 -> setLineb12(list[i])
                    12 -> setLineb13(list[i])
                    13 -> setLineb14(list[i])
                    14 -> setLineb15(list[i])
                    15 -> setLineb16(list[i])
                    16 -> setLineb17(list[i])
                    17 -> setLineb18(list[i])
                    18 -> setLineb19(list[i])
                    19 -> setLineb20(list[i])
                    20 -> setLineb21(list[i])
                    21 -> setLineb22(list[i])




                }
            }
        }else {
            getPSQDetail(retrofitSingleton,task_id)
        }

    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }
    /**
     * 获取PSQ详情
     */

    fun getPSQDetail(retrofitSingleton: RetrofitSingleton,taskid:Int){


        retrofitSingleton.api().getPsqResultDetail(taskid).enqueue(object :Callback<DataclassResultPSQDetail>{
            override fun onResponse(
                call: Call<DataclassResultPSQDetail>,
                response: Response<DataclassResultPSQDetail>
            ) {
               if (response.body()?.code == 0){

                   binding.advice.text = response.body()?.data?.measures
                   binding.score.text = "${response.body()?.data?.total}分"
                   binding.result.text = response.body()?.data?.result

                   binding.createTime.text = "${timestamp2Date("${response.body()?.data?.createTime}","yyyy/MM/dd HH:mm")}"

                   binding.questionChoiceName.text = response.body()?.data?.truename

                   response.body()?.data?.content?.x127?.let { setLineb1(it) }
                   response.body()?.data?.content?.x128?.let { setLineb2(it) }
                   response.body()?.data?.content?.x129?.let { setLineb3(it) }
                   response.body()?.data?.content?.x130?.let { setLineb4(it) }
                   response.body()?.data?.content?.x131?.let { setLineb5(it) }
                   response.body()?.data?.content?.x132?.let { setLineb6(it) }
                   response.body()?.data?.content?.x133?.let { setLineb7(it) }
                   response.body()?.data?.content?.x134?.let { setLineb8(it) }
                   response.body()?.data?.content?.x135?.let { setLineb9(it) }
                   response.body()?.data?.content?.x136?.let { setLineb10(it) }
                   response.body()?.data?.content?.x137?.let { setLineb11(it) }
                   response.body()?.data?.content?.x138?.let { setLineb12(it) }
                   response.body()?.data?.content?.x139?.let { setLineb13(it) }
                   response.body()?.data?.content?.x140?.let { setLineb14(it) }

                   response.body()?.data?.content?.x141?.let { setLineb15(it) }
                   response.body()?.data?.content?.x142?.let { setLineb16(it) }
                   response.body()?.data?.content?.x143?.let { setLineb17(it) }
                   response.body()?.data?.content?.x144?.let { setLineb18(it) }
                   response.body()?.data?.content?.x145?.let { setLineb19(it) }
                   response.body()?.data?.content?.x146?.let { setLineb20(it) }
                   response.body()?.data?.content?.x147?.let { setLineb21(it) }
                   response.body()?.data?.content?.x148?.let { setLineb22(it) }

               } else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                   if (tokenDialog == null) {
                       tokenDialog = TokenDialog(
                           this@CheckPSQ_Activity,
                           object : TokenDialog.ConfirmAction {
                               override fun onRightClick() {
                                   shp.saveToSp("token", "")
                                   shp.saveToSp("uid", "")
                                   startActivity(
                                       Intent(
                                           this@CheckPSQ_Activity,
                                           LoginActivity::class.java
                                       )
                                   )
                                   ActivityCollector2.finishAll()
                               }
                           })
                       tokenDialog!!.show()
                       tokenDialog!!.setCanceledOnTouchOutside(false)
                   } else {
                       tokenDialog!!.show()
                       tokenDialog!!.setCanceledOnTouchOutside(false)
                   }
               }else {
                   ToastUtils.showTextToast(this@CheckPSQ_Activity,"${response.body()?.msg}")
               }
            }

            override fun onFailure(call: Call<DataclassResultPSQDetail>, t: Throwable) {
                ToastUtils.showTextToast(this@CheckPSQ_Activity,"查看PSQ详情网络请求失败")
            }

        })
    }


    private fun setLineb1(x:Int){

        when(x){
            1 -> binding.bb11.isSelected = true
            0 -> binding.bb12.isSelected = true
        }
    }

    private fun setLineb2(x:Int){
        when(x){
            1 -> binding.b21.isSelected =true
            0 -> binding.b22.isSelected =true
        }
    }

    private fun setLineb3(x:Int){
        when(x){
            1 -> binding.b31.isSelected =true
            0 -> binding.b32.isSelected =true
        }
    }

    private fun setLineb4(x:Int){
        when(x){
            1 -> binding.b41.isSelected =true
            0 -> binding.b42.isSelected =true
        }
    }
    private fun setLineb5(x:Int){
        when(x){
            1 -> binding.b51.isSelected =true
            0 -> binding.b52.isSelected =true
        }
    }

    private fun setLineb6(x:Int){
        when(x){
            1 -> binding.b61.isSelected =true
            0 -> binding.b62.isSelected =true
        }
    }

    private fun setLineb7(x:Int){
        when(x){
            1 -> binding.b71.isSelected =true
            0 -> binding.b72.isSelected =true
        }
    }

    private fun setLineb8(x:Int){
        when(x){
            1 -> binding.b81.isSelected =true
            0 -> binding.b82.isSelected =true
        }
    }

    private fun setLineb9(x:Int){
        when(x){
            1 -> binding.b91.isSelected =true
            0 -> binding.b92.isSelected =true
        }
    }

    private fun setLineb10(x:Int){
        when(x){
            1 -> binding.b101.isSelected =true
            0 -> binding.b102.isSelected =true
        }
    }

    private fun setLineb11(x:Int){
        when(x){
            1 -> binding.b111.isSelected =true
            0 -> binding.b112.isSelected =true
        }
    }

    private fun setLineb12(x:Int){
        when(x){
            1 -> binding.b121.isSelected =true
            0 -> binding.b122.isSelected =true
        }
    }

    private fun setLineb13(x:Int){
        when(x){
            1 -> binding.b131.isSelected =true
            0 -> binding.b132.isSelected =true
        }
    }

    private fun setLineb14(x:Int){
        when(x){
            1 -> binding.b141.isSelected =true
            0 -> binding.b142.isSelected =true
        }
    }
    private fun setLineb15(x:Int){
        when(x){
            1 -> binding.b151.isSelected =true
            0 -> binding.b152.isSelected =true
        }
    }

    private fun setLineb16(x:Int){
        when(x){
            1 -> binding.b161.isSelected =true
            0 -> binding.b162.isSelected =true
        }
    }

    private fun setLineb17(x:Int){
        when(x){
            1 -> binding.b171.isSelected =true
            0 -> binding.b172.isSelected =true
        }
    }

    private fun setLineb18(x:Int){
        when(x){
            1 -> binding.b181.isSelected =true
            0 -> binding.b182.isSelected =true
        }
    }

    private fun setLineb19(x:Int){
        when(x){
            1 -> binding.b191.isSelected =true
            0 -> binding.b192.isSelected =true
        }
    }

    private fun setLineb20(x:Int){
        when(x){
            1 -> binding.b201.isSelected =true
            0 -> binding.b202.isSelected =true
        }
    }

    private fun setLineb21(x:Int){
        when(x){
            1 -> binding.b211.isSelected =true
            0 -> binding.b212.isSelected =true
        }
    }

    private fun setLineb22(x:Int){
        when(x){
            1 -> binding.b221.isSelected =true
            0 -> binding.b222.isSelected =true
        }
    }
}