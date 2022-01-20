package com.hzdq.bajiesleepchildrenHD.home.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentHomeBindDeviceBindBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceBindUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DeviceBindUserBody
import com.hzdq.bajiesleepchildrenHD.device.dialog.DaysDialog
import com.hzdq.bajiesleepchildrenHD.device.dialog.TimeRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Time

@RequiresApi(Build.VERSION_CODES.M)
class HomeBindDeviceBindFragment : Fragment() {

    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:FragmentHomeBindDeviceBindBinding
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private var timeRangePickerDialog : TimeRangePickerDialog?= null
    private var daysDialog : DaysDialog?= null
    private lateinit var shp: Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_bind_device_bind, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shp = Shp(requireContext())

        homeBindDeviceViewModel = ViewModelProvider(requireActivity()).get(HomeBindDeviceViewModel::class.java)

        val shp = Shp(requireContext())
        val retrofitSingleton =RetrofitSingleton.getInstance(requireContext())
        val deviceBindUserBody = DeviceBindUserBody()
        deviceBindUserBody.type ="1"
        deviceBindUserBody.devStatus ="1"
        deviceBindUserBody.uid = homeBindDeviceViewModel.uid.value.toString()
        deviceBindUserBody.monitor = "21:00 - 06:00"
        deviceBindUserBody.hospitalid = shp.getHospitalId().toString()

        homeBindDeviceViewModel.frequency.observe(viewLifecycleOwner, Observer {
            deviceBindUserBody.frequency = it
            binding.bindUserDays.text = "${it}天"

        })

        homeBindDeviceViewModel.rangeTime.observe(viewLifecycleOwner, Observer {
            deviceBindUserBody.monitor = it
        })

        binding.back.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.cancel.setOnClickListener {
            requireActivity().finish()
        }

        binding.bindUserSN.text = homeBindDeviceViewModel.sn.value

        binding.bindUserName.text = homeBindDeviceViewModel.name.value

        binding.bindUserChangeDays.setOnClickListener {
            if (daysDialog == null) {
                daysDialog = DaysDialog(requireContext(), object : DaysDialog.ConfirmAction {
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(day: Int) {

                        homeBindDeviceViewModel.frequency.value = "$day"

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
                timeRangePickerDialog = TimeRangePickerDialog(requireContext(),binding.bindUserTime.text.toString(),object :TimeRangePickerDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(
                        startHour: String?,
                        starMinute: String?,
                        endHour: String?,
                        endMinute: String?
                    ) {
                        homeBindDeviceViewModel.startTime.value = "${startHour}:${starMinute}"
                        homeBindDeviceViewModel.endTime.value = "${endHour}:${endMinute}"
                        homeBindDeviceViewModel.setRangeTime()
                        binding.bindUserTime.text = "${startHour}:${starMinute}~${endHour}:${endMinute}"
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

        binding.confirm.setOnClickListener {


            postBindUser(retrofitSingleton,deviceBindUserBody)
        }


    }

    fun postBindUser(retrofitSingleton: RetrofitSingleton,deviceBindUserBody: DeviceBindUserBody){
        retrofitSingleton.api().postDeviceBindUser("/v2/device/${homeBindDeviceViewModel.sn.value}",deviceBindUserBody).enqueue(object :Callback<DataClassDeviceBindUser>{
            override fun onResponse(
                call: Call<DataClassDeviceBindUser>,
                response: Response<DataClassDeviceBindUser>
            ) {

                if (response.body()?.code == 0){
                    ToastUtils.showTextToast(requireContext(),"绑定成功")
                    requireActivity().setResult(RESULT_OK)
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
                ToastUtils.showTextToast(requireContext(),"网络请求失败")
            }

        })
    }

}