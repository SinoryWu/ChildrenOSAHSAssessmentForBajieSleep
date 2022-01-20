package com.hzdq.bajiesleepchildrenHD.setting.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.common.BitmapUtils
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentSettingRegisterBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.screen.dialog.CheckPhotoBitmapQRDialog
import com.hzdq.bajiesleepchildrenHD.setting.dialog.RegisterDialog
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel
import com.hzdq.bajiesleepchildrenHD.user.activities.REQUEST_WRITE_EXTERNAL_STORAGE
import com.hzdq.bajiesleepchildrenHD.utils.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.HashMap


class SettingRegisterFragment : Fragment() {
    private var tokenDialog: TokenDialog? = null
    private var registerDialog: RegisterDialog? = null
    private lateinit var binding:FragmentSettingRegisterBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var retrofit: RetrofitSingleton
    private lateinit var shp: Shp
    private var urlRegister: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=  DataBindingUtil.inflate(inflater,R.layout.fragment_setting_register, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        retrofit = RetrofitSingleton.getInstance(requireContext())
        shp  = Shp(requireContext())
        val startTime = "1262275260"
        val newTaskBody = NewTaskBody()
        newTaskBody.hospital_id = shp.getHospitalId().toString()
        newTaskBody.login = "1"
        newTaskBody.resubmit = "0"
        newTaskBody.shows = "1"
        newTaskBody.modify = "0"
        newTaskBody.start = startTime

        urlRegister = OkhttpSingleton.BASE_URL+"/v2/accessmenTaskInfo?register=1&hospital_id=${shp.getHospitalId()!!}"

        val map1 = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )
        settingViewModel.register.observe(requireActivity(), Observer {
            map1.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 -> {
                    binding.b12.isSelected = true
                    binding.b12.setTextColor(Color.parseColor("#FFFFFF"))
                    val endTime = "1262361660"
                    newTaskBody.end = endTime
                }
                2 -> {
                    binding.b11.isSelected = true
                    binding.b11.setTextColor(Color.parseColor("#FFFFFF"))
                    val endTime = "1999999999"
                    newTaskBody.end = endTime
                }
                else -> {
                    map1.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b11.setOnClickListener { settingViewModel.register.value = 2 }
        binding.b12.setOnClickListener { settingViewModel.register.value = 1 }

        val map2 = mapOf(
            1 to binding.osa,
            2 to binding.psq
        )
        settingViewModel.question.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    binding.osa.isChecked = true
                    newTaskBody.type = "14"

                }
                2 -> {
                    binding.psq.isChecked = true
                    newTaskBody.type = "13"

                }
                else -> {
                    map2.forEach {
                        it.value.isChecked = false

                    }
                }
            }
        })

        binding.osa.setOnClickListener { settingViewModel.question.value = 1 }
        binding.psq.setOnClickListener { settingViewModel.question.value = 2 }


        binding.name.text = shp.getUserName()


        binding.cancel.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()

        }


        getTaskInfoRegister(urlRegister!!)
        binding.confirm.setOnClickListener {

            if (settingViewModel.question.value == 0 || settingViewModel.register.value == 0){
                Toast.makeText(requireContext(),"有选项还未选择",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(registerDialog == null){
                registerDialog = RegisterDialog(requireContext(),object :RegisterDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick() {
                        val url = OkhttpSingleton.BASE_URL+"/v2/accessmenTaskSave"
                        val maps = HashMap<String, String>()
                        maps["hospital_id"] = shp.getHospitalId().toString()
                        maps["start"] = newTaskBody.start
                        maps["end"] = newTaskBody.end
                        maps["login"] = "1"
                        maps["resubmit"] = "0"
                        maps["shows"] = "1"
                        maps["modify"] = "0"

                        maps["type"] = newTaskBody.type
                        maps["id"] = ""
                        maps["register"] = settingViewModel.register.value.toString()
                        postNewTask(url,maps)
                    }

                })
                registerDialog?.show()
                registerDialog?.setCanceledOnTouchOutside(false)
            }else {
                registerDialog?.show()
                registerDialog?.setCanceledOnTouchOutside(false)
            }
        }
    }

    fun postNewTask(url:String,map: HashMap<String, String>) {
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

                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "网络请求失败") }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val res = response.body()!!.string()


                //封装java对象
                val dataClassNewScreenTask = DataClassNewScreenTask()
                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("msg")
                    val data = jsonObject.optString("data")

                    //第一层封装
                    dataClassNewScreenTask.code = code
                    dataClassNewScreenTask.msg = msg
                   dataClassNewScreenTask.data = data.toString()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                requireActivity().runOnUiThread {
                    if (dataClassNewScreenTask.code == 0) {
                        Toast.makeText(requireContext(),"保存成功",Toast.LENGTH_SHORT).show()


                        val task_id = dataClassNewScreenTask.data.toInt()


//                        getTaskInfo(retrofit, task_id)
                        urlRegister?.let { getTaskInfoRegister(it) }
                    }else if ( dataClassNewScreenTask.code == 10010 ||  dataClassNewScreenTask.code == 10004) {
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
                        ToastUtils.showTextToast(
                            requireContext(),
                            "${dataClassNewScreenTask.msg}"
                        )
                    }
                }
            }
        })
    }


    fun saveImageToGallery(context: Context, bmp: Bitmap) {
        //检查有没有存储权限
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Toast.makeText(context,"请至权限中心打开应用权限",Toast.LENGTH_SHORT).show()
        } else {
            // 新建目录appDir，并把图片存到其下
            val appDir =
                File(context.getExternalFilesDir(null)?.getPath().toString() + "BarcodeBitmap")
            if (!appDir.exists()) {
                appDir.mkdir()
            }
            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val file = File(appDir, fileName)
            try {
                val fos = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // 把file里面的图片插入到系统相册中
            try {
                MediaStore.Images.Media.insertImage(
                    context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            // 通知相册更新
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTaskInfo(retrofit: RetrofitSingleton,task_id:Int){
        retrofit.api().getTaskInfoResult(task_id,shp.getHospitalId()!!).enqueue(object :Callback<DataClassTaskInfo>{
            override fun onResponse(
                call: Call<DataClassTaskInfo>,
                response: Response<DataClassTaskInfo>
            ) {

                if (response.body()?.data != null){
                    if (response.body()?.code == 0){


                        //生成二维码
                        val content = "http://cloud.bajiesleep.com/children/h5/?taskID=${response.body()?.data?.id}"
//                        val content = "http://test.bajiesleep.com/children/h5/?taskID=${response.body()?.data?.id}"

                        settingViewModel.bitmap  = BitmapUtils.create2DCode(content)
//                        binding.QRCode.setImageBitmap(bitmap)
                        binding.QRCode.setOnClickListener {

                            val checkPhotoBitmapQRDialog =
                                settingViewModel.bitmap?.let { it1 ->
                                    CheckPhotoBitmapQRDialog(requireContext(), it1,object :CheckPhotoBitmapQRDialog.ConfirmAction{
                                        override fun onRightClick(bitmap:Bitmap) {
                                            settingViewModel.bitmapQRCode  = bitmap
                                            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                                    REQUEST_WRITE_EXTERNAL_STORAGE
                                                )
                                            }else {

                                                saveImageToGallery(requireContext(), settingViewModel.bitmapQRCode!!)
                                            }
                                        }

                                    })
                                }

                            checkPhotoBitmapQRDialog?.show()


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

            override fun onFailure(call: Call<DataClassTaskInfo>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"筛查任务详情网络请求失败")
            }

        })
    }

    private fun getTaskInfoRegister(url:String){
        val request = Request.Builder()
            .url(url) //                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
            //                .addHeader("uid", "8")
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
            .addHeader("User-Agent", shp.getUserAgent())
            .get()
            .build()
        //3.将request封装为call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                requireActivity().runOnUiThread { ToastUtils.showTextToast2(requireContext(), "筛查任务详情网络请求失败") }
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val res = response.body()!!.string()


                //封装对象
                val dataClassTaskInfo = DataClassTaskInfo()
                val dataTaskInfo = DataTaskInfo()


                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val data = jsonObject.optJSONObject("data")
                    val msg = jsonObject.optString("msg")


                    //第一层封装

                    //第一层封装
                    dataClassTaskInfo.code = code
                    dataClassTaskInfo.msg = msg
                    if (data != null){
                        val id = data.optInt("id")
                        val type = data.optInt("type")
                        val name = data.optString("name")
                        val start = data.optInt("start")
                        val end = data.optInt("end")
                        val login = data.optInt("login")
                        val resubmit = data.optInt("resubmit")
                        val shows = data.optInt("shows")
                        val modify = data.optInt("modify")
                        val num = data.optInt("num")
                        val views = data.optInt("views")
                        val hospitalId = data.optInt("hospitalId")
                        val hospitalName = data.optString("hospitalName")
                        val status = data.optInt("status")
                        val register = data.optInt("register")


                        dataTaskInfo.id = id
                        dataTaskInfo.type = type
                        dataTaskInfo.name = name
                        dataTaskInfo.start = start
                        dataTaskInfo.end = end
                        dataTaskInfo.login = login
                        dataTaskInfo.resubmit = resubmit
                        dataTaskInfo.shows = shows
                        dataTaskInfo.modify = modify
                        dataTaskInfo.num = num
                        dataTaskInfo.views = views
                        dataTaskInfo.hospitalId = hospitalId
                        dataTaskInfo.hospitalName = hospitalName
                        dataTaskInfo.status = status
                        dataTaskInfo.register = register

                        dataClassTaskInfo.data = dataTaskInfo


                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                }

                requireActivity().runOnUiThread{

                    if (dataClassTaskInfo.code == 0){

                        if (dataTaskInfo.type == 13){
                            settingViewModel.question.value = 2
                        }else if (dataTaskInfo.type == 14){
                            settingViewModel.question.value = 1
                        }

                        settingViewModel.register.value = dataTaskInfo.register

                        //生成二维码
                        val content = "http://cloud.bajiesleep.com/children/h5/?taskID=${dataTaskInfo.id}"
//                        val content = "http://test.bajiesleep.com/children/h5/?taskID=${dataTaskInfo.id}"

                        settingViewModel.bitmap  = BitmapUtils.create2DCode(content)
//                        binding.QRCode.setImageBitmap(bitmap)
                        binding.QRCode.setOnClickListener {

                            val checkPhotoBitmapQRDialog =
                                settingViewModel.bitmap?.let { it1 ->
                                    CheckPhotoBitmapQRDialog(requireContext(), it1,object :CheckPhotoBitmapQRDialog.ConfirmAction{
                                        override fun onRightClick(bitmap:Bitmap) {
                                            settingViewModel.bitmapQRCode  = bitmap
                                            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                                    REQUEST_WRITE_EXTERNAL_STORAGE
                                                )
                                            }else {

                                                saveImageToGallery(requireContext(), settingViewModel.bitmapQRCode!!)
                                            }
                                        }

                                    })
                                }

                            checkPhotoBitmapQRDialog?.show()


                        }



                    }else if ( dataClassTaskInfo.code == 10010 ||  dataClassTaskInfo.code == 10004) {
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
                    }else if (dataClassTaskInfo.code == 10001){

                    }else {
                        ToastUtils.showTextToast(requireContext(),"${dataClassTaskInfo.msg}")
                    }

                }
            }
        })

//        retrofit.api().getTaskInfoRegister(1,shp.getHospitalId()!!).enqueue(object :Callback<DataClassTaskInfo>{
//            override fun onResponse(
//                call: Call<DataClassTaskInfo>,
//                response: Response<DataClassTaskInfo>
//            ) {
//
//                Log.d("asdasdsadasda", "onResponse:${response.body()} ")
//                if (response.body()?.data != null){
//                    if (response.body()?.code == 0){
//
//
//
//                        //生成二维码
//                        val content = "http://test.bajiesleep.com/children/h5/?taskID=${response.body()?.data?.id}"
//
//                        settingViewModel.bitmap  = BitmapUtils.create2DCode(content)
////                        binding.QRCode.setImageBitmap(bitmap)
//                        binding.QRCode.setOnClickListener {
//
//                            val checkPhotoBitmapQRDialog =
//                                settingViewModel.bitmap?.let { it1 ->
//                                    CheckPhotoBitmapQRDialog(requireContext(), it1,object :CheckPhotoBitmapQRDialog.ConfirmAction{
//                                        override fun onRightClick(bitmap:Bitmap) {
//                                            settingViewModel.bitmapQRCode  = bitmap
//                                            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                                                    REQUEST_WRITE_EXTERNAL_STORAGE
//                                                )
//                                            }else {
//
//                                                saveImageToGallery(requireContext(), settingViewModel.bitmapQRCode!!)
//                                            }
//                                        }
//
//                                    })
//                                }
//
//                            checkPhotoBitmapQRDialog?.show()
//
//
//                        }
//
//
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
//                    }else if (response.body()?.code == 10001){
//
//                    }else {
//                        ToastUtils.showTextToast(requireContext(),"${response.body()?.msg}")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<DataClassTaskInfo>, t: Throwable) {
//                ToastUtils.showTextToast(requireContext(),"筛查任务详情网络请求失败")
//            }
//
//        })
    }

    //处理权限请求回调
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                saveImageToGallery(requireContext(),settingViewModel.bitmapQRCode!!)
            }
        }
    }
}