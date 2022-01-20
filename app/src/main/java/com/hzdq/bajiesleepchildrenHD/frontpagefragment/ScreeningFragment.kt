package com.hzdq.bajiesleepchildrenHD.frontpagefragment

import android.Manifest
import android.app.Activity.RESULT_OK
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
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.common.BitmapUtils
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentScreeningBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassMyQuestionList
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassQuestionInfo
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassQuestionMarketList
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassTaskInfo
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.screen.activities.ScreenCheckResultListActivity
import com.hzdq.bajiesleepchildrenHD.screen.activities.ScreenNewTaskActivity
import com.hzdq.bajiesleepchildrenHD.screen.adapter.FrontScreenAdapter
import com.hzdq.bajiesleepchildrenHD.screen.adapter.MyQuestionListAdapter
import com.hzdq.bajiesleepchildrenHD.screen.adapter.QuestionMarketListAdapter
import com.hzdq.bajiesleepchildrenHD.screen.dialog.CheckPhotoBitmapQRDialog
import com.hzdq.bajiesleepchildrenHD.screen.paging.FrontScreenNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.user.activities.REQUEST_WRITE_EXTERNAL_STORAGE
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAdd2Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAddActivity
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * 筛查fragment
 */
const val SCREEN_ADD_QUESTION = 12
class ScreeningFragment : Fragment() {

    private lateinit var screenViewModel: ScreenViewModel
    private lateinit var binding: FragmentScreeningBinding
    private lateinit var   frontScreenAdapter :FrontScreenAdapter
    private lateinit var retrofit: RetrofitSingleton
    private lateinit var shp: Shp
    private var tokenDialog:TokenDialog? = null
    var bitmap: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_screening,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel = ViewModelProvider(requireActivity()).get(ScreenViewModel::class.java)

        shp = Shp(requireContext())
        retrofit = RetrofitSingleton.getInstance(requireContext())

        val map = mapOf(
            1 to binding.screeningTaskButton,
            2 to binding.myQuestionnaireButton,
            3 to binding.questionnaireMarketButton
        )
        val mapLayout = mapOf(
            1 to binding.layout1,
            2 to binding.layout2,
            3 to binding.layout3
        )

        screenViewModel.screenBarPosition.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected  = false
                it.value.setTextColor(Color.parseColor("#2051BD"))
            }
            mapLayout.forEach {
                it.value.visibility = View.GONE
            }
            when(it){
                1 -> {
                    binding.screeningTaskButton.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    binding.layout1.visibility = View.VISIBLE
                }
                2 -> {
                    binding.myQuestionnaireButton.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }

                    binding.layout2.visibility = View.VISIBLE
                }

                3 -> {
                    binding.questionnaireMarketButton.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    binding.layout3.visibility = View.VISIBLE
                }
                else -> {
                    map.forEach {
                        it.value.isSelected  = false
                        it.value.setTextColor(Color.parseColor("#2051BD"))
                    }

                    mapLayout.forEach {
                        it.value.visibility = View.GONE
                    }
                }
            }
        })


        binding.screeningTaskButton.setOnClickListener { screenViewModel.screenBarPosition.value = 1 }
        binding.myQuestionnaireButton.setOnClickListener { screenViewModel.screenBarPosition.value = 2 }
        binding.questionnaireMarketButton.setOnClickListener { screenViewModel.screenBarPosition.value = 3 }


        binding.editTaskButton.setOnClickListener {
            ToastUtils.showTextToast(requireContext(),"敬请期待！")
        }

        binding.statisticalAnalysisButton.setOnClickListener {
            ToastUtils.showTextToast(requireContext(),"敬请期待！")
        }
        binding.resultExportButton.setOnClickListener {
            ToastUtils.showTextToast(requireContext(),"敬请期待！")
        }

        binding.newTaskButton.setOnClickListener {

            startActivityForResult(Intent(requireActivity(), ScreenNewTaskActivity::class.java),1)
        }

        binding.checkResultButton.setOnClickListener {
            val intent = Intent(requireActivity(),ScreenCheckResultListActivity::class.java)
            intent.putExtra("name",screenViewModel.screenTitleTask.value)
            intent.putExtra("task_id",screenViewModel.taskId.value)
            startActivity(intent)
        }

        frontScreenAdapter = FrontScreenAdapter(screenViewModel, requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.screenRecyclerView.apply {
            adapter = frontScreenAdapter
            layoutManager = linearLayout
        }
        screenViewModel.refreshFrontScreen.value = 1
        screenViewModel.frontScreenListLiveData.observe(viewLifecycleOwner, Observer {

            frontScreenAdapter.notifyDataSetChanged()
            frontScreenAdapter.submitList(it)

            if (screenViewModel.needToScrollToTop) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0, 0)
                }

                screenViewModel.needToScrollToTop = false
            }

            if(it.size == 0){
                screenViewModel.listSize.value = 0
            }else {
                screenViewModel.listSize.value = 1
            }

        })

        screenViewModel.refreshFrontScreen.observe(viewLifecycleOwner, Observer {
            when(it){
                1 -> {
                    frontScreenAdapter.notifyDataSetChanged()
                    screenViewModel.resetFrontScreenQuery()
                    screenViewModel.screenFrontPosition.value = 0
                    screenViewModel.refreshFrontScreen.value = 0
                }
            }
        })

        //下滑刷新重新请求列表
        binding.frontScreenSwiperefreshLayout.setOnRefreshListener {
            screenViewModel.refreshFrontScreen.value = 1
        }

        screenViewModel.frontScreenNetWorkStatus.observe(viewLifecycleOwner, Observer {
            if (it == FrontScreenNetWorkStatus.FRONT_SCREEN_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                screenViewModel.needToScrollToTop = true
            }
            frontScreenAdapter.updateNetWorkStatus(it)
            binding.frontScreenSwiperefreshLayout.isRefreshing =
                it == FrontScreenNetWorkStatus.FRONT_SCREEN_INITIAL_LOADING
        })


        screenViewModel.taskId.observe(viewLifecycleOwner, Observer {


            if (screenViewModel.listSize.value == 0){
                binding.questionTitleScreenTask.text = "--"
                binding.startTime.text = "--"
                binding.endTime.text = "--"
                binding.needLand.text = "--"
                binding.showResult.text = "--"
                binding.repeatSubmission.text = "--"
                binding.measuredPerson.text = "--"
                binding.browseTimes.text = "--"

                binding.QRCode.visibility = View.GONE
                binding.editTaskButton.visibility = View.GONE
                binding.checkResultButton.visibility = View.GONE
                binding.statisticalAnalysisButton.visibility = View.GONE
                binding.resultExportButton.visibility = View.GONE
            }else {
                binding.QRCode.visibility = View.VISIBLE
                binding.editTaskButton.visibility = View.VISIBLE
                binding.checkResultButton.visibility = View.VISIBLE
                binding.statisticalAnalysisButton.visibility = View.VISIBLE
                binding.resultExportButton.visibility = View.VISIBLE
                lifecycleScope.launch {
                    getTaskInfo(retrofit,it)
                }
            }
        })

        frontScreenAdapter.setOnItemClickListener(object :FrontScreenAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {
//                getTaskInfo(retrofit,it)
            }

        })


        screenViewModel.bitmapContent.observe(viewLifecycleOwner, Observer {

        })
        binding.QRCode.setOnClickListener {

            val checkPhotoBitmapQRDialog =
                screenViewModel.bitmap?.let { it1 ->
                    CheckPhotoBitmapQRDialog(requireContext(), it1,object :CheckPhotoBitmapQRDialog.ConfirmAction{
                        override fun onRightClick(bitmap:Bitmap) {

                            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_WRITE_EXTERNAL_STORAGE
                                )
                            }else {

                                saveImageToGallery(requireContext(), bitmap)
                            }
                        }

                    })
                }

            checkPhotoBitmapQRDialog?.show()


        }


        //我的问卷
        val type = "13,14"

        screenViewModel.refreshMyQuestion.value = 1
        screenViewModel.refreshMyQuestion.observe(viewLifecycleOwner, Observer {
            if (it == 1){
                getMyQuestionList(retrofit,shp.getHospitalId()!!,type)
                screenViewModel.refreshMyQuestion.value = 0
            }
        })
        val myQuestionListAdapter = MyQuestionListAdapter(screenViewModel,viewLifecycleOwner)
        binding.myQuestionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myQuestionListAdapter
        }

        screenViewModel.myQuestionList.observe(viewLifecycleOwner, Observer {
            myQuestionListAdapter.notifyDataSetChanged()
            myQuestionListAdapter.submitList(it)
        })


        binding.myQuestionPreview.setOnClickListener {
            when(screenViewModel.myQuestionType.value){
                13 -> {
                    val intent = Intent(requireActivity(),UserScreenAdd2Activity::class.java)
                    intent.putExtra("screen",1)
                    startActivityForResult(intent,SCREEN_ADD_QUESTION)
                }
                14 -> {
                    val intent = Intent(requireActivity(),UserScreenAddActivity::class.java)
                    intent.putExtra("screen",1)
                    startActivityForResult(intent,SCREEN_ADD_QUESTION)
                }
            }

        }

        screenViewModel.myQuestionType.observe(viewLifecycleOwner, Observer {
            if (it != 0){
                getMyQuestionInfo(retrofit,it)
            }

        })

        screenViewModel.myQuestionTitle.observe(viewLifecycleOwner, Observer {
            binding.questionTitleMyQuestion.text = it
        })

        screenViewModel.myQuestionDescribe.observe(viewLifecycleOwner, Observer {
            binding.myQuestionIntroduction.text = it
        })

        screenViewModel.myQuestionStandard.observe(viewLifecycleOwner, Observer {
            binding.myQuestionGrading.text = it
        })

        screenViewModel.myQuestionListSize.observe(viewLifecycleOwner, Observer {
            binding.hasQuesionCount.text = "已拥有问卷${it}份"
        })

        //问卷市场
        getQuestionMarketList(retrofit,shp.getHospitalId()!!)

        val questionMarketListAdapter = QuestionMarketListAdapter()


        binding.questionMarketRecyclerView.apply {
            adapter = questionMarketListAdapter
            layoutManager = GridLayoutManager(requireContext(),5)
        }
        screenViewModel.questionMarketList.observe(viewLifecycleOwner, Observer {
            questionMarketListAdapter.notifyDataSetChanged()
            questionMarketListAdapter.submitList(it)
        })

        questionMarketListAdapter.setOnItemClickListener(object :QuestionMarketListAdapter.OnItemClickListener{
            override fun onItemClick(type: Int) {
                when(type){
                    13 -> {
                        val intent = Intent(requireActivity(),UserScreenAdd2Activity::class.java)
                        intent.putExtra("screen",1)
                        startActivityForResult(intent,SCREEN_ADD_QUESTION)
                    }
                    14 -> {
                        val intent = Intent(requireActivity(),UserScreenAddActivity::class.java)
                        intent.putExtra("screen",1)
                        startActivityForResult(intent,SCREEN_ADD_QUESTION)
                    }
                }
            }

        })



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
                saveImageToGallery(requireContext(),screenViewModel.bitmapQRCode!!)
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> {
                if(resultCode == RESULT_OK){
                    screenViewModel.refreshFrontScreen.value = 1
                }
            }
            SCREEN_ADD_QUESTION -> {
                if(resultCode == RESULT_OK){
                    screenViewModel.refreshMyQuestion.value = 1
                }

            }
            else -> {
                Log.d("onActivityResult", "onActivityResult: 没刷新")
            }
        }
    }

    /**
     * 获取问卷市场列表
     */
    fun getQuestionMarketList(retrofit: RetrofitSingleton,hospitalid:Int){
        retrofit.api().getQuestionMarketList(hospitalid,"13,14").enqueue(object :Callback<DataClassQuestionMarketList>{
            override fun onResponse(
                call: Call<DataClassQuestionMarketList>,
                response: Response<DataClassQuestionMarketList>
            ) {
                if (response.body()?.code == 0){
                    screenViewModel.questionMarketList.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<DataClassQuestionMarketList>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"问卷市场网络请求失败")
            }
        })
    }

    /**
     * 获取问卷详情
     */
    fun getMyQuestionInfo(retrofit: RetrofitSingleton,type: Int){
        retrofit.api().getMyQuestionInfo(type).enqueue(object :Callback<DataClassQuestionInfo>{
            override fun onResponse(
                call: Call<DataClassQuestionInfo>,
                response: Response<DataClassQuestionInfo>
            ) {
                if (response.body()?.code == 0){
                    screenViewModel.myQuestionTitle.value = response.body()?.data?.title
                    screenViewModel.myQuestionDescribe.value = response.body()?.data?.describe
                    screenViewModel.myQuestionStandard.value = response.body()?.data?.standard
                }
            }

            override fun onFailure(call: Call<DataClassQuestionInfo>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"我的问卷详情网络请求失败")
            }

        })
    }

    /**
     * 获取我的用户列表
     */
    fun getMyQuestionList(retrofit: RetrofitSingleton,hospitalid:Int,type:String){
        retrofit.api().getMyQuestionList(hospitalid,type).enqueue(object :Callback<DataClassMyQuestionList>{
            override fun onResponse(
                call: Call<DataClassMyQuestionList>,
                response: Response<DataClassMyQuestionList>
            ) {
                if (response.body()?.code == 0){

                    screenViewModel.myQuestionList.value = response.body()?.data
                    screenViewModel.myQuestionListSize.value = response.body()?.data?.size
                }
            }

            override fun onFailure(call: Call<DataClassMyQuestionList>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"我的问卷列表网络请求失败")
            }

        })
    }

    private fun getTaskInfo(retrofit: RetrofitSingleton,task_id:Int){
        retrofit.api().getTaskInfoResult(task_id,shp.getHospitalId()!!).enqueue(object :Callback<DataClassTaskInfo>{
            override fun onResponse(
                call: Call<DataClassTaskInfo>,
                response: Response<DataClassTaskInfo>
            ) {

                if (response.body()?.data != null){
                    if (response.body()?.code == 0){
                        binding.questionTitleScreenTask.text = response.body()?.data?.name
                        screenViewModel.screenTitleTask.value = response.body()?.data?.name
                        if (response.body()?.data?.start != 0){
                            binding.startTime.text = "${timestamp2Date("${response.body()?.data?.start}","yyyy-MM-dd HH:mm")}"
                        }else {
                            binding.startTime.text = ""
                        }
                        if (response.body()?.data?.end != 0 ){
                            binding.endTime.text = "${timestamp2Date("${response.body()?.data?.end}","yyyy-MM-dd HH:mm")}"
                        }else {
                            binding.endTime.text = ""
                        }

                        when(response.body()?.data?.login){
                            0 -> binding.needLand.text = "否"
                            1 -> binding.needLand.text = "是"
                            else -> binding.needLand.text = ""
                        }
                        when(response.body()?.data?.resubmit){
                            0 -> binding.repeatSubmission.text = "否"
                            1 -> binding.repeatSubmission.text = "是"
                            else -> binding.repeatSubmission.text = ""
                        }
                        when(response.body()?.data?.shows){
                            0 -> binding.showResult.text = "否"
                            1 -> binding.showResult.text = "是"
                            else -> binding.showResult.text = ""
                        }
                        when(response.body()?.data?.modify){
                            0 -> binding.canModified.text = "否"
                            1 -> binding.canModified.text = "是"
                            else -> binding.canModified.text = ""
                        }

                        binding.measuredPerson.text = "${response.body()?.data?.num}次"
                        binding.browseTimes.text = "${response.body()?.data?.views}次"


//                        val content = "https://cloud.bajiesleep.com/report?reportURL=61aa93717ef12c0e0b706836"

                        //生成二维码
                        val content = "http://cloud.bajiesleep.com/children/h5/?taskID=${response.body()?.data?.id}"
//                        val content = "http://test.bajiesleep.com/children/h5/?taskID=${response.body()?.data?.id}"

                        screenViewModel.bitmapContent.value = content
                        screenViewModel.bitmap  = BitmapUtils.create2DCode(content)
//                        binding.QRCode.setImageBitmap(bitmap)

                        Log.d("taskurl", "$content ")



                    }
                }

            }

            override fun onFailure(call: Call<DataClassTaskInfo>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"筛查任务详情网络请求失败")
            }

        })
    }

}