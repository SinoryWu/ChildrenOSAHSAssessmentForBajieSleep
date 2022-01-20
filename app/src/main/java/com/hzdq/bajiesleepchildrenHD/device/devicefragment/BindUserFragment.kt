package com.hzdq.bajiesleepchildrenHD.device.devicefragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentBindUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceBindUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DeviceBindUserBody
import com.hzdq.bajiesleepchildrenHD.device.dialog.DaysDialog
import com.hzdq.bajiesleepchildrenHD.device.dialog.TimeRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.M)
class BindUserFragment : Fragment() {


private lateinit var binding:FragmentBindUserBinding
private lateinit var navController: NavController
    private var timeRangePickerDialog :TimeRangePickerDialog ?= null
    private var daysDialog :DaysDialog ?= null
    private lateinit var deviceViewModel: DeviceViewModel
    private var tokenDialog: TokenDialog? = null
    var startAndEndTime = "21:00-06:00"
    private lateinit var shp:Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.fragment_bind_user,null,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shp = Shp(requireContext())
        deviceViewModel = ViewModelProvider(requireActivity()).get(
            DeviceViewModel::class.java
        )
        val deviceBindUserBody = DeviceBindUserBody()
        deviceBindUserBody.type ="1"
        deviceBindUserBody.uid = ""
        deviceBindUserBody.monitor ="21:00 - 06:00"
        deviceBindUserBody.frequency ="1"
        deviceBindUserBody.hospitalid ="${shp.getHospitalId()}"

        val retrofitSingleton = RetrofitSingleton.getInstance(requireContext())

        binding.bindUserBack.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
//            Navigation.findNavController(it).navigateUp()
        }

        deviceViewModel.uid.observe(requireActivity(), Observer {
            deviceBindUserBody.uid = it
        })

//        deviceViewModel.a = 2
//        deviceViewModel.userName.value = "1wdsa"

        deviceViewModel.day.observe(viewLifecycleOwner, Observer {
            binding.bindUserDays.text = "${it}天"
        })

        deviceViewModel.userName.observe(viewLifecycleOwner, Observer {
            binding.bindUserName.text = it
        })

        deviceViewModel.editDeviceSN.observe(requireActivity(), Observer {
            binding.bindUserSN.text = it
        })


        binding.bindUserChangeSN.setOnClickListener {
            navController = Navigation.findNavController(it)

            navController.navigate(R.id.action_bindUserFragment_to_choiceDeviceFragment)


        }

        binding.bindUserChangeName.setOnClickListener {
            navController = Navigation.findNavController(it)

            navController.navigate(R.id.action_bindUserFragment_to_choiceUserFragment)
        }

        binding.bindUserChangeDays.setOnClickListener {
            if (daysDialog == null) {
                daysDialog = DaysDialog(requireContext(), object : DaysDialog.ConfirmAction {
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(day: Int) {
                        deviceViewModel.updateDay(day)
                        deviceBindUserBody.frequency = "$day"
                    }

                })
                daysDialog?.show()
                //在show之后调用否则第一次不生效
                daysDialog?.setCanceledOnTouchOutside(false)
            }else {
                daysDialog?.show()
                //在show之后调用否则第一次不生效
                daysDialog?.setCanceledOnTouchOutside(false)
            }


        }

        binding.bindUserChangeTime.setOnClickListener {

            if (timeRangePickerDialog == null) {
                timeRangePickerDialog = TimeRangePickerDialog(
                    requireContext(),
                    binding.bindUserTime.text.toString(),
                    object : TimeRangePickerDialog.ConfirmAction {
                        override fun onLeftClick() {

                        }

                        override fun onRightClick(
                            startHour: String?,
                            starMinute: String?,
                            endHour: String?,
                            endMinute: String?
                        ) {
                            Log.d("TAG", "$startHour: $starMinute  到 $endHour : $endMinute ")
                            binding.bindUserTime.text = "${startHour}:${starMinute}~${endHour}:${endMinute}"
                            startAndEndTime = "${startHour}:${starMinute}-${endHour}:${endMinute}"
                            deviceBindUserBody.monitor = "${startHour}:${starMinute} - ${endHour}:${endMinute}"
                        }

                    })
                timeRangePickerDialog?.show()
                //在show之后调用否则第一次不生效
                timeRangePickerDialog?.setCanceledOnTouchOutside(false)

            }else {
                timeRangePickerDialog?.show()
                //在show之后调用否则第一次不生效
                timeRangePickerDialog?.setCanceledOnTouchOutside(false)
            }

        }

        binding.bindUserCancel.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }
        binding.bindUserConfirm.setOnClickListener {

            if (deviceBindUserBody.uid.equals("")){
                ToastUtils.showTextToast(requireContext(),"请选择用户")
            }else {
                postRecoveryDevice(retrofitSingleton,deviceBindUserBody)
            }
        }
    }


    /**
     * 绑定用户
     */
    fun postRecoveryDevice(retrofitSingleton: RetrofitSingleton, deviceBindUserBody: DeviceBindUserBody){
        retrofitSingleton.api().postDeviceBindUser("/v2/device/${deviceViewModel.editDeviceSN.value}",deviceBindUserBody).enqueue(object :
            Callback<DataClassDeviceBindUser> {
            override fun onResponse(
                call: Call<DataClassDeviceBindUser>,
                response: Response<DataClassDeviceBindUser>
            ) {
                if (response.body()?.code == 0){
                    ToastUtils.showTextToast(requireContext(),"绑定成功")
                    requireActivity().setResult(RESULT_OK)
                    ActivityCollector2.removeActivity(requireActivity())
                    requireActivity().finish()


                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(requireContext(), object : TokenDialog.ConfirmAction {
                            override fun onRightClick() {
                                shp.saveToSp("token", "")
                                shp.saveToSp("uid", "")

                                startActivity(
                                    Intent(requireContext(),
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
                    ToastUtils.showTextToast(requireContext(),"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassDeviceBindUser>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"回收设备网络请求失败")
            }

        })
    }
}


