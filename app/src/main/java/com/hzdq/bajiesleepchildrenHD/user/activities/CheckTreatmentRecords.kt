package com.hzdq.bajiesleepchildrenHD.user.activities

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckTreatmentRecordsBinding
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.adapter.TreatmentDetailPicAdapter
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val REQUEST_WRITE_EXTERNAL_STORAGE = 1
@RequiresApi(Build.VERSION_CODES.M)
class CheckTreatmentRecords : AppCompatActivity() {
    private lateinit var binding:ActivityCheckTreatmentRecordsBinding
    private lateinit var treatmentDetailPicAdapter: TreatmentDetailPicAdapter
    private lateinit var retrofitSingleton: RetrofitSingleton
    private lateinit var userViewModel: UserViewModel
    private lateinit var shp: Shp
    private var tokenDialog: TokenDialog? = null
    private  var imageHeight:Int? = null
    private  var imageWidth:Int? = null
    private var bitmap:Bitmap? = null
    private var popupWindow:PopupWindow? = null
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_treatment_records)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        navController  = Navigation.findNavController(this,R.id.check_treatment_detail_fragment)
        val intent = intent
        val id =  intent.getIntExtra("id",0)
        userViewModel.uid.value = id
        val name =  intent.getStringExtra("name")
        userViewModel.name.value = name
//        retrofitSingleton = RetrofitSingleton.getInstance(this)
//        shp = Shp(this)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
//        binding.back.setOnClickListener {
//            finish()
//        }
//        binding.checkRepoertName.text = name
//
//        binding.checkReportDoctor.text = shp.getDoctorName()




//        getTreatDetail(retrofitSingleton,id)



    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()

        DataCleanManagerKotlin.cleanInternalCache(applicationContext)
    }

//    /**
//     * 获取诊疗记录详情
//     */
//    fun getTreatDetail(retrofitSingleton: RetrofitSingleton,id:Int){
//        retrofitSingleton.api().getTreatDetail(id).enqueue(object :Callback<DataClassTreatmentDetail>{
//            override fun onResponse(
//                call: Call<DataClassTreatmentDetail>,
//                response: Response<DataClassTreatmentDetail>
//            ) {
//                if (response.body()?.code == 0){
//                    binding.checkReportDoctor.text = response.body()?.data?.doctorName
//                    binding.time.text = "${timestamp2Date("${response.body()?.data?.createTime}","yyyy/MM//dd HH:ss")}"
//                    binding.oahi.text = response.body()?.data?.oahi
//                    when(response.body()?.data?.osas){
//                        1 -> binding.osas.text = "正常"
//                        2 -> binding.osas.text = "轻度"
//                        3 -> binding.osas.text = "中度"
//                        4 -> binding.osas.text = "重度"
//                    }
//
//                    binding.diagnosis.text = response.body()?.data?.opinion
//                    binding.checkReportContent.text = response.body()?.data?.treatment
//

//                    treatmentDetailPicAdapter = TreatmentDetailPicAdapter()
//                    response.body()?.data?.attachment?.let { treatmentDetailPicAdapter.setList(it) }
//
//                    val linearLayoutManager = LinearLayoutManager(this@CheckTreatmentRecords)
//                    binding.recyclerView.apply {
//                        adapter = treatmentDetailPicAdapter
//                        layoutManager = linearLayoutManager
//                    }
//
//                    treatmentDetailPicAdapter.setOnItemClickListener(object :TreatmentDetailPicAdapter.OnItemClickListener{
//                        override fun onItemClick(url: String) {
//                            val checkUrlPhotoDialog  = CheckUrlPhotoDialog(this@CheckTreatmentRecords,url,userViewModel,this@CheckTreatmentRecords,object :CheckUrlPhotoDialog.ConfirmAction{
//                                override fun onClick(photoView: PhotoView) {
//                                    showPuPopWindow(photoView)

//                                }
//
//                            })
//
//                            checkUrlPhotoDialog.show()
//
//
//                            val dialogWindow: Window = checkUrlPhotoDialog.getWindow()!!
//                            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
//                            dialogWindow.setGravity(Gravity.BOTTOM)
//                            val lp = dialogWindow.attributes
//                            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//                            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//                            lp.y = 0
//                            dialogWindow.attributes = lp
//
//
//
//                        }
//
//                    })
//
//                }
//            }
//
//            override fun onFailure(call: Call<DataClassTreatmentDetail>, t: Throwable) {
//                 ToastUtils.showTextToast(this@CheckTreatmentRecords,"获取诊疗记录详情网络请求失败")
//            }
//
//        })
//    }


    //显示PuPopWindow
    @RequiresApi(Build.VERSION_CODES.M)
    fun showPuPopWindow(popupView: View) {
        Log.d("TAG", "showPuPopWindow: ")
        var popupView = popupView
        popupView = View.inflate(this, R.layout.file_popview_item, null)

        var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        uiOptions = uiOptions or 0x00001000
        popupView.systemUiVisibility = uiOptions

        // 参数2,3：指明popupwindow的宽度和高度

        // 参数2,3：指明popupwindow的宽度和高度
        popupWindow = PopupWindow(
            popupView, UpdateReportActivity2.dip2px(this, 427f),
            UpdateReportActivity2.dip2px(this, 170f)
        )

        // 设置背景图片， 必须设置，不然动画没作用
//        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow!!.setFocusable(true)
        popupWindow!!.setOutsideTouchable(true)
        var animation: Animation
        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F,
            Animation.RELATIVE_TO_PARENT, 1F, Animation.RELATIVE_TO_PARENT, 0F
        )
        animation.setInterpolator(AccelerateInterpolator())
        animation.setDuration(250)

        //设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow!!.showAtLocation(
            findViewById<View>(R.id.check_photo_dialog_bottom),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )

//            popupWindow!!.showAsDropDown(binding.viewPager2,0,dip2px(requireContext(),3f))
        popupView.startAnimation(animation)

//            popupWindow!!.setOnDismissListener{
//                popupWindow == null
//            }
        popupView.findViewById<View>(R.id.popwindow_save).setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                 requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_WRITE_EXTERNAL_STORAGE)
            }else {
                lifecycleScope.launch {
                    userViewModel.checkDetailBitmap.value?.let { it1 -> savePhoto(it1) }
                }
            }
            popupWindow!!.dismiss()

        }
        popupView.findViewById<View>(R.id.popwindow_cancel).setOnClickListener {

            popupWindow!!.dismiss()


        }

        popupWindow!!.setOnDismissListener {
            popupWindow == null
        }

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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //保存图片是耗时操作，需要开启协程
                    lifecycleScope.launch {
                        userViewModel.checkDetailBitmap.value?.let { it1 -> savePhoto(it1) }
                    }
                }else {
                    Toast.makeText(this,"存储失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 保存图片
     * 将现有的图片转换为位图保存下来
     * 保存图片是一个耗时操作 需要开启一个协程执行这些操作
     * 先找到viewholder再找到图
     */
    private suspend fun  savePhoto(bitmap: Bitmap){
        withContext(Dispatchers.IO) {
            //从viewgroup的0号位置找到recyclerview 再找到viewholder
//            val holder = (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
            //toBitmap里面传递的两个参数是宽和高
//            val bitmap = imageWidth?.let { imageHeight?.let { it1 ->
//                    photoView.drawable.toBitmap(it, it1)
//                }
            }
            if (Build.VERSION.SDK_INT < 29){
                if (MediaStore.Images.Media.insertImage(this@CheckTreatmentRecords.contentResolver,bitmap,"","") == null){
                    //吐司如果不放在主线程里面就会报错
                    MainScope().launch {
                        ToastUtils.showTextToast(this@CheckTreatmentRecords,"保存失败")
                    }
                }else {
                    MainScope().launch {
                        ToastUtils.showTextToast(this@CheckTreatmentRecords,"保存成功")
                    }
                }
            }else {
                //保存图片用的uri
                val saveUri = this@CheckTreatmentRecords.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues()
                )?: kotlin.run {
                    MainScope().launch {
                        ToastUtils.showTextToast(this@CheckTreatmentRecords,"保存失败")
                    }
                    return
                }

                //use在用完之后可以将流自动关闭 用OutputStream写入流
                this@CheckTreatmentRecords.contentResolver.openOutputStream(saveUri).use {
                    //bitmap写入流都用compress
                    //使用jpg格式压缩率为90
                    if(bitmap?.compress(Bitmap.CompressFormat.JPEG,90,it) == true){
                        MainScope().launch {
                            ToastUtils.showTextToast(this@CheckTreatmentRecords,"保存成功")
                        }
                    }else{
                        MainScope().launch {
                            ToastUtils.showTextToast(this@CheckTreatmentRecords,"保存失败")

                    }
                }
            }
        }


    }


}