package com.hzdq.bajiesleepchildrenHD.frontpagefragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentDeviceBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassDeviceInfo
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontDevice
import com.hzdq.bajiesleepchildrenHD.device.activities.BindUserActivity
import com.hzdq.bajiesleepchildrenHD.device.activities.EditDeviceActivity
import com.hzdq.bajiesleepchildrenHD.device.adapter.FrontDeviceAdapter
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.SPACE_TIME
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.hash
import com.hzdq.bajiesleepchildrenHD.utils.ViewClickDelay.lastClickTime
import kotlinx.android.synthetic.main.fragment_device.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * 设备fragment
 */
class DeviceFragment : Fragment() {

    private lateinit var binding: FragmentDeviceBinding
    private lateinit var navController: NavController
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var retrofit:RetrofitSingleton
    private lateinit var shp:Shp
    private lateinit var frontDeviceAdapter:FrontDeviceAdapter
    private var tokenDialog :TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofit = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)
        shp.saveToSp("frontdevicekeyword", "")
//       binding.radiogroup.clearCheck()
         frontDeviceAdapter = FrontDeviceAdapter(deviceViewModel, requireActivity())

        deviceViewModel.selectState.observe(viewLifecycleOwner, Observer {

            if (it == 0) {
                binding.selectAll.isChecked = true
                binding.selectOnline.isChecked = false
                shp.saveToSpInt("frontdevicestatus", 0)
                frontDeviceAdapter.notifyDataSetChanged()
                deviceViewModel.resetFrontDeviceQuery()


            } else if (it == 1) {
                binding.selectAll.isChecked = false
                binding.selectOnline.isChecked = true
                shp.saveToSpInt("frontdevicestatus", 1)
                frontDeviceAdapter.notifyDataSetChanged()
                deviceViewModel.resetFrontDeviceQuery()

            }
        })

        getDevicesNumber(retrofit, shp.getFrontDeviceKeyWord()!!, shp.getFrontDeviceStatus()!!)


        //倒计时方法
        var countTIme:CountDownTimer



        binding.selectAll.setOnClickListener {
                deviceViewModel.selectState.value = 0
             countTIme  = object :CountDownTimer(300,300){
                override fun onTick(millisUntilFinished: Long) {
                   binding.selectOnline.isClickable = false

                }
                override fun onFinish() {
                    binding.selectOnline.isClickable = true

                }

            }
            countTIme.start()

        }



        binding.selectOnline.setOnClickListener {
            deviceViewModel.selectState.value = 1
            countTIme  = object :CountDownTimer(300,300){
                override fun onTick(millisUntilFinished: Long) {
                    binding.selectAll.isClickable = false
                }
                override fun onFinish() {
                    binding.selectAll.isClickable = true
                }

            }
            countTIme.start()

        }


        deviceViewModel.deviceAllNumber.observe(viewLifecycleOwner, Observer {
            binding.selectAll.text = "全部设备${it}台"
        })
        deviceViewModel.deviceOnlineNumber.observe(viewLifecycleOwner, Observer {
            binding.selectOnline.text = "在线设备${it}台"
        })


        //编辑设备
        binding.editDevice.setOnClickListener {

            val intent = Intent(requireActivity(), EditDeviceActivity::class.java)
            intent.putExtra("sn",deviceViewModel.deviceSn.value)

            startActivityForResult(intent,3)
        }

        binding.bindUser.setOnClickListener {
            val intent = Intent(requireActivity(), BindUserActivity::class.java)
            intent.putExtra("sn",deviceViewModel.deviceSn.value)
            startActivityForResult(intent,3)
        }


        val linearLayout = LinearLayoutManager(requireContext())
        binding.deviceFragmentRecyclerView.apply {
            adapter = frontDeviceAdapter
            layoutManager = linearLayout
        }

        deviceViewModel.frontDeviceListLiveData?.observe(viewLifecycleOwner, Observer {

            frontDeviceAdapter.submitList(it)

            if (deviceViewModel.needToScrollToTop) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0, 0)
                }

                deviceViewModel.needToScrollToTop = false
            }

            if (it.size == 0){
                deviceViewModel.listSize.value = 0
            }else {
                deviceViewModel.listSize.value = 1
            }
        })

        deviceViewModel.listSize.observe(requireActivity(), Observer {
            lifecycleScope.launch {
                delay(50)
                if (it == 0){
                    binding.editDevice.visibility = View.GONE
                    binding.bindUser.visibility = View.GONE
                    binding.bindUserName.visibility = View.VISIBLE
                    binding.bindUserName.text = "--"
                    binding.cardSn.text  = "--"
                    binding.age.text  = "--"
                    binding.workMode2.text  = "--"
                    binding.bindUserTime.text  = "--"
                    binding.heartRate.text  = "--"
                    binding.bloodOxygen.text  = "--"
                    binding.breath.text  = "--"
                    binding.roomTemperature.text  = "--"
                    binding.network.visibility = View.GONE
                    binding.netWorkStatus.text ="--"
                    binding.monitorFrmwareVersion.text ="--"
                    binding.monitorStatus.text ="--"
                    binding.connectionStatus.text ="--"
                    binding.ringFirmwareVersion.text ="--"
                    binding.ringStatus.text ="--"
                    binding.dataStatus.text ="--"
                    binding.ringSN.text ="--"
                    binding.electricImage.visibility = View.GONE
                    binding.electricPowerNumber.visibility = View.GONE
                    binding.electricGif.visibility = View.GONE


                }else {
                    binding.editDevice.visibility = View.VISIBLE
                }
            }

        })

        //下滑刷新重新请求列表
        binding.frontDeviceSwiperefreshLayout.setOnRefreshListener {
//            frontDeviceAdapter.notifyDataSetChanged()
//            deviceViewModel.resetFrontDeviceQuery()
//            deviceViewModel.frontDevicePosition.value = 0
            deviceViewModel.frontDeviceRefresh.value = 1
        }
        deviceViewModel.frontDeviceNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == FrontDeviceNetWorkStatus.FRONT_DEVICE_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                deviceViewModel.needToScrollToTop = true
            }
            frontDeviceAdapter.updateNetWorkStatus(it)
            binding.frontDeviceSwiperefreshLayout.isRefreshing =
                it == FrontDeviceNetWorkStatus.FRONT_DEVICE_INITIAL_LOADING
        })


        binding.frontDeviceListSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code

                shp.saveToSp(
                    "frontdevicekeyword",
                    binding.frontDeviceListSearch.text.toString().trim()
                )
//                frontDeviceAdapter.notifyDataSetChanged()
//                deviceViewModel.resetFrontDeviceQuery()
//                deviceViewModel.frontDevicePosition.value = 0
                deviceViewModel.frontDeviceRefresh.value = 1
                hideKeyboard(v, requireContext())
//                getTotalCount(retrofit,binding.userUserListSearch.text.toString().trim())
                getDevicesNumber(
                    retrofit,
                    shp.getFrontDeviceKeyWord()!!,
                    shp.getFrontDeviceStatus()!!
                )
            }
            false
        }

        binding.cancelSearch.setOnClickListener {
            binding.frontDeviceListSearch.setText("")
            shp.saveToSp("frontdevicekeyword", "")
            binding.cancelSearch.visibility = View.GONE
//            frontDeviceAdapter.notifyDataSetChanged()
//            deviceViewModel.resetFrontDeviceQuery()
//            deviceViewModel.frontDevicePosition.value = 0
            deviceViewModel.frontDeviceRefresh.value = 1
            getDevicesNumber(retrofit, shp.getFrontDeviceKeyWord()!!, shp.getFrontDeviceStatus()!!)
        }

        binding.frontDeviceListSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")) {
                    shp.saveToSp("frontdevicekeyword", "")
//                    frontDeviceAdapter.notifyDataSetChanged()
//                    deviceViewModel.resetFrontDeviceQuery()
                    binding.cancelSearch.visibility = View.GONE
                } else {
                    binding.cancelSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        var t = 0
        deviceViewModel.deviceSn.observe(viewLifecycleOwner, Observer {
            deviceViewModel.sn.value = it
            deviceViewModel.sn

            getDeviceInfo(retrofit, deviceViewModel.sn.value!!)
            t = 0

        })







        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                t++
                if (t >= 30) {
                    val message = Message()
                    message.what = 0
                    handler.sendMessage(message)
                    t = 0
                }
                Log.d("timer", t.toString())
            }
        }, 0, 1000)



        deviceViewModel.frontDeviceRefresh.observe(viewLifecycleOwner, Observer {
            if (it == 1){
                frontDeviceAdapter.notifyDataSetChanged()
                deviceViewModel.resetFrontDeviceQuery()
                deviceViewModel.frontDevicePosition.value = 0
                deviceViewModel.frontDeviceRefresh.value = 0
                getDevicesNumber(retrofit, shp.getFrontDeviceKeyWord()!!, shp.getFrontDeviceStatus()!!)
            }
        })
    }


    private val handler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0){
                getDeviceInfo(retrofit, deviceViewModel.sn.value!!)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            3 -> {
                if (resultCode == RESULT_OK){
                    deviceViewModel.frontDeviceRefresh.value = 1

                }

            }
            else -> {
                Log.d("DeviceFragment", "onActivityResult:没刷新 ")
            }
        }
    }

    fun hideKeyboard(view: View, context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getDevicesNumber(retrofitSingleton: RetrofitSingleton, keywords: String, devStatus: Int) {
        retrofitSingleton.api().getFrontDeviceList(1, shp.getHospitalId()!!, keywords, 1,1, devStatus)
            .enqueue(object : Callback<DataClassFrontDevice> {
                override fun onResponse(
                    call: Call<DataClassFrontDevice>,
                    response: Response<DataClassFrontDevice>
                ) {

                    if (response.body()?.code == 0) {
                        deviceViewModel.deviceAllNumber.value = response.body()?.data?.totalNum
                        deviceViewModel.deviceOnlineNumber.value = response.body()?.data?.onlineNum
                    }else if ( response.body()?.code == 10010 ||  response.body()?.code == 10004) {
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
                    }
                }

                override fun onFailure(call: Call<DataClassFrontDevice>, t: Throwable) {
                    ToastUtils.showTextToast(requireContext(), "获取设备数量网络请求失败")
                }

            })
    }


    /**
     * 获取设备信息
     */
    fun getDeviceInfo(retrofitSingleton: RetrofitSingleton, keywords: String) {

        retrofitSingleton.api().getDeviceInfo("/v2/deviceInfo/$keywords").enqueue(object :Callback<DataClassDeviceInfo>{
            override fun onResponse(
                call: Call<DataClassDeviceInfo>,
                response: Response<DataClassDeviceInfo>
            ) {

               if (response.body() != null){
                   if (response.body()?.code == 0){
                       binding.cardSn.text = response.body()?.data?.sn



//                       Log.d("deviceinfo", "指环SN:${response.body()?.data?.ringsn} ")
//                       Log.d("deviceinfo", "报告状态:${response.body()?.data?.reportStatus} ")
                       Log.d("deviceinfo", "${response.body()} ")

                       //用户姓名
                       if (response.body()?.data?.truename.equals("")){

                           binding.bindUserName.text ="--"
                           if (response.body()?.data?.status == 5 || response.body()?.data?.status == 10){
                               binding.bindUser.visibility = View.GONE
                               binding.bindUserName.visibility =View.VISIBLE
                           }else {
                               binding.bindUser.visibility = View.VISIBLE
                               binding.bindUserName.visibility = View.GONE
                           }

                       }else {
                           binding.bindUserName.visibility = View.VISIBLE
                           binding.bindUser.visibility = View.GONE
                           binding.bindUserName.text = response.body()?.data?.truename
                       }

                       //工作模式
                       when(response.body()?.data?.modeType){
                           0 -> binding.workMode2.text = "成人模式"
                           1 -> binding.workMode2.text = "儿童模式"
                           else -> binding.workMode2.text = ""
                       }

                       //年龄
                       if (response.body()?.data?.age.equals("")){
                           binding.age.text = "--"
                       }else {
                           binding.age.text = "${response.body()?.data?.age}岁"
                       }


                       //监测时段
                       if (response.body()?.data?.monitor.equals("")){
                           binding.bindUserTime.text = "--"
                       }else {
                           binding.bindUserTime.text = response.body()?.data?.monitor?.replace(" ","")
                       }


                       //心率
                       if (response.body()?.data?.heartrate.equals("0")){
                           binding.heartRate.text = "--"
                       }else {
                           binding.heartRate.text = response.body()?.data?.heartrate
                       }

                       //血氧
                       if (response.body()?.data?.bloodoxygen.equals("0")){
                           binding.bloodOxygen.text = "--"
                       }else {
                           binding.bloodOxygen.text = response.body()?.data?.bloodoxygen
                       }

                       //呼吸
                       if (response.body()?.data?.breathrate.equals("0")){
                           binding.bloodOxygen.text = "--"
                       }else {
                           binding.bloodOxygen.text = response.body()?.data?.breathrate
                       }

                       //室温
                       if (response.body()?.data?.tempetature.equals("0")){
                           binding.roomTemperature.text = "--"
                       }else {
                           binding.roomTemperature.text = response.body()?.data?.tempetature
                       }

                       //设备状态
                       when (response.body()?.data?.devStatus) {
                           1 -> {
                               //设备
                               binding.netWorkStatus.text = "未连接"
                               binding.monitorStatus.text = "--"
                               //设备网络
                               binding.network.visibility = View.GONE
                           }
                           2 -> {
                               binding.netWorkStatus.text = "已连接"
                               binding.monitorStatus.text = "未监测"
                               //设备网络
                               binding.network.visibility = View.VISIBLE
                           }
                           3 -> {
                               binding.netWorkStatus.text = "已连接"
                               binding.monitorStatus.text = "已离床"
                               //设备网络
                               binding.network.visibility = View.VISIBLE
                           }
                           4 -> {
                               binding.netWorkStatus.text = "已连接"
                               binding.monitorStatus.text = "监测中"
                               //设备网络
                               binding.network.visibility = View.VISIBLE
                           }
                           5 -> {
                               binding.netWorkStatus.text = "已连接"
                               binding.monitorStatus.text = "活动中"
                               //设备网络
                               binding.network.visibility = View.VISIBLE
                           }
                       }


                       //戒指状态
                       when(response.body()?.data?.ringStatus){
                           0 -> {
                               binding.connectionStatus.text = "未开启"
                               binding.ringStatus.text = "--"
                               //电量图标
                               binding.electricImage.visibility = View.GONE
                               binding.electricGif.visibility = View.GONE
                               //电量显示
                               binding.electricPowerNumber.visibility = View.GONE

                           }
                           1 -> {
                               binding.connectionStatus.text = "已开启"
                               binding.ringStatus.text = "未监测"
                               //电量显示
                               binding.electricPowerNumber.visibility = View.VISIBLE
                               //戒指是否充电
                               when(response.body()?.data?.powerStatus){
                                   1 -> {
                                       binding.electricGif.visibility = View.VISIBLE
                                       binding.electricImage.visibility = View.GONE
                                   }
                                   2 -> {
                                       binding.electricGif.visibility = View.VISIBLE
                                       binding.electricImage.visibility = View.GONE
                                   }
                                   else -> {
                                       binding.electricGif.visibility = View.GONE
                                       binding.electricImage.visibility = View.VISIBLE
                                   }
                               }
                           }
                           2 -> {
                               binding.connectionStatus.text = "已开启"
                               binding.ringStatus.text = "监测中"
                               //电量显示
                               binding.electricPowerNumber.visibility = View.VISIBLE
                               //戒指是否充电
                               when(response.body()?.data?.powerStatus){
                                   1 -> {
                                       binding.electricGif.visibility = View.VISIBLE
                                       binding.electricImage.visibility = View.GONE
                                   }
                                   2 -> {
                                       binding.electricGif.visibility = View.VISIBLE
                                       binding.electricImage.visibility = View.GONE
                                   }
                                   else -> {
                                       binding.electricGif.visibility = View.GONE
                                       binding.electricImage.visibility = View.VISIBLE
                                   }
                               }
                           }
                       }

                       //信号
                       var mobilerssi = 0
                       var wifirssi= 0

                       if (response.body()?.data?.networktype == 0){
                           if (!response.body()?.data?.mobilerssi.equals("")){
                               mobilerssi = response.body()?.data?.mobilerssi?.toInt()!!
                               if(mobilerssi < -90 ){
                                   binding.network.setImageResource(R.mipmap.mobil_network_20)
                               }else if (mobilerssi >= -90 && mobilerssi <= -80){
                                   binding.network.setImageResource(R.mipmap.mobil_network_40)
                               }else if (mobilerssi >= -80 && mobilerssi < -70){
                                   binding.network.setImageResource(R.mipmap.mobil_network_60)
                               }else if (mobilerssi >= -70 && mobilerssi < -60){
                                   binding.network.setImageResource(R.mipmap.mobil_network_80)
                               }else if (mobilerssi >= -60){
                                   binding.network.setImageResource(R.mipmap.mobil_network_100)
                               }
                           }else {
                               binding.network.visibility = View.GONE
                           }
                       }else if (response.body()?.data?.networktype == 1){
                           if (!response.body()?.data?.mobilerssi.equals("")){
                               wifirssi = response.body()?.data?.mobilerssi?.toInt()!!
                               if(wifirssi < -90 ){
                                   binding.network.setImageResource(R.mipmap.wifi_network_20)
                               }else if (wifirssi >= -90 && wifirssi <= -80){
                                   binding.network.setImageResource(R.mipmap.wifi_network_40)
                               }else if (wifirssi >= -80 && wifirssi < -70){
                                   binding.network.setImageResource(R.mipmap.wifi_network_60)
                               }else if (wifirssi >= -70 && wifirssi < -60){
                                   binding.network.setImageResource(R.mipmap.wifi_network_80)
                               }else if (wifirssi >= -60){
                                   binding.network.setImageResource(R.mipmap.wifi_network_100)
                               }
                           }else {
                               binding.network.visibility = View.GONE
                           }
                       }else {
                           binding.network.visibility = View.GONE
                       }

                       //固件版本
                       if (response.body()?.data?.versionno.equals("")){
                           binding.monitorFrmwareVersion.text = "--"
                       }else {
                           binding.monitorFrmwareVersion.text = response.body()?.data?.versionno
                       }


                       //戒指电量
                       val battery = response.body()?.data?.battery!!
                       if ( battery > 0 && battery <= 20  ){
                           binding.electricImage.setImageResource(R.mipmap.electric20_icon)
                       }else if (battery >= 20 && battery < 40 ){
                           binding.electricImage.setImageResource(R.mipmap.electric40_icon)
                       }else if (battery >= 40 && battery < 60){
                           binding.electricImage.setImageResource(R.mipmap.electric60_icon)
                       }else if (battery >= 60 && battery < 80){
                           binding.electricImage.setImageResource(R.mipmap.electric80_icon)
                       }else if (battery >= 80 && battery <= 100){
                           binding.electricImage.setImageResource(R.mipmap.electric100_icon)
                       }




                       //戒指固件版本
                       if (response.body()?.data?.swversion.equals("")){
                           binding.ringFirmwareVersion.text = "--"
                       }else {
                           binding.ringFirmwareVersion.text = response.body()?.data?.swversion
                       }


                       //数据状态
                       when(response.body()?.data?.reportStatus){
                           1 -> binding.dataStatus.text = "戒指数据未收取"
                           else -> binding.dataStatus.text = "戒指数据已收取"
                       }


                       //指环SN
                       if(response.body()?.data?.ringsn.equals("")){
                           binding.ringSN.text ="--"
                       }else {
                           binding.ringSN.text =response.body()?.data?.ringsn
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
                   }


               }





            }

            override fun onFailure(call: Call<DataClassDeviceInfo>, t: Throwable) {


                ToastUtils.showTextToast(requireContext(),"设备详情网络请求失败")
            }

        })




}

    object ViewClickDelay {
        var hash: Int = 0
        var lastClickTime: Long = 0
        var SPACE_TIME: Long = 3000
    }

    infix fun View.clickDelay2(clickAction: () -> Unit) {
        this.setOnClickListener {
            if (this.hashCode() != hash) {
                hash = this.hashCode()
                lastClickTime = System.currentTimeMillis()
                clickAction()
            } else {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > SPACE_TIME) {
                    lastClickTime = System.currentTimeMillis()
                    clickAction()
                }
            }
        }
    }
}

