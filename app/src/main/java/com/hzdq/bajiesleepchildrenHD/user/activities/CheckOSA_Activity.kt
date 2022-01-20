package com.hzdq.bajiesleepchildrenHD.user.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckOsaBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassResultOSADetail
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2.finishAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckOSA_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckOsaBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp: Shp
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_osa)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        binding.back.setOnClickListener {
            finish()
        }
        val retrofitSingleton = RetrofitSingleton.getInstance(this)
        shp = Shp(this)
//        bundle.putString("list",measures)
//        bundle.putString("result",result)
//        bundle.putInt("total", total!!)
//        bundle.putInt("createTime", createTime!!)
        val intent = intent

        val taskid = intent.getIntExtra("taskid", 0)
        Log.d("osataskid", "onCreate: $taskid")
//        binding.advice.text = measures
//        binding.score.text = "$total"
//        binding.result.text = result

//        binding.createTime.text = "${timestamp2Date("$createTime","yyyy/MM/dd HH:mm")}"

//        binding.questionChoiceName.text = name

        binding.questionDoctor.text = "管理医生：${shp.getDoctorName()}"

        if (taskid == 0){
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

            for (i in 1..list?.size!!){
                when(i){
                    1 -> setLineb1(list[i-1])
                    2 -> setLineb2(list[i-1])
                    3 -> setLineb3(list[i-1])
                    4 -> setLineb4(list[i-1])
                    5 -> setLineb5(list[i-1])
                    6 -> setLineb6(list[i-1])
                    7 -> setLineb7(list[i-1])
                    8 -> setLineb8(list[i-1])
                    9 -> setLineb9(list[i-1])
                    10 -> setLineb10(list[i-1])
                    11 -> setLineb11(list[i-1])
                    12 -> setLineb12(list[i-1])
                    13 -> setLineb13(list[i-1])
                    14 -> setLineb14(list[i-1])
                    15 -> setLineb15(list[i-1])
                    16 -> setLineb16(list[i-1])
                    17 -> setLineb17(list[i-1])
                    18 -> setLineb18(list[i-1])
                }
            }

        }else {
            getDetail(retrofitSingleton,taskid)
        }


    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }
    /**
     * 获取结果详情
     */
    fun getDetail(retrofitSingleton: RetrofitSingleton,taskid:Int){


        retrofitSingleton.api().getOsaResultDetail(taskid).enqueue(object :Callback<DataClassResultOSADetail>{
            override fun onResponse(
                call: Call<DataClassResultOSADetail>,
                response: Response<DataClassResultOSADetail>
            ) {
                if (response.body() != null){
                    if (response.body()?.code == 0){

                        Log.d("checkosa", "onResponse:${response.body()} ")
                        binding.questionChoiceName.text = response.body()?.data?.truename
                        binding.advice.text = response.body()?.data?.measures
                        binding.score.text = "${response.body()?.data?.total}分"
                        binding.result.text = response.body()?.data?.result
                        binding.createTime.text = "${timestamp2Date("${response.body()?.data?.createTime}","yyyy/MM/dd HH:mm")}"
                        response.body()?.data?.content?.x150?.let { setLineb1(it) }
                        response.body()?.data?.content?.x151?.let { setLineb2(it) }
                        response.body()?.data?.content?.x152?.let { setLineb3(it) }
                        response.body()?.data?.content?.x153?.let { setLineb4(it) }
                        response.body()?.data?.content?.x154?.let { setLineb5(it) }
                        response.body()?.data?.content?.x155?.let { setLineb6(it) }
                        response.body()?.data?.content?.x156?.let { setLineb7(it) }
                        response.body()?.data?.content?.x157?.let { setLineb8(it) }
                        response.body()?.data?.content?.x158?.let { setLineb9(it) }
                        response.body()?.data?.content?.x159?.let { setLineb10(it) }
                        response.body()?.data?.content?.x160?.let { setLineb11(it) }
                        response.body()?.data?.content?.x161?.let { setLineb12(it) }
                        response.body()?.data?.content?.x162?.let { setLineb13(it) }
                        response.body()?.data?.content?.x163?.let { setLineb14(it) }
                        response.body()?.data?.content?.x164?.let { setLineb15(it) }
                        response.body()?.data?.content?.x163?.let { setLineb16(it) }
                        response.body()?.data?.content?.x166?.let { setLineb17(it) }
                        response.body()?.data?.content?.x167?.let { setLineb18(it) }

                    } else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(
                                this@CheckOSA_Activity,
                                object : TokenDialog.ConfirmAction {
                                    override fun onRightClick() {
                                        shp.saveToSp("token", "")
                                        shp.saveToSp("uid", "")
                                        startActivity(
                                            Intent(
                                                this@CheckOSA_Activity,
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
                        ToastUtils.showTextToast(this@CheckOSA_Activity,"${response.body()?.msg}")
                    }
                }

            }

            override fun onFailure(call: Call<DataClassResultOSADetail>, t: Throwable) {
                ToastUtils.showTextToast(this@CheckOSA_Activity,"查看OSA详情网络请求失败")
            }

        })
    }

    private fun setLineb1(x:Int){
        when(x){
            1 -> binding.b11.isSelected = true
            2 -> binding.b12.isSelected = true
            3 -> binding.b13.isSelected = true
            4 -> binding.b14.isSelected = true
            5 -> binding.b15.isSelected = true
            6 -> binding.b16.isSelected = true
            7 -> binding.b17.isSelected = true
        }

    }

    private fun setLineb2(x:Int){
        when(x){
            1 -> binding.b21.isSelected = true
            2 -> binding.b22.isSelected = true
            3 -> binding.b23.isSelected = true
            4 -> binding.b24.isSelected = true
            5 -> binding.b25.isSelected = true
            6 -> binding.b26.isSelected = true
            7 -> binding.b27.isSelected = true
        }

    }
    private fun setLineb3(x:Int){
        when(x){
            1 -> binding.b31.isSelected = true
            2 -> binding.b32.isSelected = true
            3 -> binding.b33.isSelected = true
            4 -> binding.b34.isSelected = true
            5 -> binding.b35.isSelected = true
            6 -> binding.b36.isSelected = true
            7 -> binding.b37.isSelected = true
        }

    }

    private fun setLineb4(x:Int){
        when(x){
            1 -> binding.b41.isSelected = true
            2 -> binding.b42.isSelected = true
            3 -> binding.b43.isSelected = true
            4 -> binding.b44.isSelected = true
            5 -> binding.b45.isSelected = true
            6 -> binding.b46.isSelected = true
            7 -> binding.b47.isSelected = true
        }

    }

    private fun setLineb5(x:Int){
        when(x){
            1 -> binding.b51.isSelected = true
            2 -> binding.b52.isSelected = true
            3 -> binding.b53.isSelected = true
            4 -> binding.b54.isSelected = true
            5 -> binding.b55.isSelected = true
            6 -> binding.b56.isSelected = true
            7 -> binding.b57.isSelected = true
        }

    }

    private fun setLineb6(x:Int){
        when(x){
            1 -> binding.b61.isSelected = true
            2 -> binding.b62.isSelected = true
            3 -> binding.b63.isSelected = true
            4 -> binding.b64.isSelected = true
            5 -> binding.b65.isSelected = true
            6 -> binding.b66.isSelected = true
            7 -> binding.b67.isSelected = true
        }

    }

    private fun setLineb7(x:Int){
        when(x){
            1 -> binding.b71.isSelected = true
            2 -> binding.b72.isSelected = true
            3 -> binding.b73.isSelected = true
            4 -> binding.b74.isSelected = true
            5 -> binding.b75.isSelected = true
            6 -> binding.b76.isSelected = true
            7 -> binding.b77.isSelected = true
        }

    }

    private fun setLineb8(x:Int){
        when(x){
            1 -> binding.b81.isSelected = true
            2 -> binding.b82.isSelected = true
            3 -> binding.b83.isSelected = true
            4 -> binding.b84.isSelected = true
            5 -> binding.b85.isSelected = true
            6 -> binding.b86.isSelected = true
            7 -> binding.b87.isSelected = true
        }

    }

    private fun setLineb9(x:Int){
        when(x){
            1 -> binding.b91.isSelected = true
            2 -> binding.b92.isSelected = true
            3 -> binding.b93.isSelected = true
            4 -> binding.b94.isSelected = true
            5 -> binding.b95.isSelected = true
            6 -> binding.b96.isSelected = true
            7 -> binding.b97.isSelected = true
        }

    }

    private fun setLineb10(x:Int){
        when(x){
            1 -> binding.b101.isSelected = true
            2 -> binding.b102.isSelected = true
            3 -> binding.b103.isSelected = true
            4 -> binding.b104.isSelected = true
            5 -> binding.b105.isSelected = true
            6 -> binding.b106.isSelected = true
            7 -> binding.b107.isSelected = true
        }

    }


    private fun setLineb11(x:Int){
        when(x){
            1 -> binding.b111.isSelected = true
            2 -> binding.b112.isSelected = true
            3 -> binding.b113.isSelected = true
            4 -> binding.b114.isSelected = true
            5 -> binding.b115.isSelected = true
            6 -> binding.b116.isSelected = true
            7 -> binding.b117.isSelected = true
        }

    }

    private fun setLineb12(x:Int){
        when(x){
            1 -> binding.b121.isSelected = true
            2 -> binding.b122.isSelected = true
            3 -> binding.b123.isSelected = true
            4 -> binding.b124.isSelected = true
            5 -> binding.b125.isSelected = true
            6 -> binding.b126.isSelected = true
            7 -> binding.b127.isSelected = true
        }

    }


    private fun setLineb13(x:Int){
        when(x){
            1 -> binding.b131.isSelected = true
            2 -> binding.b132.isSelected = true
            3 -> binding.b133.isSelected = true
            4 -> binding.b134.isSelected = true
            5 -> binding.b135.isSelected = true
            6 -> binding.b136.isSelected = true
            7 -> binding.b137.isSelected = true
        }

    }

    private fun setLineb14(x:Int){
        when(x){
            1 -> binding.b141.isSelected = true
            2 -> binding.b142.isSelected = true
            3 -> binding.b143.isSelected = true
            4 -> binding.b144.isSelected = true
            5 -> binding.b145.isSelected = true
            6 -> binding.b146.isSelected = true
            7 -> binding.b147.isSelected = true
        }

    }

    private fun setLineb15(x:Int){
        when(x){
            1 -> binding.b151.isSelected = true
            2 -> binding.b152.isSelected = true
            3 -> binding.b153.isSelected = true
            4 -> binding.b154.isSelected = true
            5 -> binding.b155.isSelected = true
            6 -> binding.b156.isSelected = true
            7 -> binding.b157.isSelected = true
        }

    }

    private fun setLineb16(x:Int){
        when(x){
            1 -> binding.b161.isSelected = true
            2 -> binding.b162.isSelected = true
            3 -> binding.b163.isSelected = true
            4 -> binding.b164.isSelected = true
            5 -> binding.b165.isSelected = true
            6 -> binding.b166.isSelected = true
            7 -> binding.b167.isSelected = true
        }

    }

    private fun setLineb17(x:Int){
        when(x){
            1 -> binding.b171.isSelected = true
            2 -> binding.b172.isSelected = true
            3 -> binding.b173.isSelected = true
            4 -> binding.b174.isSelected = true
            5 -> binding.b175.isSelected = true
            6 -> binding.b176.isSelected = true
            7 -> binding.b177.isSelected = true
        }

    }

    private fun setLineb18(x:Int){
        when(x){
            1 -> binding.b181.isSelected = true
            2 -> binding.b182.isSelected = true
            3 -> binding.b183.isSelected = true
            4 -> binding.b184.isSelected = true
            5 -> binding.b185.isSelected = true
            6 -> binding.b186.isSelected = true
            7 -> binding.b187.isSelected = true
        }

    }














}