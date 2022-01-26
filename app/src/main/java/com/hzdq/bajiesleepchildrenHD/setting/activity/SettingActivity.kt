package com.hzdq.bajiesleepchildrenHD.setting.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivitySettingBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.frontpagefragment.*
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.setting.dialog.LogoutDialog
import com.hzdq.bajiesleepchildrenHD.setting.fragment.*
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var shp: Shp
    private lateinit var retrofitSingleton: RetrofitSingleton
    private var logoutDialog: LogoutDialog? = null

    private val fragmentList: MutableList<Fragment> = ArrayList()
    var settingBaseFragment2: Fragment = SettingBaseFragment()
    var settingAccountFragment: Fragment = SettingAccountFragment()
    var settingPasswordFragment: Fragment = SettingPasswordFragment()
    var settingRegisterFragment: Fragment = SettingRegisterFragment()
    var settingDataFragment: Fragment = SettingDataFragment()
    var destinationMap: Map<Fragment, MotionLayout>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shp = Shp(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        HideUI(this).hideSystemUI()
        retrofitSingleton = RetrofitSingleton.getInstance(this)
        ActivityCollector2.addActivity(this)
        //创建一个map映射,一个key 对应一个value
//        val destinationMap = mapOf(
//            R.id.settingBaseFragment2 to binding.base.settingBaseMotion,
//            R.id.settingAccountFragment to binding.account.settingAccountMotion,
//            R.id.settingPasswordFragment to binding.password.settingPasswordMotion,
//            R.id.settingRegisterFragment to binding.register.settingRegisterMotion,
//            R.id.settingDataFragment to binding.data.settingDataMotion,
//
//        )
        if (savedInstanceState != null) {
            //不为空说明缓存视图中有fragment实例，通过tag取出来
            /*获取保存的fragment  没有的话返回null*/
            settingBaseFragment2 =
                supportFragmentManager.getFragment(savedInstanceState, "BaseFragment")!!
            settingAccountFragment =
                supportFragmentManager.getFragment(savedInstanceState, "AccountFragment")!!
            settingPasswordFragment =
                supportFragmentManager.getFragment(savedInstanceState, "PasswordFragment")!!
            settingRegisterFragment =
                supportFragmentManager.getFragment(savedInstanceState, "RegisterFragment")!!
            settingDataFragment =
                supportFragmentManager.getFragment(savedInstanceState, "DataFragment")!!
            addToList(settingBaseFragment2)
            addToList(settingAccountFragment)
            addToList(settingPasswordFragment)
            addToList(settingRegisterFragment)
            addToList(settingDataFragment)
        } else {
            initFragment()
        }
        destinationMap = mapOf(
            settingBaseFragment2 to binding.base.settingBaseMotion,
            settingAccountFragment to binding.account.settingAccountMotion,
            settingPasswordFragment to binding.password.settingPasswordMotion,
            settingRegisterFragment to binding.register.settingRegisterMotion,
            settingDataFragment to binding.data.settingDataMotion,

            )
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)


        //导航点击事件 每次循环一遍
//        destinationMap.forEach { map->
//            map.value.setOnClickListener {
//                navController.navigate(map.key)
//            }
//        }

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            navController.popBackStack() //每次导航就清空返回栈 这样点击返回按钮才不会回退到上一个导航
//            destinationMap.values.forEach {
//                it.progress = 0f
//            }// 循环设置 layout 状态为初始状态
//            //这里的destination.id  是导航到的fragment的id  而fragment id值在destinationMap作为key对应的值就是底部导航栏的motionlayout 让这些transitionToEnd就可以完成动画
//            destinationMap[destination.id]?.transitionToEnd() //把动画执行完成
//        }

        binding.base.settingBaseMotion.setOnClickListener {
            baseIconCompleteClick()
            if (settingBaseFragment2 == null) {

                settingBaseFragment2 = SettingBaseFragment()
                addFragment(settingBaseFragment2)

            }
            showFragment(settingBaseFragment2)
        }

        binding.account.settingAccountMotion.setOnClickListener {
            accountIconCompleteClick()

            if (settingAccountFragment == null) {

                settingAccountFragment = SettingAccountFragment()
                addFragment(settingAccountFragment)

            }

            showFragment(settingAccountFragment)
        }

        binding.password.settingPasswordMotion.setOnClickListener {
            passwordIconCompleteClick()
            if (settingPasswordFragment == null) {

                settingPasswordFragment = SettingPasswordFragment()
                addFragment(settingPasswordFragment)

            }
            showFragment(settingPasswordFragment)


        }

        binding.register.settingRegisterMotion.setOnClickListener {
            registerIconCompleteClick()


            if (settingRegisterFragment == null) {
                settingRegisterFragment = SettingRegisterFragment()
                addFragment(settingRegisterFragment)
            }

            showFragment(settingRegisterFragment)


        }

        binding.data.settingDataMotion.setOnClickListener {
            dataIconCompleteClick()

            if (settingDataFragment == null) {
                settingDataFragment = SettingDataFragment()
                addFragment(settingDataFragment)
            }

            showFragment(settingDataFragment)


        }


        binding.back.setOnClickListener {
            finish()
        }

        settingViewModel.refreshBase.value = 1
        settingViewModel.refreshAccount.value = 1

        settingViewModel.refreshBase.observe(this, Observer {
            if (it == 1) {
                val map = HashMap<String, String>()
                map["hospitalid"] = shp.getHospitalId().toString() //18158188052
                map["category"] = "app_report"
                val url = OkhttpSingleton.BASE_URL + "/v2/config/info"
                postSettingBaseInfo(url, map)
                settingViewModel.refreshBase.value = 0
            }

        })

        settingViewModel.refreshAccount.observe(this, Observer {
            if (it == 1) {
                val url = OkhttpSingleton.BASE_URL+"/v2/auserList"
                val map = HashMap<String, String>()
                map["hospitalid"] = shp.getHospitalId().toString()

                postAuserList(url, map)
                settingViewModel.refreshAccount.value = 0
            }
        })


        //注册管理
//        val content = "http://test.bajiesleep.com/children/h5/?taskID=24"
        settingViewModel.bitmap = null
//        settingViewModel.register.value = 1
//        settingViewModel.question.value = 1


        //退出登陆
        binding.logOut.setOnClickListener {
            if (logoutDialog == null) {
                logoutDialog = LogoutDialog(this, object : LogoutDialog.ConfirmAction {
                    override fun onLeftClick() {

                    }

                    override fun onRightClick() {
                        postLogOut(retrofitSingleton)
                    }

                })
                logoutDialog!!.show()
                logoutDialog?.setCanceledOnTouchOutside(false)
            } else {
                logoutDialog!!.show()
                logoutDialog?.setCanceledOnTouchOutside(false)
            }

//            if (tokenDialog == null){
//                tokenDialog = TokenDialog(this,object :TokenDialog.ConfirmAction{
//                    override fun onRightClick() {
//                        shp.saveToSp("token","")
//                        shp.saveToSp("uid","")
//
//                    }
//
//                })
//                tokenDialog!!.show()
//                tokenDialog?.setCanceledOnTouchOutside(false)
//            }else {
//                tokenDialog!!.show()
//                tokenDialog?.setCanceledOnTouchOutside(false)
//            }
        }
    }

    /**
     * 退出登录
     */
    fun postLogOut(retrofit: RetrofitSingleton) {
        retrofit.api().postLogOut().enqueue(object : Callback<DataClassLogout> {
            override fun onResponse(
                call: Call<DataClassLogout>,
                response: Response<DataClassLogout>
            ) {
                if (response.body()?.code == 0) {
                    shp.saveToSp("token", "")
                    shp.saveToSp("uid", "")
                    Toast.makeText(this@SettingActivity, "退出成功", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java))


                } else if (response.body()?.code === 10010 || response.body()?.code === 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(this@SettingActivity, object : TokenDialog.ConfirmAction {
                            override fun onRightClick() {
                                shp.saveToSp("token", "")
                                shp.saveToSp("uid", "")

                                startActivity(Intent(this@SettingActivity,LoginActivity::class.java))
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
                    ToastUtils.showTextToast(this@SettingActivity, response.body()?.msg)
                }
            }

            override fun onFailure(call: Call<DataClassLogout>, t: Throwable) {
                ToastUtils.showTextToast(this@SettingActivity, "退出登录网络请求失败")
            }

        })
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    /**
     * 获取账号管理列表
     */

    fun postAuserList(url:String,map:HashMap<String,String>) {
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

                runOnUiThread { ToastUtils.showTextToast2(this@SettingActivity, "管理员列表网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()
                val gson = Gson()
                val dataclassAdministrators: DataClassAdministrators = gson.fromJson(res, DataClassAdministrators::class.java)

                runOnUiThread {
                    if (dataclassAdministrators.code == 0) {
                        val list = dataclassAdministrators.data.data
                        settingViewModel.list.value = list

                    }else if ( dataclassAdministrators.code == 10010 ||  dataclassAdministrators.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@SettingActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@SettingActivity,
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
                            this@SettingActivity,
                            "${dataclassAdministrators.msg}"
                        )
                    }
                }
            }
        })
    }

//    fun getAccountList(
//        retrofit: RetrofitSingleton,
//        hospitalid: Int,
//        kewords: String,
//        startTime: String,
//        endTime: String,
//        page: Int
//    ) {
//
//        retrofit.api().getHomeSleepReport(10, hospitalid, kewords, startTime, endTime, page)
//            .enqueue(object :
//                Callback<DataClassHomeReportSleep> {
//                override fun onResponse(
//                    call: Call<DataClassHomeReportSleep>,
//                    response: Response<DataClassHomeReportSleep>
//                ) {
//                    if (response.body()?.code == 0) {
//
//                        if (response.body() != null) {
//                            val list = response.body()?.data?.data
//
//
//                            settingViewModel.list.value = list
//
//                        }
//
//
//                    }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                        if (tokenDialog == null) {
//                            tokenDialog = TokenDialog(this@SettingActivity, object : TokenDialog.ConfirmAction {
//                                override fun onRightClick() {
//                                    shp.saveToSp("token", "")
//                                    shp.saveToSp("uid", "")
//
//                                    startActivity(
//                                        Intent(this@SettingActivity,
//                                            LoginActivity::class.java)
//                                    )
//                                    ActivityCollector2.finishAll()
//                                }
//
//                            })
//                            tokenDialog!!.show()
//                            tokenDialog?.setCanceledOnTouchOutside(false)
//                        } else {
//                            tokenDialog!!.show()
//                            tokenDialog?.setCanceledOnTouchOutside(false)
//                        }
//                    }else {
//                        ToastUtils.showTextToast(this@SettingActivity,"${response.body()?.msg}")
//                    }
//                }
//
//                override fun onFailure(call: Call<DataClassHomeReportSleep>, t: Throwable) {
//                    ToastUtils.showTextToast(this@SettingActivity, "网络请求失败")
//
//                }
//
//            })
//    }


    fun getMap(): HashMap<String, String> {
        val maps = HashMap<String, String>()
        maps["hospitalid"] = shp.getHospitalId().toString() //18158188052
        maps["category"] = "app_report"
        return maps
    }

    fun postSettingBaseInfo(url: String, map: HashMap<String, String>) {
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

                runOnUiThread { ToastUtils.showTextToast2(this@SettingActivity, "网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val res = response.body()!!.string()
                //封装java对象
                val dataClassSettingBaseInfo = DataClassSettingBaseInfo()
                val dataSettingBaseInfo = DataSettingBaseInfo()
                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("msg")
                    val data = jsonObject.optJSONObject("data")

                    //第一层封装
                    dataClassSettingBaseInfo.code = code
                    dataClassSettingBaseInfo.msg = msg
                    if (data != null) {
                        val hospital_name = data.optString("hospital_name")
                        val hospital_logo = data.optString("hospital_logo")
                        val report_name = data.optString("report_name")
                        val report_logo = data.optString("report_logo")
                        val report_standard = data.optString("report_standard")
                        val report_evaluate = data.optString("report_evaluate")
                        dataSettingBaseInfo.hospitalName = hospital_name
                        dataSettingBaseInfo.hospitalLogo = hospital_logo
                        dataSettingBaseInfo.reportName = report_name
                        dataSettingBaseInfo.reportLogo = report_logo
                        dataSettingBaseInfo.reportStandard = report_standard
                        dataSettingBaseInfo.reportEvaluate = report_evaluate
                        dataClassSettingBaseInfo.data = dataSettingBaseInfo
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                runOnUiThread {
                    if (dataClassSettingBaseInfo.code == 0) {
                        settingViewModel.hospitalIcon.value = dataSettingBaseInfo.hospitalLogo
                        settingViewModel.hospitalName.value = dataSettingBaseInfo.hospitalName
                        settingViewModel.reportName.value = dataSettingBaseInfo.reportName
                        settingViewModel.reportIcon.value = dataSettingBaseInfo.reportLogo
                        settingViewModel.standard.value = dataSettingBaseInfo.reportStandard
                        settingViewModel.evaluate.value = dataSettingBaseInfo.reportEvaluate
                        settingViewModel.welcome.value = shp.getWelcome()

                        if (dataSettingBaseInfo.reportStandard.equals("1")) {

                            settingViewModel.standardContent.value = "正常：OAHI < 5\n" +
                                    "轻度：5 ≤ OAHI < 10\n" +
                                    "中度：10 ≤ OAHI < 20\n" +
                                    "重度：20 ≤ OAHI"
                        } else if (dataSettingBaseInfo.reportStandard.equals("2")) {

                            settingViewModel.standardContent.value = "正常：OAHI < 1\n" +
                                    "轻度：1 ≤ OAHI < 5\n" +
                                    "中度：5 ≤ OAHI < 10\n" +
                                    "重度：10 ≤ OAHI"
                        }

                        if (dataSettingBaseInfo.reportEvaluate.equals("1")) {
                            settingViewModel.evaluateContent.value = "睡眠分期分析：已开启\n" +
                                    "睡眠呼吸综述：已开启\n" +
                                    "建议：已开启"
                        } else {

                            settingViewModel.evaluateContent.value = ""
                            settingViewModel.evaluateContent.value = ""
                        }

                    }else if (dataClassSettingBaseInfo.code == 10010 ||
                        dataClassSettingBaseInfo.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@SettingActivity,
                                object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@SettingActivity,
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
                            this@SettingActivity,
                            "${dataClassSettingBaseInfo.msg}"
                        )
                    }
                }
            }
        })
    }


    /**
     * 数据导出图标动画完成
     */
    private fun dataIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// 循环设置 layout 状态为初始状态
        binding.data.settingDataMotion.transitionToEnd()


    }

    /**
     * 注册管理图标动画完成
     */
    private fun registerIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// 循环设置 layout 状态为初始状态
        binding.register.settingRegisterMotion.transitionToEnd()

    }

    /**
     * 密码管理图标动画完成
     */
    private fun passwordIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// 循环设置 layout 状态为初始状态
        binding.password.settingPasswordMotion.transitionToEnd()
    }

    /**
     * 账号管理图标动画完成
     */
    private fun accountIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// 循环设置 layout 状态为初始状态
        binding.account.settingAccountMotion.transitionToEnd()
    }

    /**
     * 基本信息图标动画完成
     */
    private fun baseIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// 循环设置 layout 状态为初始状态
        binding.base.settingBaseMotion.transitionToEnd()

    }

    private fun initFragment() {
        /* 默认显示home  fragment*/
        baseIconCompleteClick()
        settingBaseFragment2 = SettingBaseFragment()
        addFragment(settingBaseFragment2 as SettingBaseFragment)
        showFragment(settingBaseFragment2 as SettingBaseFragment)
        addFragment(settingAccountFragment as SettingAccountFragment)
        addFragment(settingPasswordFragment as SettingPasswordFragment)
        addFragment(settingRegisterFragment as SettingRegisterFragment)
        addFragment(settingDataFragment as SettingDataFragment)
    }

    /**
     * 添加fragment对象到fragment数组
     */
    private fun addToList(fragment: Fragment?) {
        if (fragment != null) {
            fragmentList.add(fragment)
        }
    }

    /**
     * 显示fragment
     */
    private fun showFragment(fragment: Fragment) {
        for (frag in fragmentList) {
            if (frag !== fragment) {
                /*先隐藏其他fragment*/
                supportFragmentManager.beginTransaction().hide(frag).commit()
            }
        }
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }

    /**
     * 添加fragment
     */
    private fun addFragment(fragment: Fragment) {
        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.content_layout2, fragment).commit()
            /*添加到 fragmentList*/fragmentList.add(fragment)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        /*fragment不为空时 保存*/
        if (settingBaseFragment2 != null) {
            supportFragmentManager.putFragment(outState!!, "BaseFragment", settingBaseFragment2)
        }
        if (settingAccountFragment != null) {
            supportFragmentManager.putFragment(
                outState!!,
                "AccountFragment",
                settingAccountFragment
            )
        }
        if (settingPasswordFragment != null) {
            supportFragmentManager.putFragment(
                outState!!,
                "PasswordFragment",
                settingPasswordFragment
            )
        }
        if (settingRegisterFragment != null) {
            supportFragmentManager.putFragment(
                outState!!,
                "RegisterFragment",
                settingRegisterFragment
            )
        }
        if (settingDataFragment != null) {
            supportFragmentManager.putFragment(outState!!, "DataFragment", settingDataFragment)
        }
        super.onSaveInstanceState(outState)
    }

}