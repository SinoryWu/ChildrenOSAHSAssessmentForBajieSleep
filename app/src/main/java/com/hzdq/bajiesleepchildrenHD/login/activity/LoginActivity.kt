package com.hzdq.bajiesleepchildrenHD.login.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityLoginBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.login.viewmodel.LoginViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var navController: NavController
    private lateinit var shp: Shp
    var messageButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        navController = findNavController(R.id.login_fragment)
        loginViewModel = ViewModelProvider(this).get(
            LoginViewModel::class.java
        )
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()

        shp = Shp(this)

        binding.version.text = "V${getVerName()}"

        EPSoftKeyBoardListener.setListener(this,
            object : EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener {
                override fun keyBoardShow(height: Int) {
                    binding.loginMotion.transitionToEnd()
                }

                override fun keyBoardHide(height: Int) {
                    binding.loginMotion.transitionToStart()
                }

            })


        binding.loginMessageAgree.setOnClickListener {
            binding.loginCheckbox.isChecked = !binding.loginCheckbox.isChecked
        }

        //隐私声明
        binding.loginPrivate.setOnClickListener {
            startActivity(Intent(this,PrivateLinkActivity::class.java))
        }

        //用户协议
        binding.loginUser.setOnClickListener {
            startActivity(Intent(this,UserLinkActivity::class.java))
        }



        binding.loginButtonLogin.setOnClickListener {
            if (binding.loginCheckbox.isChecked) {
                if (navController.currentDestination?.id == R.id.passWordFragment) {
                    if (loginViewModel.phoneNumber.isNotEmpty() && loginViewModel.passWord.isNotEmpty() && PhoneFormatCheckUtils.isPhoneLegal(
                            loginViewModel.phoneNumber
                        ) == true
                    ) {
                        val map = HashMap<String, String>()

                        map["username"] = loginViewModel.phoneNumber //18158188052

                        map["password"] = loginViewModel.passWord //111

//                        map["reg_id"] = "18071adc03ca4e71439"
                        val url: String = OkhttpSingleton.BASE_URL + "/v2/login/index"

                        getPasswordLogin("${url}?username=${loginViewModel.phoneNumber}&password=${loginViewModel.passWord}")
//                        retrofitSingleton.loginApi().getPasswordLogin(loginViewModel.phoneNumber,loginViewModel.passWord,"18071adc03ca4e71439").enqueue(object :Callback<PasswordDateClass>{
//                            override fun onResponse(
//                                call: Call<PasswordDateClass>,
//                                response: Response<PasswordDateClass>
//                            ) {

//                                if (response.body()?.code == 0){
//                                    response.body()?.data?.token?.let { it1 ->
//                                        shp.saveToSp("token",
//                                            it1
//                                        )
//                                    }
//                                    shp.saveToSp("uid","${response.body()?.data?.uid}")
//                                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
//                                }else {
//                                    ToastUtils.showTextToast(this@LoginActivity,response.body()?.msg)
//                                }
//                            }
//
//                            override fun onFailure(call: Call<PasswordDateClass>, t: Throwable) {
//                                ToastUtils.showTextToast(this@LoginActivity,"网络请求失败")
//                            }

//                        })


                    } else if (loginViewModel.phoneNumber.isEmpty()) {
                        Toast.makeText(this,"账号不能为空",Toast.LENGTH_SHORT).show()
                    } else if (PhoneFormatCheckUtils.isPhoneLegal(loginViewModel.phoneNumber) == false) {
                        Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show()
                    } else if (loginViewModel.passWord.isEmpty()) {
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
                    }

                } else if (navController.currentDestination?.id == R.id.messageFragment) {
                    if (loginViewModel.phoneNumber.isNotEmpty() && loginViewModel.messageCode.isNotEmpty() && PhoneFormatCheckUtils.isPhoneLegal(
                            loginViewModel.phoneNumber
                        ) == true
                    ) {

                        val map = HashMap<String, String>()
                        map["mobile"] = loginViewModel.phoneNumber //18158188052

                        map["code"] = loginViewModel.messageCode //111

                        map["reg_id"] = "18071adc03ca4e71439"
                        //        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
                        val url: String = OkhttpSingleton.BASE_URL + "/v2/login/check"

                        postResLogin(url, map)
//                        retrofitSingleton.loginApi().postMessageLogin(messageBody).enqueue(object :
//                            Callback<MessageDateClass> {
//
//                            override fun onResponse(
//                                call: Call<MessageDateClass>,
//                                response: Response<MessageDateClass>
//                            ) {

//                                if (response.body()?.code == 0){
//                                    response.body()?.data?.token?.let { it1 ->
//                                        shp.saveToSp("token",
//                                            it1
//                                        )
//                                    }
//                                    shp.saveToSp("uid","${response.body()?.data?.uid}")
//                                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
//                                    Log.d("loginResponse", "onResponse: ${response.body()}")
//                                }else {
//                                    ToastUtils.showTextToast(this@LoginActivity,"${response.body()?.msg}")
//                                }
//                            }
//
//                            override fun onFailure(call: Call<MessageDateClass>, t: Throwable) {
//                                ToastUtils.showTextToast(this@LoginActivity,"网络请求失败")
//                            }
//
//                        })
                    } else if (loginViewModel.phoneNumber.isEmpty()) {
                        Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show()
                    } else if (PhoneFormatCheckUtils.isPhoneLegal(loginViewModel.phoneNumber) == false) {
                        Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show()
                    } else if (loginViewModel.messageCode.isEmpty()) {
                        Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "请勾选已阅读并同意《隐私声明》《用户协议》", Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun postResLogin(url: String?, map: HashMap<String, String>) {

        //1.拿到okhttp对象
        val client = OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    .add("publicobject.com", "sha256/afwiKY3RxoMmLkuRW1l7QsPZTJPwDS2pdDROQjXw8ig=")
                    .build())
            .build()

        //2.构造request
        //2.1构造requestbody
        val params = HashMap<String?, Any?>()
        Log.e("params:", params.toString())
        val keys: Set<String> = map.keys
        for (key in keys) {
            params[key] = map[key]

        }
        val jsonObject = JSONObject(params)
        val jsonStr = jsonObject.toString()
        val requestBodyJson =
            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr)
//        Log.d("useragent", GetShp.getUserAgent(applicationContext))
        val request = Request.Builder()
            .addHeader("User-Agent", shp.getUserAgent())
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

                runOnUiThread { ToastUtils.showTextToast2(this@LoginActivity, "网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()
                runOnUiThread {
                    //                        L.e(res);
                    val gson = Gson()
                    val messageDateClass: MessageDateClass =
                        gson.fromJson(res, MessageDateClass::class.java)
                    if (messageDateClass.code == 0) {
                        messageDateClass.data.token.let { it1 ->
                            shp.saveToSp(
                                "token",
                                it1
                            )
                        }

                        Log.d("token", "onResponse: ${messageDateClass.data.token}")
                        shp.saveToSp("uid", "${messageDateClass.data.uid}")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                        finish()

                    } else {
                        val msg: String = messageDateClass.msg
                        ToastUtils.showTextToast2(this@LoginActivity, msg)
                    }
                }
            }
        })
    }


    fun getPasswordLogin(url: String?) {

        val client = OkHttpClient.Builder()
            .connectionSpecs(
                Arrays.asList(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS
                )
            )
            .build()
        //1.拿到okhttp对象
        val okHttpClient = OkHttpClient()

        //2.构造request
        val request = Request.Builder()
            .get()
            .url(url)
            .addHeader("User-Agent", shp.getUserAgent())

            .build()
        //3.将request封装为call
        val call = OkhttpSingleton.ok()?.newCall(request)
        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread { ToastUtils.showTextToast2(this@LoginActivity, "网络请求失败") }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body()!!.string()

                //封装对象
                val passwordDateClass = PasswordDateClass()
                val passwordData = PasswordData()

                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val data = jsonObject.optJSONObject("data")
                    val msg = jsonObject.optString("msg")


                    //第一层封装

                    //第一层封装
                    passwordDateClass.code = code
                    passwordDateClass.msg = msg
                    if (data != null){
                        val token = data.optString("token")
                        val uid = data.optInt("uid")
                        val sessionId = data.optString("sessionId")

                        passwordData.token = token
                        passwordData.uid = uid
                        passwordData.sessionId = sessionId

                        passwordDateClass.data = passwordData


                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                }
                runOnUiThread {

                    if (passwordDateClass.code == 0) {
                        passwordData.token.let { it1 ->
                            shp.saveToSp(
                                "token",
                                it1
                            )
                        }
                        shp.saveToSp("uid", "${passwordData.uid}")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else {
                        val msg: String = passwordDateClass.msg
                        ToastUtils.showTextToast2(this@LoginActivity, msg)
                    }
                }
            }


        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCollector2.removeActivity(this)
        finish()

    }

    fun getVerName(): String? {
        var verName: String? = ""
        try {
            verName = packageManager.getPackageInfo(
                "com.hzdq.bajiesleepchildrenHD", 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return verName
    }
}