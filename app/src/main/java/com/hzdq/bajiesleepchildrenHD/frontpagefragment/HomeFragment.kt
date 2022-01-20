package com.hzdq.bajiesleepchildrenHD.frontpagefragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentHomeBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontHome
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontHomeCalculate
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassUserInfo
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.home.activities.HomeAddUserActivity
import com.hzdq.bajiesleepchildrenHD.home.activities.HomeBindDeviceActivity
import com.hzdq.bajiesleepchildrenHD.home.activities.HomeReportListActivity
import com.hzdq.bajiesleepchildrenHD.home.activities.ReportInfoActivity
import com.hzdq.bajiesleepchildrenHD.home.adapter.FrontHomeSleepAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeUserSearchAdapter
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeUserSearchNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.FrontHomeViewModel
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAdd2Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAddActivity
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 主页fragment
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var frontHomeSleepAdapter: FrontHomeSleepAdapter
    private lateinit var frontHomeViewModel: FrontHomeViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var retrofit : RetrofitSingleton
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay =  0
    private var datePicker: DatePicker? = null
    private lateinit var shp: Shp
    private lateinit var userSearchRecyclerView: RecyclerView
    private lateinit var userSearchRefresh: SwipeRefreshLayout
    private var customView: View? = null
    private var popupwindow: PopupWindow? = null
    private lateinit var homeViewModel: HomeViewModel

    private var tokenDialog:TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        datePicker = DatePicker(requireContext())
        currentYear = datePicker?.year!!
        currentMonth = datePicker?.month!!+1
        currentDay = datePicker?.dayOfMonth!!

        shp = Shp(requireContext())
        binding.homeTime.text = "${currentYear}.${currentMonth}.${currentDay}"
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)


        frontHomeViewModel = ViewModelProvider(requireActivity()).get(FrontHomeViewModel::class.java)
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)
        retrofit = RetrofitSingleton.getInstance(requireContext())


        homeViewModel.refreshHome.observe(viewLifecycleOwner, Observer {
            when(it){
                1 -> {
                    getUserInfo(retrofit)
                    homeViewModel.refreshHome.value = 0
                }
            }
        })

        Log.d("token", "token: ${shp.getToken()}")
        shp.saveToSp("homeusersearchkeyword","")


        binding.homeCheckAllReportList.setOnClickListener {
            startActivity(Intent(requireActivity(),HomeReportListActivity::class.java))

        }

        frontHomeSleepAdapter = FrontHomeSleepAdapter()
        frontHomeViewModel.frontHomeXList.observe(viewLifecycleOwner, Observer {
            frontHomeSleepAdapter.notifyDataSetChanged()
            frontHomeSleepAdapter.submitList(it)

        })


        val linearLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(requireContext(), VERTICAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = frontHomeSleepAdapter
        }


        binding.addOSA.setOnClickListener {
            startActivity(Intent(requireActivity(),UserScreenAddActivity::class.java))
        }

        binding.addPSQ.setOnClickListener {
            startActivity(Intent(requireActivity(),UserScreenAdd2Activity::class.java))
        }

        frontHomeSleepAdapter.setOnItemDetailClickListener(object :FrontHomeSleepAdapter.OnItemDetailClickListener{
            override fun onItemDetailClick(url: String,fileName:String) {
                val intent = Intent(requireActivity(),ReportInfoActivity::class.java)
                intent.putExtra("url",url)
                intent.putExtra("fileName",fileName)
                startActivity(intent)
            }

        })

        binding.homeAddUser.setOnClickListener {
            startActivityForResult(Intent(requireActivity(),HomeAddUserActivity::class.java),1)
        }


        binding.homeBindDevice.setOnClickListener {
            startActivityForResult(Intent(requireActivity(),HomeBindDeviceActivity::class.java),2)
        }


        val homeUserSearchAdapter = HomeUserSearchAdapter(homeViewModel,requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.homeUserSearchRecyclerView.apply {
            adapter = homeUserSearchAdapter
            layoutManager = linearLayout
        }

        val linearParams = binding.searchCard.getLayoutParams()
        homeViewModel.homeUserSearchListLiveData.observe(requireActivity(), Observer {
            homeUserSearchAdapter.notifyDataSetChanged()
            homeUserSearchAdapter.submitList(it)

            if(it.size == 1){
                linearParams.height = dip2px(requireContext(),136f)
                binding.searchCard.layoutParams = linearParams
            }else if (it.size == 2) {
                linearParams.height = dip2px(requireContext(),204f)
                binding.searchCard.layoutParams = linearParams
            }else {
                linearParams.height = dip2px(requireContext(),230f)
                binding.searchCard.layoutParams = linearParams
            }

            if (homeViewModel.needToScrollToTopUserSearch) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0, 0)
                }

                homeViewModel.needToScrollToTopUserSearch = false
            }

        })

        //下滑刷新重新请求列表
        binding.homeUserSearchRefresh.setOnRefreshListener {

            homeViewModel.resetHomeUserSearchQuery()
        }
        homeViewModel.homeUserSearchNetWorkStatus.observe(requireActivity(), Observer {

            if (it == HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                homeViewModel.needToScrollToTopUserSearch = true
            }
            homeUserSearchAdapter.updateNetWorkStatus(it)
            binding.homeUserSearchRefresh.isRefreshing =
                it == HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_INITIAL_LOADING
        })

        binding.homeRepostListSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    binding.cancelSearch.visibility = View.GONE
                    binding.searchCard.visibility = View.GONE
                    shp.saveToSp("homeusersearchkeyword","")
                }else {
                    binding.cancelSearch.visibility = View.VISIBLE
                    binding.searchCard.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        delay(200)
                        shp.saveToSp("homeusersearchkeyword",s.toString())
                        delay(200)
                        homeUserSearchAdapter.notifyDataSetChanged()
                        homeViewModel.resetHomeUserSearchQuery()

                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.cancelSearch.setOnClickListener {
            binding.homeRepostListSearch.setText("")

        }

        binding.homeRepostListSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code

                shp.saveToSp(
                    "homeusersearchkeyword",
                    binding.homeRepostListSearch.text.toString().trim()
                )
                homeUserSearchAdapter.notifyDataSetChanged()
                homeViewModel.resetHomeUserSearchQuery()
                HideKeyboard.hideKeyboard(v,requireContext())
            }
            false
        }

        homeUserSearchAdapter.setOnItemClickListener(object :HomeUserSearchAdapter.OnItemClickListener{
            override fun onItemClick(name: String,itemView:View) {
                userViewModel.keyword.value = name
                HideKeyboard.hideKeyboard(itemView,requireContext())
                binding.homeRepostListSearch.setText("")
            }

        })
    }


    fun initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图

        customView = layoutInflater.inflate(
            R.layout.layout_home_user_search,
            null, false
        )
        customView?.systemUiVisibility = HideUI(requireActivity()).uiOptions()
        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindow = PopupWindow(customView, dip2px(requireContext(), 750f), dip2px(requireContext(), 230f))




        popupwindow?.setOutsideTouchable(true)
        popupwindow?.setFocusable(true)
        popupwindow?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        customView?.setOnTouchListener(View.OnTouchListener { v, event ->
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
            true
        })

    }


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1 -> {
                if(resultCode == Activity.RESULT_OK){
                    homeViewModel.refreshHome.value = 1
                    userViewModel.frontUserRefresh.value = 1

                    deviceViewModel.frontDeviceRefresh.value = 1
                    evaluateViewModel.frontEvaluateRefresh.value = 1
                }
            }
            2 -> {

                deviceViewModel.frontDeviceRefresh.value = 1

            }
            else -> {
                Log.d("HomeFragment", "onActivityResult: 没刷新")
            }
        }
    }
    fun initSleepReport(retrofit:RetrofitSingleton,hospitalid: Int){

        retrofit.api().getFrontHomeSleepReport(5,hospitalid).enqueue(object :Callback<DataClassFrontHome>{
            override fun onResponse(
                call: Call<DataClassFrontHome>,
                response: Response<DataClassFrontHome>
            ) {
                Log.d("HomeFragment", "onResponse:${response.body()} ")


                if (response.body()?.code == 0){
                    val list = response.body()?.data?.data

                    frontHomeViewModel.frontHomeXList.value = list
//                    frontHomeSleepAdapter.submitList(list)
//                    frontHomeViewModel.frontHomeXList.value = list

                }
//                else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                    if (tokenDialog == null) {
//                        tokenDialog = TokenDialog(requireContext(), object : TokenDialog.ConfirmAction {
//                            override fun onRightClick() {
//                                shp.saveToSp("token", "")
//                                shp.saveToSp("uid", "")
//
//                                startActivity(
//                                    Intent(requireContext(),
//                                        LoginActivity::class.java)
//                                )
//                                ActivityCollector2.finishAll()
//                            }
//
//                        })
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    } else {
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    }
//                }


            }

            override fun onFailure(call: Call<DataClassFrontHome>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"主页报告列表网络请求失败")
            }

        })
    }

    fun getUserInfo(retrofit:RetrofitSingleton){
        retrofit.api().getUserInfo("/v1/auser/${Shp(requireContext()).getUid()}").enqueue(object :Callback<DataClassUserInfo>{
            override fun onResponse(
                call: Call<DataClassUserInfo>,
                response: Response<DataClassUserInfo>
            ) {

                if (response.body()?.code == 0){

                    response.body()?.data?.hospitalid?.let {
                        Shp(requireContext()).saveToSpInt("hospitalid",
                            it
                        )
                    }


                    binding.homeHospitalName.text = response.body()?.data?.hospitalname
                    binding.homeUserName.text = response.body()?.data?.truename
                    response.body()?.data?.truename?.let { shp.saveToSp("doctorname", it) }
                    binding.title.text = shp.getWelcome()
                    response.body()?.data?.mobile?.let { shp.saveToSp("usermobile", it) }
                    response.body()?.data?.hospitalname?.let { shp.saveToSp("hospitalname", it) }
                    response.body()?.data?.truename?.let { shp.saveToSp("username", it) }
                    //类型 1-管理员2-呼吸教练3-全科医师 4-医院领导 5-机构管理员 7-运营专员 8-机构操作员
                   when(response.body()?.data?.type){
                       1 -> binding.homeUserIdentity.text = "管理员"
                       2 -> binding.homeUserIdentity.text = "呼吸教练"
                       3 -> binding.homeUserIdentity.text = "全科医师"
                       4 -> binding.homeUserIdentity.text = "医院领导"
                       5 -> binding.homeUserIdentity.text = "机构管理员"
                       7 -> binding.homeUserIdentity.text = "运营专员"
                       8 -> binding.homeUserIdentity.text = "机构操作员"
                   }


                    /**
                     * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
                     */
                    Glide.with(requireContext()).load(response.body()?.data?.hospitalImage)
                        .error(R.mipmap.home_user_head_icon) //异常时候显示的图片
                        .fallback(R.mipmap.home_user_head_icon) //url为空的时候,显示的图片
//                        .placeholder(R.mipmap.home_user_head_icon) //加载成功前显示的图片
                        .into(binding.hospitalHead)

                    shp.getHospitalId()?.let { initSleepReport(retrofit, it) }
                    shp.getHospitalId()?.let { getCalculate(retrofit, it) }
                }
//                else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                    if (tokenDialog == null) {
//                        tokenDialog = TokenDialog(requireContext(), object : TokenDialog.ConfirmAction {
//                            override fun onRightClick() {
//                                shp.saveToSp("token", "")
//                                shp.saveToSp("uid", "")
//
//                                startActivity(
//                                    Intent(requireContext(),
//                                        LoginActivity::class.java)
//                                )
//                                ActivityCollector2.finishAll()
//                            }
//
//                        })
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    } else {
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    }
//                }

            }

            override fun onFailure(call: Call<DataClassUserInfo>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"主页获取用户详情网络请求失败")
            }

        })
    }

    fun getCalculate(retrofit: RetrofitSingleton,hospitalid:Int){
        retrofit.api().getFrontHomeCalculate(hospitalid).enqueue(object :Callback<DataClassFrontHomeCalculate>{
            override fun onResponse(
                call: Call<DataClassFrontHomeCalculate>,
                response: Response<DataClassFrontHomeCalculate>
            ) {


                if (response.body()?.code == 0){

                    binding.homeStatisticalReport.text = "全部报告${response.body()?.data?.totalReportNum}，今日收到${response.body()?.data?.todayReportNum}"
                    binding.homeDeviceCount.text = "${response.body()?.data?.total}台"
                    binding.homeDeviceUseRate.text = "使用率${response.body()?.data?.useRate}%"
                    binding.homeUserCount.text = "${response.body()?.data?.totalUserNum}人"
                    binding.homeAddUserCountToday.text = "今日新增${response.body()?.data?.todayUserNum}人"
                    binding.homeScreeningCount.text = "${response.body()?.data?.totalAssNum}人次"
                    binding.homeAddScreeningUserCountToday.text = "今日新增${response.body()?.data?.todayAssNum}人次"
                    binding.homeRandomVisitPersonCount.text = "${response.body()?.data?.totalFollowupUser}人"
                    binding.homeRandomVisitAllCount.text = "随访${response.body()?.data?.totalFollowupNum}次"

                }
//                else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
//                    if (tokenDialog == null) {
//                        tokenDialog = TokenDialog(requireContext(), object : TokenDialog.ConfirmAction {
//                            override fun onRightClick() {
//                                shp.saveToSp("token", "")
//                                shp.saveToSp("uid", "")
//
//                                startActivity(
//                                    Intent(requireContext(),
//                                        LoginActivity::class.java)
//                                )
//                                ActivityCollector2.finishAll()
//                            }
//
//                        })
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    } else {
//                        tokenDialog!!.show()
//                        tokenDialog?.setCanceledOnTouchOutside(false)
//                    }
//                }
            }

            override fun onFailure(call: Call<DataClassFrontHomeCalculate>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"主页设备记录网络请求失败")
            }

        })
    }

}