package com.hzdq.bajiesleepchildrenHD.device.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentDeviceEditBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceInfo
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassEditDevice
import com.hzdq.bajiesleepchildrenHD.dataclass.EditDeviceBody
import com.hzdq.bajiesleepchildrenHD.device.dialog.*
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DeviceEditFragment : Fragment() {

    private lateinit var binding: FragmentDeviceEditBinding
    private var timeRangePickerDialog : TimeRangePickerDialog? =null
    private var statusDialog : StatusDialog? =null
    private var endDeviceDialog : EndDeviceDialog? =null
    private var recoveryDeviceDialog: RecoveryDeviceDialog?=null
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var retrofit : RetrofitSingleton
    private var tokenDialog: TokenDialog? = null
    var startAndEndTime = "21:00-06:00"
    private lateinit var navController: NavController
    private lateinit var shp: Shp
    private var deleteDeviceDialog: DeleteDeviceDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_device_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        retrofit = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)

        binding.editDeviceSN.text = deviceViewModel.editDeviceSN.value

        val editDeviceBody = EditDeviceBody()
        editDeviceBody.hospitalid = shp.getHospitalId()!!

        deviceViewModel.deviceStatus.observe(viewLifecycleOwner, Observer {
            editDeviceBody.status = it
        })

        deviceViewModel.rangeTime.observe(viewLifecycleOwner, Observer {
            editDeviceBody.monitor = it
        })

        deviceViewModel.editDeviceSN.observe(viewLifecycleOwner, Observer {
            editDeviceBody.sn = it
        })

        binding.editDeviceCancel.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }

        binding.editDeviceBack.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }

        getDeviceInfo(retrofit)
        //???????????????

        binding.editDeviceChangeTime.setOnClickListener {


            val buffer = StringBuffer(startAndEndTime)
            if (startAndEndTime.substring(3,5).equals("15")){
                buffer.replace(3,5,"01")
            }else if (startAndEndTime.substring(3,5).equals("30")){
                buffer.replace(3,5,"02")
            }else if (startAndEndTime.substring(3,5).equals("45")){
                buffer.replace(3,5,"03")
            }

            if (startAndEndTime.substring(9,11).equals("15")){
                buffer.replace(9,11,"01")
            }else if (startAndEndTime.substring(9,11).equals("30")){
                buffer.replace(9,11,"02")
            }else if (startAndEndTime.substring(9,11).equals("45")){
                buffer.replace(9,11,"03")
            }
            startAndEndTime = buffer.toString()

            if(timeRangePickerDialog == null){
                timeRangePickerDialog = TimeRangePickerDialog(requireContext(),startAndEndTime,object :TimeRangePickerDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(
                        startHour: String?,
                        starMinute: String?,
                        endHour: String?,
                        endMinute: String?
                    ) {
                        binding.editDeviceTime.text = "${startHour}:${starMinute}~${endHour}:${endMinute}"
                        startAndEndTime = "${startHour}:${starMinute}-${endHour}:${endMinute}"
                        deviceViewModel.rangeTime.value = "${startHour}:${starMinute}-${endHour}:${endMinute}"

                    }

                })
                timeRangePickerDialog!!.show()
                timeRangePickerDialog?.setCanceledOnTouchOutside(false)
            }else {
                timeRangePickerDialog!!.show()
                timeRangePickerDialog?.setCanceledOnTouchOutside(false)
            }

        }

        //????????????dialog
        binding.editDeviceRecoveryStatus.setOnClickListener {
            if (recoveryDeviceDialog == null){
                recoveryDeviceDialog = RecoveryDeviceDialog(requireContext(),object :RecoveryDeviceDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(view:View) {
                        navController.navigate(R.id.action_deviceEditFragment_to_deviceRecoveryFragment)
                    }

                })
                recoveryDeviceDialog?.show()
                recoveryDeviceDialog?.setCanceledOnTouchOutside(false)

            }else {
                recoveryDeviceDialog?.show()
                recoveryDeviceDialog?.setCanceledOnTouchOutside(false)
            }
        }

        //????????????dialog
        binding.editDeviceStopStatus.setOnClickListener {
            if (endDeviceDialog == null){
                endDeviceDialog = EndDeviceDialog(requireContext(),object :EndDeviceDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick() {
                        shp.getHospitalId()?.let { it1 -> postEndDevice(retrofit, it1) }
                    }

                })
                endDeviceDialog?.show()
                endDeviceDialog?.setCanceledOnTouchOutside(false)
            }else {
                endDeviceDialog?.show()
                endDeviceDialog?.setCanceledOnTouchOutside(false)
            }
        }

        deviceViewModel.deviceStatus.observe(requireActivity(), Observer {

        })

        binding.editDeviceChangeStatus.setOnClickListener {
            if (statusDialog == null){
                statusDialog = StatusDialog(deviceViewModel,requireActivity(),requireContext(),object :StatusDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(status: Int) {
                        deviceViewModel.editDeviceStatus.value = status
                        shp.getHospitalId()?.let { it1 -> putEditDevice(retrofit, it1) }
                    }

                })

                statusDialog!!.show()
                statusDialog?.setCanceledOnTouchOutside(false)

            }else {
                statusDialog!!.show()
                statusDialog?.setCanceledOnTouchOutside(false)
            }
        }

        //????????????
        binding.deleteButton.setOnClickListener {
            if (deleteDeviceDialog == null){
                deleteDeviceDialog = DeleteDeviceDialog(requireContext(),object :DeleteDeviceDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick() {
                        val url =  OkhttpSingleton.BASE_URL+"/v2/device/${deviceViewModel.editDeviceSN.value}"
                        val maps = HashMap<String, String>()
                        maps["hospitalid"] = shp.getHospitalId().toString() //18158188052
                        maps["status"] = "-1" //18158188052
                        maps["sn"] = deviceViewModel.editDeviceSN.value!!
                        maps["scene"] = "1"
                        maps["mode_type"] = "1"
                        putDeleteDevice(url,maps)
                    }

                })
                deleteDeviceDialog?.show()
                //???show????????????????????????????????????
                deleteDeviceDialog?.setCanceledOnTouchOutside(false)
            }else {
                deleteDeviceDialog?.show()
                //???show????????????????????????????????????
                deleteDeviceDialog?.setCanceledOnTouchOutside(false)
            }

        }

        binding.editDeviceConfirm.setOnClickListener {

//            editDevice(retrofit,editDeviceBody)
            val maps = HashMap<String, String>()
            maps["sn"] = deviceViewModel.editDeviceSN.value!!
            maps["status"] = deviceViewModel.deviceStatus.value!!.toString()
//            maps["status"] = "5"
            maps["scene"] = "1"
            maps["mode_type"] = "1"
            maps["monitor"] = deviceViewModel.rangeTime.value!!
            maps["hospitalid"] = shp.getHospitalId().toString()


            val url = OkhttpSingleton.BASE_URL+"/v2/device/${deviceViewModel.editDeviceSN.value!!}"
            putEditDevice2(url,maps)
        }
    }


    /**
     * ??????????????????
     */
    fun getDeviceInfo(retrofitSingleton: RetrofitSingleton){
        val map = mapOf(
            1 to binding.editDeviceChangeStatus,
            2 to binding.editDeviceRecoveryStatus,
            3 to binding.editDeviceStopStatus
        )
        map.forEach {
            it.value.visibility = View.GONE
        }
        retrofitSingleton.api().getDeviceInfo("/v2/deviceInfo/${deviceViewModel.editDeviceSN.value}").enqueue(object :Callback<DataClassDeviceInfo>{
            override fun onResponse(
                call: Call<DataClassDeviceInfo>,
                response: Response<DataClassDeviceInfo>
            ) {

                if (response.body()?.code == 0){


                    deviceViewModel.deviceStatus.value = response.body()?.data?.status
                    when(response.body()?.data?.status){
                        15 -> {
                            binding.editDeviceRecoveryStatus.visibility = View.VISIBLE
                            when(response.body()?.data?.outTime){
                                0 -> binding.editDeviceStatus.text = "?????????"
                                else -> binding.editDeviceStatus.text = "??????${response.body()?.data?.outTime}???"
                            }
                        }
                        20 -> {
                            binding.editDeviceStopStatus.visibility = View.VISIBLE
                            binding.editDeviceStatus.text = "?????????"
                        }
                        1 -> {
                            binding.editDeviceChangeStatus.visibility = View.VISIBLE
                            binding.editDeviceStatus.text = "??????"
                        }
                        5 -> {
                            binding.editDeviceChangeStatus.visibility = View.VISIBLE
                            binding.editDeviceStatus.text = "?????????"
                        }
                        10 -> {
                            binding.editDeviceChangeStatus.visibility = View.VISIBLE
                            binding.editDeviceStatus.text = "?????????"
                        }
                    }

                    val monitor = response.body()?.data?.monitor?.replace(" ","")?.trim()
                    if (monitor != null) {
                        startAndEndTime = monitor
                    }
                    val rangeTime = monitor?.replace("-"," ~ ")
                    binding.editDeviceTime.text = rangeTime
                    deviceViewModel.rangeTime.value = monitor

                    when(response.body()?.data?.modeType){
                        0 -> binding.editDeviceMode.text = "????????????"
                        1 -> binding.editDeviceMode.text = "????????????"
                    }


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

            override fun onFailure(call: Call<DataClassDeviceInfo>, t: Throwable) {

            }

        })
    }

    /**
     * ??????????????????
     */
    fun putEditDevice2(url:String,map: HashMap<String, String>) {
        //1.??????okhttp??????


        //2.??????request
        //2.1??????requestbody
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
            .put(requestBodyJson)
            .build()
        //3.???request?????????call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.??????call
//        ????????????
//        Response response = call.execute();

        //????????????
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "??????????????????") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {


                val res = response.body()!!.string()

                val gson = Gson()
                val dataClassEditDevice = gson.fromJson(res,DataClassEditDevice::class.java)
                requireActivity().runOnUiThread {
                    if (dataClassEditDevice.code == 0){
                        ToastUtils.showTextToast(requireContext(),"????????????")
                        requireActivity().setResult(AppCompatActivity.RESULT_OK)
                        requireActivity().finish()
                    }else if (dataClassEditDevice.code == 10010 || dataClassEditDevice.code == 10004) {
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
                        ToastUtils.showTextToast(requireContext(),"${dataClassEditDevice.msg}")
                    }
                }
            }
        })
    }

    fun putEditDevice(retrofitSingleton: RetrofitSingleton, hospitalid:Int) {
        retrofitSingleton.api().putEditDevice(
            "/v2/device/${deviceViewModel.editDeviceSN.value}",
            deviceViewModel.editDeviceStatus.value!!,
            hospitalid
        )
            .enqueue(object : Callback<DataClassEditDevice> {
                override fun onResponse(
                    call: Call<DataClassEditDevice>,
                    response: Response<DataClassEditDevice>
                ) {
                    if (response.body()?.code == 0){
                        ToastUtils.showTextToast(requireContext(),"????????????")
                        getDeviceInfo(retrofit)

//                        requireActivity().setResult(AppCompatActivity.RESULT_OK)
//                        requireActivity().finish()
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

                override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
                    ToastUtils.showTextToast(requireContext(),"??????????????????????????????")
                }
            })
    }


    /**
     * ????????????
     */
    fun putDeleteDevice(url: String,map: HashMap<String, String>) {



        //1.??????okhttp??????


        //2.??????request
        //2.1??????requestbody
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
            .put(requestBodyJson)
            .build()
        //3.???request?????????call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.??????call
//        ????????????
//        Response response = call.execute();

        //????????????
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "??????????????????????????????") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()
                val gson = Gson()
                val dataClassEditDevice = gson.fromJson(res,DataClassEditDevice::class.java)

                requireActivity().runOnUiThread {
                    if (dataClassEditDevice.code == 0) {
                        ToastUtils.showTextToast(requireContext(),"????????????")


                        requireActivity().setResult(AppCompatActivity.RESULT_OK)
                        requireActivity().finish()


                    }else if ( dataClassEditDevice.code == 10010 ||  dataClassEditDevice.code == 10004) {
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
                    } else {
                        ToastUtils.showTextToast(requireContext(),"${dataClassEditDevice.msg}")
                    }
                }
            }
        })

//        retrofitSingleton.api().putEditDevice(
//            "/v2/device/${deviceViewModel.editDeviceSN.value}",
//            -1,
//            hospitalid
//        )
//            .enqueue(object : Callback<DataClassEditDevice> {
//                override fun onResponse(
//                    call: Call<DataClassEditDevice>,
//                    response: Response<DataClassEditDevice>
//                ) {
//                    if (response.body()?.code == 0){
//                        ToastUtils.showTextToast(requireContext(),"????????????")
//
//
//                        requireActivity().setResult(AppCompatActivity.RESULT_OK)
//                        requireActivity().finish()
//                    }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                        if (tokenDialog == null) {
//                            tokenDialog = TokenDialog(requireContext(), object : TokenDialog.ConfirmAction {
//                                override fun onRightClick() {
//                                    shp.saveToSp("token", "")
//                                    shp.saveToSp("uid", "")
//
//                                    startActivity(
//                                        Intent(requireContext(),
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
//                        ToastUtils.showTextToast(requireContext(),"${response.body()?.msg}")
//                    }
//                }
//
//                override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
//                    ToastUtils.showTextToast(requireContext(),"??????????????????????????????")
//                }
//            })
    }
    /**
     * ????????????
     */
    fun postEndDevice(retrofitSingleton: RetrofitSingleton, hospitalid:Int){

        retrofitSingleton.api().postEndDevice(hospitalid, deviceViewModel.editDeviceSN.value!!)
            .enqueue(object : Callback<DataClassEditDevice> {
                override fun onResponse(
                    call: Call<DataClassEditDevice>,
                    response: Response<DataClassEditDevice>
                ) {

                    if (response.body()?.code == 0){
                        ToastUtils.showTextToast(requireContext(),"????????????")
                        getDeviceInfo(retrofit)
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

                override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
                    ToastUtils.showTextToast(requireContext(),"??????????????????????????????")
                }
            })
    }

    /**
     * ????????????
     */

    fun editDevice(retrofitSingleton: RetrofitSingleton,editDeviceBody: EditDeviceBody){
        retrofitSingleton.api().putDeviceEdit("/v2/device/${deviceViewModel.editDeviceSN.value}",editDeviceBody).enqueue(object :Callback<DataClassEditDevice>{
            override fun onResponse(
                call: Call<DataClassEditDevice>,
                response: Response<DataClassEditDevice>
            ) {
                if (response.body()?.code == 0){
                    ToastUtils.showTextToast(requireContext(),"????????????")
                        requireActivity().setResult(AppCompatActivity.RESULT_OK)
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

            override fun onFailure(call: Call<DataClassEditDevice>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"??????????????????????????????")
            }

        })
    }

}