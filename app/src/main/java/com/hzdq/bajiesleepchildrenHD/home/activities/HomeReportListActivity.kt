package com.hzdq.bajiesleepchildrenHD.home.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityHomeReportListBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontHome
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassHomeReportSleep
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomePageListAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepReportListAdapter
import com.hzdq.bajiesleepchildrenHD.home.dialog.DateRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeReportListActivity : AppCompatActivity() {
    private var dateRangePickerDialog : DateRangePickerDialog? =null
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding: ActivityHomeReportListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeSleepReportListAdapter: HomeSleepReportListAdapter
    private lateinit var pageNumberAdapter: HomePageListAdapter
    val numberList = ArrayList<Int>()

    private lateinit var shp: Shp

    var pages = 1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shp = Shp(this)
      binding = DataBindingUtil.setContentView(this,R.layout.activity_home_report_list)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        Log.d("UserAgent", "onCreate: ${Shp(this).getUserAgent()}")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        EPSoftKeyBoardListener.setListener(this,object : EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                binding.homeListMainLayout.transitionToEnd()
            }

            override fun keyBoardHide(height: Int) {
                binding.homeListMainLayout.transitionToStart()
            }

        })

        homeSleepReportListAdapter = HomeSleepReportListAdapter()

        val retrofit = RetrofitSingleton.getInstance(this)

//        initSleepReport(retrofit)
        val shp = Shp(this)

        getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,1)


        val linearLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, VERTICAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        binding.homeReportListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = homeSleepReportListAdapter
        }


        homeSleepReportListAdapter.setOnItemDetailClickListener(object :HomeSleepReportListAdapter.OnItemDetailClickListener{
            override fun onItemDetailClick(url: String, fileName: String) {
                val intent = Intent(this@HomeReportListActivity,ReportInfoActivity::class.java)
                intent.putExtra("url",url)
                intent.putExtra("fileName",fileName)
                startActivity(intent)
            }

        })




        homeViewModel.rangeTime.observe(this, Observer {
            if (!it.equals("")){
                binding.homeRepostListDate.text = it
                binding.homeRepostListCancelDate.visibility = View.VISIBLE
            }

        })

        //这里使用协程 因为直接打开不初始化会导致数据没有传递到adapter中, 延迟0.5秒 再传递数组到adapter中就可以进行初始化了
        lifecycleScope.launch {
            delay(300)
            homeViewModel.initPage()
//            homeViewModel.needFooters()

        }


//        for (i in 1..pages){
//            numberList.add(i)
//        }

//        val pageNumberAdapter = PageListAdapter(this,homeViewModel,numberListAdapter,this)
        pageNumberAdapter = HomePageListAdapter(this,homeViewModel,this,9)



        homeViewModel.page.observe(this, Observer {
            numberList.clear()
            for (i in 1..it){
                 numberList.add(i)
            }
            pageNumberAdapter.notifyDataSetChanged()
            pageNumberAdapter.setNumbers(numberList)
        })


        pageNumberAdapter.setOnItemClickListener(object :HomePageListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                pages = position
                getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,pages)
            }

        })

        val horizontalLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        binding.pageListRecyclerView.apply {

            layoutManager = horizontalLayoutManager
            adapter = pageNumberAdapter
        }


//        pageNumberAdapter.notifyDataSetChanged()
//        pageNumberAdapter.setNumbers(numberList)
        homeViewModel.reportList.observe(this, Observer {
            homeSleepReportListAdapter.notifyDataSetChanged()
            homeSleepReportListAdapter.submitList(it)

        })


        homeViewModel.currentPage.observe(this, Observer {
            lifecycleScope.launch {
                delay(100)
                binding.number.text = "${it.toString()}页"
                binding.pageCount.text = "共${homeViewModel.lastPage.value}页，${it}页"
//
                homeViewModel.lastPage.value  = homeViewModel.lastPage.value
                if (pages == homeViewModel.lastPage.value){
                    binding.homeReportListNextPage.isClickable = false
                }else {
                    binding.homeReportListNextPage.isClickable = true
                }
                if (pages == 1){
                    binding.homeReportListFrontPage.isClickable = false
                }else {
                    binding.homeReportListFrontPage.isClickable = true
                }
            }

            homeViewModel.jumpPage()
            if (it < 1){
                homeViewModel.currentPage.value = 1

            }

            if (it > homeViewModel.lastPage.value!!){
                homeViewModel.currentPage.value = homeViewModel.lastPage.value
            }


//            getSleepReport(retrofit,keywords,homeViewModel.startTime,homeViewModel.endTime,it)

        })


        homeViewModel.jumpPagesPosition.observe(this,Observer {

            horizontalLayoutManager.scrollToPositionWithOffset(it,0)
        })



        binding.homeListJumpPageButton.setOnClickListener {
            hideKeyboard(it)
            val page : String ? = binding.homeListJumpPage.text.toString()
            if(page.equals("")){
                homeViewModel.currentPage.value = 1
                pages =  1
            }else {
                homeViewModel.currentPage.value = page?.toInt()
                if (binding.homeListJumpPage.text.toString().toInt() > homeViewModel.lastPage.value!!){

                    pages = homeViewModel.lastPage.value!!
                }else {
                    pages = binding.homeListJumpPage.text.toString().toInt()
                }
            }





            lifecycleScope.launch {

                getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,pages)
            }

        }





        binding.homeReportListNextPage.setOnClickListener {

            if (pages > homeViewModel.lastPage.value!!){
                pages == homeViewModel.lastPage.value
            }else {
                pages += 1
            }


            homeViewModel.currentPage.value = homeViewModel.currentPage.value?.plus(1)
            lifecycleScope.launch {
                delay(100)
                getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,pages)
            }

        }

        binding.homeReportListFrontPage.setOnClickListener {
            if (pages < 1){
                pages = 1
            }else {
                pages -= 1
            }

            homeViewModel.currentPage.value =  homeViewModel.currentPage.value?.plus(-1)

            getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,pages)
        }

        binding.homeRepostListBack.setOnClickListener {

            this.finish()
        }


        binding.homeRepostListSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                Log.d("点击", "onCreate: ")
                hideKeyboard(v)
                homeViewModel.keyWords = binding.homeRepostListSearch.text.toString().trim()
                homeViewModel.currentPage.value = 1
                lifecycleScope.launch {
                    delay(100)
                    getSleepReport(retrofit,shp.getHospitalId()!!, homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,1)
                }
            }

            false
        }

        binding.homeRepostListCancelSearch.setOnClickListener {
            binding.homeRepostListSearch.setText("")
            homeViewModel.keyWords = ""
            hideKeyboard(it)
            pages = 1
            homeViewModel.currentPage.value = 1
            lifecycleScope.launch {
                delay(100)
                getSleepReport(retrofit, shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,1)
            }
        }


        binding.homeRepostListSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    binding.homeRepostListCancelSearch.visibility = View.GONE
                }else {
                    binding.homeRepostListCancelSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.homeRepostListChoiceDate.setOnClickListener {
            if (dateRangePickerDialog == null){

                dateRangePickerDialog = DateRangePickerDialog(this,object :DateRangePickerDialog.ConfirmAction{
                    override fun onLeftClick() {

                    }

                    override fun onRightClick(
                        startYear: Int?,
                        startMonth: Int?,
                        startDay: Int?,
                        endYear: Int?,
                        endMonth: Int?,
                        endDay: Int?
                    ) {
                        homeViewModel.startYear.value = startYear
                        homeViewModel.startMonth.value = startMonth
                        homeViewModel.startDay.value = startDay
                        homeViewModel.endYear.value = endYear
                        homeViewModel.endMonth.value = endMonth
                        homeViewModel.endDay.value = endDay
                        homeViewModel.setRangeTime()
//                        ToastUtils.showTextToast(this@HomeReportListActivity,"${startYear}年${startMonth}月${startDay}日 - ${endYear}年${endMonth}月${endDay}日")
//                        Toast.makeText(this@HomeReportListActivity,"${startYear}年${startMonth}月${startDay}日 - ${endYear}年${endMonth}月${endDay}日",
//                            Toast.LENGTH_SHORT).show()

//                        val startTimes = (date2Stamp("${(startYear)}-${startMonth}-${startDay} 00:00:00")?.toFloat()!! / 1000)
                        val startTimes = date2Stamp("${(startYear)}-${startMonth}-${startDay} 00:00:00")?.substring(0,10)
//                        val endTimes = (date2Stamp("${endYear}-${(endMonth)}-${(endDay)} 23:59:59")?.toFloat()!! / 1000)
                        val endTimes = date2Stamp("${endYear}-${(endMonth)}-${(endDay)} 23:59:59")?.substring(0,10)
                        Log.d("HomeReportList", "startTimes:$startTimes ")
                        Log.d("HomeReportList", "endTimes:$endTimes ")


                        homeViewModel.startTime = startTimes!!
                        homeViewModel.endTime = endTimes!!
                        homeViewModel.currentPage.value = 1
                        pages = 1
                        getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,1)

                    }

                })
                dateRangePickerDialog?.show()
                dateRangePickerDialog?.setCanceledOnTouchOutside(false)
            }else {
                dateRangePickerDialog?.show()
                dateRangePickerDialog?.setCanceledOnTouchOutside(false)
            }


        }


        binding.homeRepostListCancelDate.setOnClickListener {
            binding.homeRepostListCancelDate.visibility = View.GONE
            binding.homeRepostListDate.text = ""
            homeViewModel.startTime = "0"
            homeViewModel.endTime = "0"
            pages = 1
            getSleepReport(retrofit,shp.getHospitalId()!!,homeViewModel.keyWords,homeViewModel.startTime,homeViewModel.endTime,1)
            homeViewModel.currentPage.value = 1


        }
    }


    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    /**
     * 分割数组
     */
    private fun splitList(list: MutableList<Int>, groupSize: Int): MutableList<MutableList<Int>>? {
        val length = list.size
        // 计算可以分成多少组
        val num = (length + groupSize - 1) / groupSize // TODO
        val newList: MutableList<MutableList<Int>> = ArrayList(num)
        for (i in 0 until num) {
            // 开始位置
            val fromIndex = i * groupSize
            // 结束位置
            val toIndex = if ((i + 1) * groupSize < length) (i + 1) * groupSize else length
            newList.add(list.subList(fromIndex, toIndex))
        }
        return newList
    }


    fun getSleepReport(retrofit: RetrofitSingleton,hospitalid:Int,kewords:String,startTime:String,endTime:String,page:Int){

        retrofit.api().getHomeSleepReport(10,hospitalid,kewords,startTime,endTime,page).enqueue(object :Callback<DataClassHomeReportSleep>{
            override fun onResponse(
                call: Call<DataClassHomeReportSleep>,
                response: Response<DataClassHomeReportSleep>
            ) {
                if (response.body()?.code == 0){

                    if (response.body() != null){
                        val list = response.body()?.data?.data


                        homeViewModel.page.value = response.body()!!.data.lastPage
//                        homeViewModel.pages = response.body()!!.data.lastPage
                        homeViewModel.lastPage.value = response.body()!!.data.lastPage
                        homeViewModel.reportList.value = null
                        homeViewModel.reportList.value = list
                    }


                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(this@HomeReportListActivity, object : TokenDialog.ConfirmAction {
                            override fun onRightClick() {
                                shp.saveToSp("token", "")
                                shp.saveToSp("uid", "")

                                startActivity(
                                    Intent(this@HomeReportListActivity,
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
                    ToastUtils.showTextToast(this@HomeReportListActivity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassHomeReportSleep>, t: Throwable) {
               ToastUtils.showTextToast(this@HomeReportListActivity,"报告列表网络请求失败")

            }

        })
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}