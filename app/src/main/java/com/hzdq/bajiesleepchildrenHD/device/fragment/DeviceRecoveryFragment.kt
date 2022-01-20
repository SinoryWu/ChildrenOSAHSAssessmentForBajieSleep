package com.hzdq.bajiesleepchildrenHD.device.fragment

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothClass
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentDeviceRecoveryBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceBindUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassRecoverDeviceInfo
import com.hzdq.bajiesleepchildrenHD.dataclass.DeviceBindUserBody
import com.hzdq.bajiesleepchildrenHD.device.adapter.DeviceRecoveryListAdapter
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.home.activities.ReportInfoActivity
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeviceRecoveryFragment : Fragment() {

    private lateinit var binding:FragmentDeviceRecoveryBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var deviceRecoveryListAdapter: DeviceRecoveryListAdapter
    private lateinit var shp: Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_device_recovery, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)
        val retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())

        val deviceBindUserBody = DeviceBindUserBody()
        deviceBindUserBody.type = "2"
        deviceBindUserBody.devStatus = ""
        deviceBindUserBody.hospitalid = shp.getHospitalId().toString()



        //列表适配器
        deviceRecoveryListAdapter = DeviceRecoveryListAdapter()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            adapter = deviceRecoveryListAdapter
            layoutManager = linearLayoutManager
        }

        deviceViewModel.deviceReportList.observe(requireActivity(), Observer {
            deviceRecoveryListAdapter.submitList(it)
        })

        deviceRecoveryListAdapter.setOnItemClickListener(object :DeviceRecoveryListAdapter.OnItemClickListener{
            override fun onItemClick(reportUrl: String, quality: Int, createTime: Int) {
                when (quality){
                    1 -> {
                        val reportUrl = reportUrl
                        val fileNames = "${deviceViewModel.deviceName.value}  ${timestamp2Date("$createTime","yyyy-MM-dd HH:mm")}.pdf"
                        val intent = Intent(requireActivity(),ReportInfoActivity::class.java)
                        intent.putExtra("url",reportUrl)
                        intent.putExtra("fileName",fileNames)
                        startActivity(intent)
                    }
                    2 -> {
                        ToastUtils.showTextToast(requireContext(),"缺少血氧，请查看有效报告")
                    }
                    3 -> {ToastUtils.showTextToast(requireContext(),"无效报告，请查看有效报告")}
                }

            }


        })

        binding.back.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.editDeviceCancel.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }

        binding.sn.text  = deviceViewModel.editDeviceSN.value

        val map = mapOf(
            1 to binding.deviceDamage,
            2 to binding.deviceIntact
        )

        deviceViewModel.recoverDeviceStatus.observe(requireActivity(), Observer {

            map.forEach {
                it.value.isSelected = false
            }
            when(it){
                1 -> binding.deviceDamage.isSelected = true
                2 -> binding.deviceIntact.isSelected = true
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                    }
                }
            }
        })

        binding.deviceDamage.setOnClickListener {
            deviceViewModel.recoverDeviceStatus.value = 1
            deviceBindUserBody.devStatus = "2"}
        binding.deviceIntact.setOnClickListener {
            deviceViewModel.recoverDeviceStatus.value = 2
            deviceBindUserBody.devStatus = "1"}

        getRecoverDeiceInfo(retrofitSingleton)


        binding.editDeviceConfirm.setOnClickListener {
            if (deviceBindUserBody.devStatus.equals("")){
                ToastUtils.showTextToast(requireContext(),"请选择设备是否完好")
            }else {
                postRecoveryDevice(retrofitSingleton,deviceBindUserBody)
            }
        }

    }



    fun getRecoverDeiceInfo(retrofitSingleton: RetrofitSingleton){
        retrofitSingleton.api().getRecoverDeviceInfo("/v2/device/${deviceViewModel.editDeviceSN.value}").enqueue(object :Callback<DataClassRecoverDeviceInfo>{
            override fun onResponse(
                call: Call<DataClassRecoverDeviceInfo>,
                response: Response<DataClassRecoverDeviceInfo>
            ) {

                if (response.body()?.code == 0){
                    binding.name.text = response.body()?.data?.truename
                    binding.phoneNumber.text = response.body()?.data?.mobile
                    binding.reportNum.text = "${response.body()?.data?.reportNum}份"
                    deviceViewModel.deviceReportList.value = response.body()?.data?.report
                    deviceViewModel.deviceName.value = response.body()?.data?.truename
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

            override fun onFailure(call: Call<DataClassRecoverDeviceInfo>, t: Throwable) {

            }

        })
    }


    /**
     * 回收设备
     */
    fun postRecoveryDevice(retrofitSingleton: RetrofitSingleton,deviceBindUserBody: DeviceBindUserBody){
        retrofitSingleton.api().postDeviceBindUser("/v2/device/${deviceViewModel.editDeviceSN.value}",deviceBindUserBody).enqueue(object :Callback<DataClassDeviceBindUser>{
            override fun onResponse(
                call: Call<DataClassDeviceBindUser>,
                response: Response<DataClassDeviceBindUser>
            ) {
                if (response.body()?.code == 0){
                    ToastUtils.showTextToast(requireContext(),"操作成功")
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