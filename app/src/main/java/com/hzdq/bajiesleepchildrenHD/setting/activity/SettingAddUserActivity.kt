package com.hzdq.bajiesleepchildrenHD.setting.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.MainViewModel
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivitySettingAddUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.HashMap

class SettingAddUserActivity : AppCompatActivity() {
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var binding:ActivitySettingAddUserBinding
    private lateinit var retrofitSingleton: RetrofitSingleton
    private lateinit var shp: Shp
    private var tokenDialog: TokenDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting_add_user)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        retrofitSingleton = RetrofitSingleton.getInstance(this)
        shp = Shp(this)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        binding.back.setOnClickListener {
            finish()
        }
        binding.cancel.setOnClickListener {
            finish()
        }

        binding.hospitalName.text = shp.getHospitalName()

        binding.name.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                settingViewModel.accountName.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.mobile.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                settingViewModel.accountMobile.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.confirm.setOnClickListener {

            if (settingViewModel.accountName.equals("")){
                Toast.makeText(this,"请输入姓名", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!settingViewModel.accountMobile.equals("") && PhoneFormatCheckUtils.isPhoneLegal(settingViewModel.accountMobile.value) == true) {

                val url = OkhttpSingleton.BASE_URL+"/v2/auserSave"
                val map = HashMap<String, String>()
                map["hospitalid"] = shp.getHospitalId().toString()
                map["type"] = "8"
                map["truename"] = settingViewModel.accountName.value!!
                map["mobile"] = settingViewModel.accountMobile.value!!

                postAddAuser(url, map)

            }else if (settingViewModel.accountMobile.value!!.equals("")){
                Toast.makeText(this,"手机号不能为空", Toast.LENGTH_SHORT).show()
            } else if (PhoneFormatCheckUtils.isPhoneLegal(settingViewModel.accountMobile.value) == false){
                Toast.makeText(this,"请输入正确手机号", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }


    /**
     * 添加管理员
     */

    fun postAddAuser(url:String,map: HashMap<String, String>) {
        //1.拿到okhttp对象


        //2.构造request
        //2.1构造requestbody
        val params = HashMap<String?, Any?>()

        val keys: Set<String> = map.keys
        for (key in keys) {
            params[key] = map[key]
        }

        Log.e("params:", params.toString())
        val jsonObject = JSONObject(params)
        val jsonStr = jsonObject.toString()

        val requestBodyJson =
            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr)

        val request = Request.Builder()
            .addHeader("User-Agent", shp.getUserAgent())
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
            .url(url)
            .post(requestBodyJson)
            .build()
        //3.将request封装为call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                runOnUiThread { ToastUtils.showTextToast2(this@SettingAddUserActivity, "添加管理员网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()

                //封装java对象
                val dataClassAddAuser = DataClassAddAuser()
                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("msg")
                    val data = jsonObject.optJSONObject("data")

                    //第一层封装
                    dataClassAddAuser.code = code
                    dataClassAddAuser.msg = msg
                    if (data != null) {
                        dataClassAddAuser.data = data.toString()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                runOnUiThread {
                    if (dataClassAddAuser.code == 0) {
                        ToastUtils.showTextToast(this@SettingAddUserActivity,"操作成功")
                        setResult(RESULT_OK)
                        finish()

                    }else if ( dataClassAddAuser.code == 10010 ||  dataClassAddAuser.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@SettingAddUserActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@SettingAddUserActivity,
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
                    } else {
                        ToastUtils.showTextToast(
                            this@SettingAddUserActivity,
                            "${dataClassAddAuser.msg}"
                        )
                    }
                }
            }
        })
    }

}