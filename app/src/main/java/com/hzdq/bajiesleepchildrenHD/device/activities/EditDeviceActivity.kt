package com.hzdq.bajiesleepchildrenHD.device.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityEditDeviceBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceBindUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassEditDevice
import com.hzdq.bajiesleepchildrenHD.dataclass.DeviceBindUserBody
import com.hzdq.bajiesleepchildrenHD.device.dialog.EndDeviceDialog
import com.hzdq.bajiesleepchildrenHD.device.dialog.RecoveryDeviceDialog
import com.hzdq.bajiesleepchildrenHD.device.dialog.StatusDialog
import com.hzdq.bajiesleepchildrenHD.device.dialog.TimeRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url

@RequiresApi(Build.VERSION_CODES.O)
class EditDeviceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditDeviceBinding
    private var timeRangePickerDialog : TimeRangePickerDialog? =null
    private var tokenDialog: TokenDialog? = null
    private var statusDialog : StatusDialog? =null
    private var endDeviceDialog : EndDeviceDialog? =null
    private var recoveryDeviceDialog:RecoveryDeviceDialog?=null
    private lateinit var deviceViewModel: DeviceViewModel

    private lateinit var shp: Shp
    private lateinit var controller:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_device)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        controller  = Navigation.findNavController(this,R.id.edit_device_fragment)
//        binding.editDeviceBack.setOnClickListener {
//            finish()
//        }
//        val retrofitSingleton = RetrofitSingleton.getInstance(this)
//        val shp = Shp(this)
//        val deviceBindUserBody = DeviceBindUserBody()
//        deviceBindUserBody.hospitalid = shp.getHospitalId().toString()
//        deviceBindUserBody.type = "2"
//
//
//
//
        val intent = intent
        val deviceSN = intent.getStringExtra("sn")
        shp = Shp(this)

//
        deviceViewModel.editDeviceSN.value = deviceSN
//
//        binding.editDeviceSN.text = deviceSN
//
//        when(status){
//            1 -> {
//                binding.editDeviceStatus.text = "闲置"
//                binding.editDeviceChangeStatus.visibility = View.VISIBLE
//            }
//            5 -> {
//                binding.editDeviceChangeStatus.visibility = View.VISIBLE
//                binding.editDeviceStatus.text = "维修中"
//            }
//            10 ->{
//                binding.editDeviceStatus.text = "维保中"
//                binding.editDeviceChangeStatus.visibility = View.VISIBLE
//            }
//            15 -> {
//                binding.editDeviceRecoveryStatus.visibility = View.VISIBLE
//                when(outTime){
//                    0 -> binding.editDeviceStatus.text = "待归还"
//                    else -> binding.editDeviceStatus.text = "逾期${outTime}天"
//                }
//            }
//            20 ->{
//                binding.editDeviceStopStatus.visibility = View.VISIBLE
//                binding.editDeviceStatus.text = "已借出"
//            }
//        }
//
//        var startAndEndTime = "21:00-06:00"
//        binding.editDeviceChangeTime.setOnClickListener {
//
//            if(timeRangePickerDialog == null){
//                 timeRangePickerDialog = TimeRangePickerDialog(this,startAndEndTime,object :TimeRangePickerDialog.ConfirmAction{
//                    override fun onLeftClick() {
//
//                    }
//
//                    override fun onRightClick(
//                        startHour: String?,
//                        starMinute: String?,
//                        endHour: String?,
//                        endMinute: String?
//                    ) {
//                        binding.editDeviceTime.text = "${startHour}:${starMinute}~${endHour}:${endMinute}"
//                        startAndEndTime = "${startHour}:${starMinute}-${endHour}:${endMinute}"
//
//                    }
//
//                })
//                timeRangePickerDialog!!.show()
//                timeRangePickerDialog?.setCanceledOnTouchOutside(false)
//            }else {
//                timeRangePickerDialog!!.show()
//                timeRangePickerDialog?.setCanceledOnTouchOutside(false)
//            }
//
//        }
//
//        binding.editDeviceRecoveryStatus.setOnClickListener {
//            if (recoveryDeviceDialog == null){
//                recoveryDeviceDialog = RecoveryDeviceDialog(this,object :RecoveryDeviceDialog.ConfirmAction{
//                    override fun onLeftClick() {
//
//                    }
//
//                    override fun onRightClick() {
//                        binding.editDeviceStatus.text = "设备回收"
//                        binding.editDeviceRecoveryStatus.visibility = View.GONE
//                    }
//
//                })
//                recoveryDeviceDialog?.show()
//                recoveryDeviceDialog?.setCanceledOnTouchOutside(false)
//
//            }
//        }
//
//        binding.editDeviceStopStatus.setOnClickListener {
//            if (endDeviceDialog == null){
//                endDeviceDialog = EndDeviceDialog(this,object :EndDeviceDialog.ConfirmAction{
//                    override fun onLeftClick() {
//
//                    }
//
//                    override fun onRightClick() {
//                        binding.editDeviceStatus.text = "待回收"
//                        binding.editDeviceStopStatus.visibility = View.GONE
//                    }
//
//                })
//                endDeviceDialog?.show()
//                endDeviceDialog?.setCanceledOnTouchOutside(false)
//            }
//        }
//
//        binding.editDeviceChangeStatus.setOnClickListener {
//            if (statusDialog == null){
//                statusDialog = StatusDialog(deviceViewModel,this,this,object :StatusDialog.ConfirmAction{
//                    override fun onLeftClick() {
//
//                    }
//
//                    override fun onRightClick(status: Int) {
//                        deviceViewModel.editDeviceStatus.value = status
//                        when(status){
//                            1 -> binding.editDeviceStatus.text = "闲置"
//                            5 -> binding.editDeviceStatus.text = "维修中"
//                            10 -> binding.editDeviceStatus.text = "维保中"
//                        }
//                    }
//
//                })
//
//                statusDialog!!.show()
//                statusDialog?.setCanceledOnTouchOutside(false)
//
//            }else {
//                statusDialog!!.show()
//                statusDialog?.setCanceledOnTouchOutside(false)
//            }
//        }
//
//        binding.editDeviceConfirm.setOnClickListener {

//            if (status == 1 || status == 5 || status == 10){
//                if (deviceViewModel.editDeviceStatus.value == 0){
//                    ToastUtils.showTextToast(this,"请选择设备状态")
//                }else {
//                    putEditDevice(retrofitSingleton,shp.getHospitalId()!!)
//                }
//            }else if (status == 20){
//                if (binding.editDeviceStatus.text.equals("待回收")){
//                    postEndDevice(retrofitSingleton,shp.getHospitalId()!!)
//                }else {
//                    ToastUtils.showTextToast(this,"请选择是否终止设备")
//                }
//            }else if (status == 15){
//                if (binding.editDeviceStatus.text.equals("设备回收")){
//                    postRecoveryDevice(retrofitSingleton,deviceBindUserBody)
//                }else {
//                    ToastUtils.showTextToast(this,"请选择是否回收设备")
//                }
//            }
//
//        }
    }

    override fun onNavigateUp(): Boolean {
        //如果在第一个fragment页面再按一次返回键就关闭当前activity
        if (controller.currentDestination?.id == R.id.deviceEditFragment){
            ActivityCollector2.removeActivity(this)
            finish()

        }else{
            controller.navigateUp()
        }
        return super.onNavigateUp()

    }

    override fun onBackPressed() {
        onNavigateUp()

    }

    /**
     * 变更设备状态
     */
    fun putEditDevice(retrofitSingleton: RetrofitSingleton,hospitalid:Int) {
        retrofitSingleton.api().putEditDevice(
            "/v1/device/${deviceViewModel.editDeviceSN.value}",
            deviceViewModel.editDeviceStatus.value!!,
            hospitalid
        )
            .enqueue(object : Callback<DataClassEditDevice> {
                override fun onResponse(
                    call: Call<DataClassEditDevice>,
                    response: Response<DataClassEditDevice>
                ) {
                    if (response.body()?.code == 0){
                        ToastUtils.showTextToast(this@EditDeviceActivity,"操作成功")
                        setResult(RESULT_OK)
                        finish()
                    } else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@EditDeviceActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@EditDeviceActivity,
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
                        ToastUtils.showTextToast(this@EditDeviceActivity,"${response.body()?.msg}")
                    }
                }

                override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
                    ToastUtils.showTextToast(this@EditDeviceActivity,"编辑设备网络请求失败")
                }
            })
    }

    /**
     * 终止设备
     */
    fun postEndDevice(retrofitSingleton: RetrofitSingleton,hospitalid:Int){

        retrofitSingleton.api().postEndDevice(hospitalid, deviceViewModel.editDeviceSN.value!!)
            .enqueue(object : Callback<DataClassEditDevice> {
                override fun onResponse(
                    call: Call<DataClassEditDevice>,
                    response: Response<DataClassEditDevice>
                ) {

                    if (response.body()?.code == 0){
                        ToastUtils.showTextToast(this@EditDeviceActivity,"操作成功")
                        setResult(RESULT_OK)
                        finish()
                    }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@EditDeviceActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@EditDeviceActivity,
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
                        ToastUtils.showTextToast(this@EditDeviceActivity,"${response.body()?.msg}")
                    }
                }

                override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
                    ToastUtils.showTextToast(this@EditDeviceActivity,"编辑设备网络请求失败")
                }
            })
    }

    /**
     * 回收设备
     */
    fun postRecoveryDevice(retrofitSingleton: RetrofitSingleton,deviceBindUserBody: DeviceBindUserBody){
        retrofitSingleton.api().postDeviceBindUser("/v1/device/${deviceViewModel.editDeviceSN.value}",deviceBindUserBody).enqueue(object :Callback<DataClassDeviceBindUser>{
            override fun onResponse(
                call: Call<DataClassDeviceBindUser>,
                response: Response<DataClassDeviceBindUser>
            ) {
                if (response.body()?.code == 0){
                    setResult(RESULT_OK)
                    finish()
                    ToastUtils.showTextToast(this@EditDeviceActivity,"操作成功")
                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(this@EditDeviceActivity ,object : TokenDialog.ConfirmAction {
                            override fun onRightClick() {
                                shp.saveToSp("token", "")
                                shp.saveToSp("uid", "")

                                startActivity(
                                    Intent(this@EditDeviceActivity,
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
                    ToastUtils.showTextToast(this@EditDeviceActivity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassDeviceBindUser>, t: Throwable) {
                ToastUtils.showTextToast(this@EditDeviceActivity,"编辑设备网络请求失败")
            }

        })
    }
}