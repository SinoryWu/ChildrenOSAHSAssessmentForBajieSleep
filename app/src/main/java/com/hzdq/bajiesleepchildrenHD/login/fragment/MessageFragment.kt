package com.hzdq.bajiesleepchildrenHD.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentMessageBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.SendMessageBody
import com.hzdq.bajiesleepchildrenHD.dataclass.SendMessageDataClass
import com.hzdq.bajiesleepchildrenHD.login.viewmodel.LoginViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.PhoneFormatCheckUtils
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageFragment : Fragment() {

    private lateinit var binding:FragmentMessageBinding
    private lateinit var loginViewModel: LoginViewModel
    private  var phoneNumber = ""
    private  var messagecode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_message, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(requireActivity()).get(
            LoginViewModel::class.java
        )
        val retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        binding.loginPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.phoneNumber = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })



        binding.loginMessageButton.setOnClickListener {

            if (loginViewModel.phoneNumber.isNotEmpty() && PhoneFormatCheckUtils.isPhoneLegal(loginViewModel.phoneNumber) == true) {

                val sendMessageBody = SendMessageBody()
                sendMessageBody.mobile = loginViewModel.phoneNumber
                retrofitSingleton.loginApi().postSendMessage(sendMessageBody).enqueue(object :Callback<SendMessageDataClass>{
                    override fun onResponse(
                        call: Call<SendMessageDataClass>,
                        response: Response<SendMessageDataClass>
                    ) {

                        if (response.body()?.code == 0){
                            ToastUtils.showTextToast(requireContext(),"发送成功")
                        }else {
                            ToastUtils.showTextToast(requireContext(),"${response.body()?.msg}")
                        }
                    }

                    override fun onFailure(call: Call<SendMessageDataClass>, t: Throwable) {
                        ToastUtils.showTextToast(requireContext(),"网络请求失败")
                    }

                })
                loginViewModel.countTIme.start()

            }else if (loginViewModel.phoneNumber.isEmpty()){
                Toast.makeText(requireActivity(),"手机号不能为空", Toast.LENGTH_SHORT).show()
            } else if (PhoneFormatCheckUtils.isPhoneLegal(loginViewModel.phoneNumber) == false){
                Toast.makeText(requireActivity(),"请输入正确手机号", Toast.LENGTH_SHORT).show()
            }


        }

        binding.loginMessage.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.messageCode = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.loginUsePassword.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_messageFragment_to_passWordFragment)

        }


        binding.loginMessage.setText(loginViewModel.messageCode)
        binding.loginPhone.setText(loginViewModel.phoneNumber)


        loginViewModel.timeCount.observe(viewLifecycleOwner, Observer {

            if (it > 0 && it < 60){
                binding.loginMessageButton.text = "${it}秒"
                binding.loginMessageButton.isClickable = false
                binding.loginMessageButton.setBackgroundResource(R.drawable.login_message_button_background_false)
            }else if (it == 60){
                binding.loginMessageButton.text = "59秒"
                binding.loginMessageButton.isClickable = false
                binding.loginMessageButton.setBackgroundResource(R.drawable.login_message_button_background_false)
            } else if ( it == 0) {
                binding.loginMessageButton.isClickable = true
                binding.loginMessageButton.setBackgroundResource(R.drawable.login_message_button_background_true)
                binding.loginMessageButton.text = "重新获取验证码"
            }else if ( it == -1) {
                binding.loginMessageButton.isClickable = true
                binding.loginMessageButton.setBackgroundResource(R.drawable.login_message_button_background_true)
                binding.loginMessageButton.text = "获取验证码"
            }

        })


    }


}