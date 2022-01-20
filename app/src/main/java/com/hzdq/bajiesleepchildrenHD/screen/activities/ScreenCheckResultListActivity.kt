package com.hzdq.bajiesleepchildrenHD.screen.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityCheckResultListBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassQuestionResultList
import com.hzdq.bajiesleepchildrenHD.dataclass.QuestionResultListBody
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomePageListAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepReportListAdapter
import com.hzdq.bajiesleepchildrenHD.home.dialog.DateRangePickerDialog
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.screen.adapter.QuestionResultListAdapter
import com.hzdq.bajiesleepchildrenHD.screen.adapter.ScreenPageListAdapter
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenCheckListViewModel
import com.hzdq.bajiesleepchildrenHD.user.activities.CheckOSA_Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.CheckPSQ_Activity
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
@RequiresApi(Build.VERSION_CODES.O)
class ScreenCheckResultListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCheckResultListBinding
    private lateinit var screenCheckListViewModel: ScreenCheckListViewModel
    private var dateRangePickerDialog : DateRangePickerDialog? =null
    private lateinit var questionResultListAdapter: QuestionResultListAdapter
    private var tokenDialog: TokenDialog? = null
    val numberList:MutableList<Int> = ArrayList()
    var position = 1
    var pages = 1
    private lateinit var shp: Shp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_check_result_list)
        ActivityCollector2.removeActivity(this)
        HideUI(this).hideSystemUI()
        screenCheckListViewModel = ViewModelProvider(this).get(ScreenCheckListViewModel::class.java)
        val intent = intent
        val task_id = intent.getIntExtra("task_id",0)
        val name = intent.getStringExtra("name")

        shp = Shp(this)
        binding.title.text = "任务名称：$name"

        val questionResultListBody = QuestionResultListBody()

        questionResultListBody.task_id = "$task_id"
        questionResultListBody.limit = 5
        questionResultListBody.hospital_id = shp.getHospitalId()!!.toString()


        questionResultListAdapter = QuestionResultListAdapter()
        val retrofit = RetrofitSingleton.getInstance(this)
        EPSoftKeyBoardListener.setListener(this,object : EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                binding.checkListMotion.transitionToEnd()
            }

            override fun keyBoardHide(height: Int) {
                binding.checkListMotion.transitionToStart()
            }

        })

        postResultList(retrofit,questionResultListBody)

        val linearLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, VERTICAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }

        binding.checkResultRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = questionResultListAdapter
        }




        screenCheckListViewModel.rangeTime.observe(this, Observer {
            if (!it.equals("")){
                binding.rangeDate.text = it
                binding.cancelDate.visibility = View.VISIBLE
            }

        })

        //这里使用协程 因为直接打开不初始化会导致数据没有传递到adapter中, 延迟0.5秒 再传递数组到adapter中就可以进行初始化了
        lifecycleScope.launch {
            delay(300)
            screenCheckListViewModel.initPage()
//            screenCheckListViewModel.needFooters()

        }

//        var pages = screenCheckListViewModel.pages


//        for (i in 1..pages){
//            numberList.add(i)
//        }

        val pageNumberAdapter = ScreenPageListAdapter(this,screenCheckListViewModel,this,9)

        screenCheckListViewModel.page.observe(this, Observer {
            numberList.clear()
            for (i in 1..it){
                numberList.add(i)
            }
            pageNumberAdapter.notifyDataSetChanged()
            pageNumberAdapter.setNumbers(numberList)
        })

        pageNumberAdapter.setOnItemClickListener(object : ScreenPageListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                pages = position
                questionResultListBody.page = position
                postResultList(retrofit,questionResultListBody)
            }


        })

        val horizontalLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        binding.pageList.apply {

            layoutManager = horizontalLayoutManager
            adapter = pageNumberAdapter
        }


        screenCheckListViewModel.resultList.observe(this, Observer {
            questionResultListAdapter.notifyDataSetChanged()
            questionResultListAdapter.submitList(it)
        })


        screenCheckListViewModel.currentPage.observe(this, Observer {
            questionResultListBody.page = it
            lifecycleScope.launch {
                delay(100)
                binding.number.text = "${it.toString()}页"
                binding.pageCount.text = "共${screenCheckListViewModel.lastPage.value}页，${it}页"
                screenCheckListViewModel.lastPage.value  = screenCheckListViewModel.lastPage.value
                if (pages == screenCheckListViewModel.lastPage.value){
                    binding.nextPage.isClickable = false
                }else {
                    binding.nextPage.isClickable = true
                }
                if (pages == 1){
                    binding.frontPage.isClickable = false
                }else {
                    binding.frontPage.isClickable = true
                }
            }

            screenCheckListViewModel.jumpPage()
            if (it < 1){
                screenCheckListViewModel.currentPage.value = 1

            }

            if (it > screenCheckListViewModel.lastPage.value!!){
                screenCheckListViewModel.currentPage.value = screenCheckListViewModel.lastPage.value
            }



        })


        screenCheckListViewModel.jumpPagesPosition.observe(this,Observer {

            horizontalLayoutManager.scrollToPositionWithOffset(it,0)
        })





        binding.jumpPageButton.setOnClickListener {
            HideKeyboard.hideKeyboard(it,this)
            val page : String ? = binding.jumpPageEdit.text.toString()
            if (page.equals("")){
                screenCheckListViewModel.currentPage.value = 1
                pages = 1
            }else {
                pages = page?.toInt()!!

                if (binding.jumpPageEdit.text.toString().toInt() > screenCheckListViewModel.lastPage.value!!){

                    pages = screenCheckListViewModel.lastPage.value!!
                }else {
                    pages = binding.jumpPageEdit.text.toString().toInt()
                }


                screenCheckListViewModel.currentPage.value = page?.toInt()
            }


            lifecycleScope.launch {

                questionResultListBody.page = pages
                delay(100)
                postResultList(retrofit,questionResultListBody)
            }
        }

        binding.nextPage.setOnClickListener {
            if (pages > screenCheckListViewModel.lastPage.value!!){
                pages == screenCheckListViewModel.lastPage.value
            }else {
                pages += 1
            }
            screenCheckListViewModel.currentPage.value = screenCheckListViewModel.currentPage.value?.plus(1)
            lifecycleScope.launch {
                questionResultListBody.page = pages
                delay(100)
                postResultList(retrofit,questionResultListBody)
            }
        }

        binding.frontPage.setOnClickListener {
            if (pages < 1){
                pages = 1
            }else {
                pages -= 1
            }
            screenCheckListViewModel.currentPage.value =  screenCheckListViewModel.currentPage.value?.plus(-1)
            lifecycleScope.launch {
                questionResultListBody.page = pages
                delay(100)
                postResultList(retrofit,questionResultListBody)
            }
        }

        binding.back.setOnClickListener {
            this.finish()
        }


        binding.cancelDate.setOnClickListener {
            binding.cancelDate.visibility = View.GONE
            binding.rangeDate.text = ""

            questionResultListBody.start = ""
            questionResultListBody.end = ""
            postResultList(retrofit,questionResultListBody)
        }

        binding.choiceDate.setOnClickListener {
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
                        screenCheckListViewModel.startYear.value = startYear
                        screenCheckListViewModel.startMonth.value = startMonth
                        screenCheckListViewModel.startDay.value = startDay
                        screenCheckListViewModel.endYear.value = endYear
                        screenCheckListViewModel.endMonth.value = endMonth
                        screenCheckListViewModel.endDay.value = endDay
                        screenCheckListViewModel.setRangeTime()
//                        ToastUtils.showTextToast(this@HomeReportListActivity,"${startYear}年${startMonth}月${startDay}日 - ${endYear}年${endMonth}月${endDay}日")
//                        Toast.makeText(this@HomeReportListActivity,"${startYear}年${startMonth}月${startDay}日 - ${endYear}年${endMonth}月${endDay}日",
//                            Toast.LENGTH_SHORT).show()
                        val startTimes = date2Stamp("${(startYear)}-${startMonth}-${startDay} 00:00:00")?.substring(0,10)
//                        val endTimes = (date2Stamp("${endYear}-${(endMonth)}-${(endDay)} 23:59:59")?.toFloat()!! / 1000)
                        val endTimes = date2Stamp("${endYear}-${(endMonth)}-${(endDay)} 23:59:59")?.substring(0,10)
                        screenCheckListViewModel.startTime = startTimes!!
                        screenCheckListViewModel.endTime = endTimes!!
                        screenCheckListViewModel.currentPage.value = 1
                        pages = 1
                        questionResultListBody.start = startTimes!!
                        questionResultListBody.end = endTimes!!
                        postResultList(retrofit,questionResultListBody)
                    }

                })
                dateRangePickerDialog?.show()
                dateRangePickerDialog?.setCanceledOnTouchOutside(false)
            }else {
                dateRangePickerDialog?.show()
                dateRangePickerDialog?.setCanceledOnTouchOutside(false)
            }


        }


        questionResultListAdapter.setOnItemDetailClickListener(object :QuestionResultListAdapter.OnItemDetailClickListener{
            override fun onItemDetailClick(taskid: Int, type: Int) {

                if (type == 13){
                    val intent = Intent(this@ScreenCheckResultListActivity,CheckPSQ_Activity::class.java)
                    intent.putExtra("taskid",taskid)
                    startActivity(intent)
                }else if (type == 14){
                    val intent = Intent(this@ScreenCheckResultListActivity,CheckOSA_Activity::class.java)
                    intent.putExtra("taskid",taskid)
                    startActivity(intent)
                }

            }

        })


        binding.listSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    binding.cancelSearch.visibility = View.GONE
                }else {
                    binding.cancelSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.listSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code

                HideKeyboard.hideKeyboard(v,this)
                questionResultListBody.keyword = binding.listSearch.text.toString().trim()
                screenCheckListViewModel.currentPage.value = 1


                postResultList(retrofit,questionResultListBody)
            }
            false
        }

        binding.cancelSearch.setOnClickListener {
            HideKeyboard.hideKeyboard(it,this)
            questionResultListBody.keyword = ""
            binding.listSearch.setText("")
            screenCheckListViewModel.currentPage.value = 1
            postResultList(retrofit,questionResultListBody)
        }


    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }

    private fun postResultList(retrofit: RetrofitSingleton,questionResultListBody: QuestionResultListBody){

        retrofit.api().postQuestionResultList(questionResultListBody).enqueue(object :Callback<DataClassQuestionResultList>{
            override fun onResponse(
                call: Call<DataClassQuestionResultList>,
                response: Response<DataClassQuestionResultList>
            ) {


                if (response.body() != null){
                    if (response.body()?.code == 0){
                        val list = response.body()?.data?.data


                        screenCheckListViewModel.page.value = response.body()!!.data.lastPage
//                        homeViewModel.pages = response.body()!!.data.lastPage
                        screenCheckListViewModel.lastPage.value = response.body()!!.data.lastPage
                        screenCheckListViewModel.resultList.value = null
                        screenCheckListViewModel.resultList.value = list
                    }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@ScreenCheckResultListActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@ScreenCheckResultListActivity,
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
                }else {
                    ToastUtils.showTextToast(this@ScreenCheckResultListActivity,"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassQuestionResultList>, t: Throwable) {
                ToastUtils.showTextToast(this@ScreenCheckResultListActivity,"筛查任务列表网络请求失败")
            }

        })
    }
}