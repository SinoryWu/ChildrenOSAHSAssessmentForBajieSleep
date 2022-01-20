package com.hzdq.bajiesleepchildrenHD.setting.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.DataClassChangePassword
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentSettingPasswordBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateDetailActivity
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2.finishAll
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
import java.util.HashMap


class SettingPasswordFragment : Fragment() {

    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:FragmentSettingPasswordBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var shp: Shp
    private lateinit var retrofitSingleton: RetrofitSingleton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())


        settingViewModel.timeCount.observe(viewLifecycleOwner, Observer {

            if (it > 0 && it < 60){
                binding.messageButton.text = "${it}秒"
                binding.messageButton.isClickable = false
                binding.messageButton.setBackgroundResource(R.drawable.login_message_button_background_false)
            }else if (it == 60){
                binding.messageButton.text = "59秒"
                binding.messageButton.isClickable = false
                binding.messageButton.setBackgroundResource(R.drawable.login_message_button_background_false)
            } else if ( it == 0) {
                binding.messageButton.isClickable = true
                binding.messageButton.setBackgroundResource(R.drawable.login_message_button_background_true)
                binding.messageButton.text = "重新获取验证码"
            }else if ( it == -1) {
                binding.messageButton.isClickable = true
                binding.messageButton.setBackgroundResource(R.drawable.login_message_button_background_true)
                binding.messageButton.text = "获取验证码"
            }

        })

        binding.settingMessage.setText(settingViewModel.messageCode.value)
        binding.newPassword.setText(settingViewModel.newPassword.value)
        binding.repeatNewPassword.setText(settingViewModel.repeatNewPassword.value)

        binding.messageButton.setOnClickListener {
            sendMessage(retrofitSingleton)
        }

        binding.newPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                settingViewModel.newPassword.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.repeatNewPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                settingViewModel.repeatNewPassword.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.settingMessage.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                settingViewModel.messageCode.value = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })




        binding.cancel.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
           requireActivity().finish()

        }

        binding.confirm.setOnClickListener {
            if (settingViewModel.messageCode.value.equals("")){
                Toast.makeText(requireContext(),"请输入验证码",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!settingViewModel.newPassword.value.equals(settingViewModel.repeatNewPassword.value)){
                Toast.makeText(requireContext(),"新密码和重复新密码不一致",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val maps = HashMap<String, String>()
//            maps["mobile"] = shp.getUserMobile()!! //18158188052
            maps["mobile"] = shp.getUserMobile()!! //18158188052
            maps["code"] = settingViewModel.messageCode.value!!//18158188052
            val url = OkhttpSingleton.BASE_URL+"/v1/Jms/check"
            postCheckMessage(url,maps)

        }
    }


    /**
     * 修改密码
     */

    fun postChangePassword(url:String,map: HashMap<String, String>) {
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
            .url(url)
            .addHeader("User-Agent", shp.getUserAgent())
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
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

                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()
                val gson = Gson()
                val dataClassChangePassword = gson.fromJson(res,DataClassChangePassword::class.java)

                requireActivity().runOnUiThread {
                    if (dataClassChangePassword.code == 0) {
                        Toast.makeText(requireContext(),"密码修改成功",Toast.LENGTH_SHORT).show()
                        binding.settingMessage.setText("")
                        binding.newPassword.setText("")
                        binding.repeatNewPassword.setText("")
                        settingViewModel.messageCode.value = ""
                        settingViewModel.newPassword.value = ""
                        settingViewModel.repeatNewPassword.value = ""
                        settingViewModel.countTIme.cancel()
                        settingViewModel.setTimeCount(-1)



                    }  else {
                        Toast.makeText(requireContext(),dataClassChangePassword.msg,Toast.LENGTH_SHORT).show()

                    }
                }
            }
        })
    }

    /**
     * 校验验证码
     */

    fun postCheckMessage(url:String,map: HashMap<String, String>) {
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

                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()
                val gson = Gson()
                val dataClassCheckMessage = gson.fromJson(res,DataClassCheckMessage::class.java)

                requireActivity().runOnUiThread {
                    if (dataClassCheckMessage.code == 0) {


                        val maps = HashMap<String, String>()
                        maps["password"] = settingViewModel.newPassword.value!! //18158188052
                        maps["confirmPassword"] = settingViewModel.repeatNewPassword.value!!//18158188052
                        val url = OkhttpSingleton.BASE_URL+"/v2/auser/changePassword"
                        postChangePassword(url,maps)
                    }  else {
                        Toast.makeText(requireContext(),dataClassCheckMessage.msg,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    /**
     * 发送短信
     */
    private fun sendMessage(retrofitSingleton: RetrofitSingleton){
        val sendMessageBody = SendMessageBody()
//        sendMessageBody.mobile = shp.getUserMobile()!!
        sendMessageBody.mobile = shp.getUserMobile()!!
        retrofitSingleton.loginApi().postSendMessage(sendMessageBody).enqueue(object :
            Callback<SendMessageDataClass> {
            override fun onResponse(
                call: Call<SendMessageDataClass>,
                response: Response<SendMessageDataClass>
            ) {
                if (response.body()?.code == 0){
                    Toast.makeText(requireContext(),"发送成功",Toast.LENGTH_SHORT).show()
                    settingViewModel.countTIme.start()
                } else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(
                            requireContext(),
                            object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")
                                    startActivity(
                                        Intent(
                                            requireContext(),
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
                    Toast.makeText(requireContext(),"${response.body()?.msg}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SendMessageDataClass>, t: Throwable) {
                Toast.makeText(requireContext(),"发送短信网络请求失败",Toast.LENGTH_SHORT).show()
            }

        })

    }
}