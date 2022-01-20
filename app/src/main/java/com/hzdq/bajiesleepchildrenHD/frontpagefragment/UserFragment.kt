package com.hzdq.bajiesleepchildrenHD.frontpagefragment


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassUserDetailInfo
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateDetailActivity
import com.hzdq.bajiesleepchildrenHD.home.activities.ReportInfoActivity
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.activity.AddRandomVisitActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.activity.CheckFollowUpDetailActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.activities.*
import com.hzdq.bajiesleepchildrenHD.user.adapter.*
import com.hzdq.bajiesleepchildrenHD.user.fragment.ChartFragment
import com.hzdq.bajiesleepchildrenHD.user.paging.*
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.FrontUserViewModel
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * 用户fragment
 */
const val RANDOMVISIT_REQUESTCODE = 7
const val ADD_SCREEN_REQUEST_CODE = 8
const val ADD_REPORT_REQUEST_CODE = 9
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private var tokenDialog:TokenDialog? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var deviceViewModel:DeviceViewModel
    private lateinit var frontUserViewModel: FrontUserViewModel
    private var charFragment:ChartFragment ? = null
    private val chartLists = ArrayList<Int>()
    private var mBarChart:BarChart ? = null
    private var list: MutableList<BarEntry> = ArrayList()
    private var reportbarpostion = 0
    private lateinit var shp: Shp

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)


        return binding.root
//        return View.inflate(requireContext(),R.layout.fragment_user,null)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val chartList = listOf<Int>(15,12,54,32,55,78,12,46,46,86,15,12,54,32,55,78,12,46,46,86)

        shp = Shp(requireContext())

//        navController = Navigation.findNavController(requireActivity(),R.id.chartfragment);
//
        shp.saveToSp("frontuserkeyword","")


        frontUserViewModel = ViewModelProvider(requireActivity()).get(FrontUserViewModel::class.java)
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)

        userViewModel = ViewModelProvider(requireActivity()).get(
            UserViewModel::class.java)

        val retrofit = RetrofitSingleton.getInstance(requireContext())

        getTotalCount(retrofit,"")
        userViewModel.needToScrollToTop = true
        val map = mapOf(
            1 to binding.userBarInfo,
            2 to binding.userBarRecord,
            3 to binding.userBarQuestion,
            4 to binding.userBarEvaluate,
            5 to binding.userBarReport,
        )

        val mapLayout = mapOf(
            1 to binding.userCardInfo,
            2 to binding.userCardRecord,
            3 to binding.userCardQuestion,
            4 to binding.userCardEvaluate,
            5 to binding.userCardReport,
        )




        binding.userBarInfo.setOnClickListener { userViewModel.userBarPosition.value = 1 }
        binding.userBarRecord.setOnClickListener { userViewModel.userBarPosition.value = 2 }
        binding.userBarQuestion.setOnClickListener { userViewModel.userBarPosition.value = 3 }
        binding.userBarEvaluate.setOnClickListener { userViewModel.userBarPosition.value = 4 }
        binding.userBarReport.setOnClickListener { userViewModel.userBarPosition.value = 5 }




//         val factory = FrontUserDataSourceFactory(requireActivity().application,"") //paging的工厂类

        val frontUserAdapter = FrontUserAdapter(userViewModel,requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.userFragmentRecyclerView.apply {

            adapter = frontUserAdapter
            layoutManager = linearLayout

        }


        userViewModel.frontUserListLiveData.observe(viewLifecycleOwner, Observer {


            lifecycleScope.launch {
                delay(50)
                frontUserAdapter.notifyDataSetChanged()
                frontUserAdapter.submitList(it)

                getTotalCount(retrofit,shp.getFrontUserKeyWord()!!)
                if (userViewModel.needToScrollToTop){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                    lifecycleScope.launch {
                        delay(50)
                        linearLayout.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                    }

                    userViewModel.needToScrollToTop = false
                }

                if (it.size == 0){
                    userViewModel.listSize.value = 0
                }else {
                    userViewModel.listSize.value = 1
                }
            }



        })

        frontUserAdapter.setOnItemClickListener(object :FrontUserAdapter.OnItemClickListener{
            override fun onItemClick(dateItem: DataFrontUserX) {
                userViewModel.firstItemPosition.value = linearLayout.findFirstVisibleItemPosition()
                Log.d("sinory", "onTick:${linearLayout.findFirstVisibleItemPosition()} ")
            }

        })



//        deviceViewModel.needToScrollToTop

        //下滑刷新重新请求列表
        binding.frontUserSwiperefreshLayout.setOnRefreshListener {
            userViewModel.frontUserRefresh.value = 1

        }

        userViewModel.frontUserNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == FrontUserNetWorkStatus.FRONT_USER_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0,0)
                userViewModel.needToScrollToTop = true
            }
            frontUserAdapter.updateNetWorkStatus(it)
            binding.frontUserSwiperefreshLayout.isRefreshing = it == FrontUserNetWorkStatus.FRONT_USER_INITIAL_LOADING
        })



        binding.userUserListSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                frontUserViewModel.setkeyword(binding.userUserListSearch.text.toString().trim())
                shp.saveToSp("frontuserkeyword",binding.userUserListSearch.text.toString().trim())
                userViewModel.frontUserRefresh.value = 1
                hideKeyboard(v,requireContext())
                getTotalCount(retrofit,binding.userUserListSearch.text.toString().trim())
            }
            false
        }

        userViewModel.totalCount.observe(viewLifecycleOwner, Observer {
            binding.userUserCount.text = "共找到${it}位用户"
        })


        binding.cancelSearch.setOnClickListener {
            hideKeyboard(it,requireContext())
            binding.userUserListSearch.setText("")
            userViewModel.frontUserRefresh.value = 1
            userViewModel.keyword.value = ""
            getTotalCount(retrofit,shp.getFrontUserKeyWord()!!)
        }



        binding.userUserListSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    shp.saveToSp("frontuserkeyword",s.toString())
//                    frontUserAdapter.notifyDataSetChanged()
//                    userViewModel.resetFrontUserQuery()
                    binding.cancelSearch.visibility = View.GONE
                }else {
                    binding.cancelSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        //刷新列表
        userViewModel.frontUserRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    frontUserAdapter.notifyDataSetChanged()
                    userViewModel.resetFrontUserQuery()
                    userViewModel.frontUserPosition.value = 0
                    getTotalCount(retrofit,shp.getFrontUserKeyWord()!!)
                }
                else -> {
                    Log.d("frontUserRefresh", "不刷新 ")
                }
            }
        })



        userViewModel.keyword.observe(requireActivity(), Observer {
            if (!it.equals("")){
                binding.userUserListSearch.setText(it)
                shp.saveToSp("frontuserkeyword",binding.userUserListSearch.text.toString().trim())
                userViewModel.frontUserRefresh.value = 1
                getTotalCount(retrofit,binding.userUserListSearch.text.toString().trim())
            }
        })

        //baseInfoCard

        val baseInfoMore = view.findViewById<LinearLayout>(R.id.user_base_info_more)
        val baseInfoName = view.findViewById<TextView>(R.id.user_base_info_name)
        val baseInfoSex = view.findViewById<TextView>(R.id.user_base_info_sex)
        val baseInfoAge = view.findViewById<TextView>(R.id.user_base_info_age)
        val baseInfoEdit = view.findViewById<TextView>(R.id.user_base_info_edit_info)
        val baseResult = view.findViewById<TextView>(R.id.user_base_info_follow_up_evaluate)
        val baseNextFollow = view.findViewById<TextView>(R.id.user_base_info_next_follow_up)
        val baseNeedFollow = view.findViewById<TextView>(R.id.user_base_info_follow_up_management)
        val baseOahiRes = view.findViewById<TextView>(R.id.user_base_info_OSAS)
        val baseCheckDetail = view.findViewById<TextView>(R.id.user_base_info_detail)

        baseCheckDetail.setOnClickListener {
            if (userViewModel.baseEstimatId.value != 0){
                val intent  = Intent(requireActivity(),EvaluateDetailActivity::class.java)
                intent.putExtra("id",userViewModel.baseEstimatId.value)
                startActivity(intent)
            }
        }



        var uid = 0
        var hospitalid = 0
        var mzh = ""
        var name = ""
        var sex = 0
        var age = 0
        var mobile =""
        var height = ""
        var weight = ""
        var needfollow = -1
        userViewModel.dateItem.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Log.d("sinory", "onViewCreated:$it ")
                mzh = it.mzh
                name = it.truename
                sex = it.sex.toInt()
                age = it.age.toInt()
                mobile = it.mobile
                height = it.height
                weight = it.weight
//                needfollow = it.needfollow

//                if (it.sex.equals("0")){
//                    baseInfoSex.text = "未知"
//                }else if (it.sex.equals("1")){
//                    baseInfoSex.text = "男"
//                }else if (it.sex.equals("2")){
//                    baseInfoSex.text = "女"
//                }

//                baseInfoAge.text = "${ it.age}岁"
//                uid = it.uid
                hospitalid = it.hospitalid

//                //柱形图
//                val chartList = listOf<Float>(15.34F,12.25f,54.23f,32.33f,55.444f,78.2f,12.123f)
//                charFragment = null
//                charFragment = ChartFragment(chartList)
//                requireActivity().supportFragmentManager.beginTransaction().add(R.id.chartLayout, charFragment!!).show(charFragment!!).commit()
            }
        })
        userViewModel.uid.observe(viewLifecycleOwner, Observer {



//            Log.d("retryUserQuestionList", "onViewCreated: ${shp.getUserQuestionId()}")
            Log.d("retryUserQuestionList", "onViewCreated: ${it}")

            //获取用户详情
            if (it != 0){
                shp.saveToSpInt("treatrecordpatientid",it)
                shp.saveToSpInt("userquestionsid",it)

                val maps = HashMap<String, String>()
                maps["hospitalid"] = shp.getHospitalId().toString()
//                maps["uid"] = "$it"
                maps["uid"] = it.toString()
                maps["oahi"] = "1"
                //请求用户详情
                val url  = OkhttpSingleton.BASE_URL+"/v2/user/info"
                postUserDetailInfo(url,maps)
            }else {
//                userViewModel.retryUserQuestionList()
            }

        })


        userViewModel.baseName.observe(viewLifecycleOwner, Observer {

            if (it.equals("")){
                baseInfoName.text = "--"
            }else{

                baseInfoName.text = it




            }
        })
        userViewModel.baseSex.observe(viewLifecycleOwner, Observer {
            when(it){
                1 -> baseInfoSex.text = "男"
                2 -> baseInfoSex.text = "女"
                else -> baseInfoSex.text = ""
            }
        })
        userViewModel.baseEstimatres.observe(viewLifecycleOwner, Observer {
            if (it.equals("")){
                baseResult.text = "--"
                baseCheckDetail.visibility = View.GONE
            }else {
                baseResult.text = it
                baseCheckDetail.visibility = View.VISIBLE
            }
        })
        userViewModel.baseFollowtime.observe(viewLifecycleOwner, Observer {
            if (it == 0){
                baseNextFollow.text = "--"
            }else {
                baseNextFollow.text = "${timestamp2Date(it.toString(),"yyyy-MM-dd")}"
            }
        })



//        userViewModel.baseOahi.observe(viewLifecycleOwner, Observer {
//
//        })
        userViewModel.baseOahires.observe(viewLifecycleOwner, Observer {
            if (it.equals("")){
                baseOahiRes.text = "--"
            }else {
                baseOahiRes.text = it
                when(it){
                    "未评估" -> {
                        baseOahiRes.setTextColor(Color.parseColor("#96ADDF"))
//                        baseCheckDetail.visibility = View.GONE
                    }
                    "正常" -> {
//                        baseCheckDetail.visibility = View.VISIBLE
                        baseOahiRes.setTextColor(Color.parseColor("#6CC291"))
                    }
                    "轻度" -> {
                        baseOahiRes.setTextColor(Color.parseColor("#596AFD"))
//                        baseCheckDetail.visibility = View.VISIBLE
                    }
                    "中度" ->{
                        baseOahiRes.setTextColor(Color.parseColor("#F39920"))
//                        baseCheckDetail.visibility = View.VISIBLE
                    }
                    "重度" -> {
                        baseOahiRes.setTextColor(Color.parseColor("#F45C50"))
//                        baseCheckDetail.visibility = View.VISIBLE
                    }
                }
            }

        })
        userViewModel.baseAge.observe(viewLifecycleOwner, Observer {
            baseInfoAge.text = "${it}岁"
        })
//        charFragment = ChartFragment(userViewModel,viewLifecycleOwner)
        userViewModel.baseOahiList.observe(viewLifecycleOwner, Observer {



            if (charFragment != null){

                requireActivity().supportFragmentManager.beginTransaction().remove(charFragment!!).commit()
                charFragment = null
                charFragment = ChartFragment(it)
                requireActivity().supportFragmentManager.beginTransaction().add(R.id.chartLayout, charFragment!!).show(charFragment!!).commit()
            }else {


                charFragment = ChartFragment(it)

                requireActivity().supportFragmentManager.beginTransaction().add(R.id.chartLayout, charFragment!!).show(charFragment!!).commit()
            }
//


        })

        //编辑用户信息
        baseInfoEdit.setOnClickListener {
            val intent = Intent(requireActivity(),UpdateUserActivity::class.java)
            intent.putExtra("mzh",mzh)
            intent.putExtra("name",userViewModel.baseName.value)
            intent.putExtra("sex",userViewModel.baseSex.value)
            intent.putExtra("age",userViewModel.baseAge.value)
            intent.putExtra("mobile",userViewModel.baseMobile.value)
            intent.putExtra("height",userViewModel.baseHeight.value)
            intent.putExtra("weight",userViewModel.baseWeight.value)
            intent.putExtra("needfollow",userViewModel.baseNeedfollow.value)
            intent.putExtra("uid",userViewModel.baseUid.value!!.toInt())
            intent.putExtra("hospitalid",hospitalid)
            startActivityForResult(intent,1)
        }


        //recordCard
        val addRecord = view.findViewById<Button>(R.id.record_add_record)
        val recordName = view.findViewById<TextView>(R.id.user_record_name)
        val recordRefresh = view.findViewById<SwipeRefreshLayout>(R.id.user_record_refresh)
        val recordRecyclerView = view.findViewById<RecyclerView>(R.id.user_record_recycler_view)

        addRecord.setOnClickListener {
            val intent = Intent(requireActivity(),AddTreatmentRecordActivity2::class.java)
            intent.putExtra("name",userViewModel.name.value)
            intent.putExtra("patient_id",userViewModel.uid.value)
            startActivityForResult(intent,6)
//            startActivity(Intent(requireActivity(),CheckTreatmentRecords::class.java))
        }




        userViewModel.name.observe(viewLifecycleOwner, Observer {

            recordName.text = it
        })

        val userTreatRecordAdapter = UserTreatRecordAdapter(userViewModel,viewLifecycleOwner)
        val linearLayoutTreatRecord = LinearLayoutManager(requireContext())
        recordRecyclerView.apply {
            adapter = userTreatRecordAdapter
            layoutManager = linearLayoutTreatRecord

        }


        userViewModel.userTreatRecordListLiveData.observe(viewLifecycleOwner, Observer {
            userTreatRecordAdapter.notifyDataSetChanged()
            userTreatRecordAdapter.submitList(it)

            if (userViewModel.needToScrollToTopTreatRecord){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayoutTreatRecord.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                userViewModel.needToScrollToTopTreatRecord = false
            }



        })

        //下滑刷新重新请求列表
        recordRefresh.setOnRefreshListener {
            userViewModel.userTreatRecordRefresh.value = 1

        }

        userViewModel.userTreatRecordNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == UserTreatRecordNetWorkStatus.USER_TREAT_RECORD_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayoutTreatRecord.scrollToPositionWithOffset(0,0)
                userViewModel.needToScrollToTopTreatRecord = true
            }

            userTreatRecordAdapter.updateNetWorkStatus(it)
            recordRefresh.isRefreshing = it == UserTreatRecordNetWorkStatus.USER_TREAT_RECORD_INITIAL_LOADING
        })

        //刷新列表
        userViewModel.userTreatRecordRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    if (userViewModel.listSize.value == 0){
                        lifecycleScope.launch {
                            delay(70)
                            userTreatRecordAdapter.notifyDataSetChanged()
                            userTreatRecordAdapter.submitList(null)
                            userViewModel.userTreatRecordRefresh.value = 0
                        }

                    }else {
                        userTreatRecordAdapter.notifyDataSetChanged()
                        userViewModel.resetUserTreatRecordQuery()
                        userViewModel.userTreatRecordRefresh.value = 0
                    }

                }
                else -> {
                    Log.d("userTreatRecordRefresh", "不刷新 ")
                }
            }
        })

        userTreatRecordAdapter.setOnItemDetailClickListener(object :UserTreatRecordAdapter.OnItemClickListener{
            override fun onItemDetailClick(id: Int) {
                val intent = Intent(requireActivity(),CheckTreatmentRecords::class.java)
                intent.putExtra("name",userViewModel.name.value)
                intent.putExtra("id",id)
                startActivity(intent)
            }

        })



        //随访评估

        val evaluateName = view.findViewById<TextView>(R.id.user_follow_up_name)
        val addFlowUp = view.findViewById<Button>(R.id.user_evaluation_add_follow_up)
        val recyclerViewFlowUp = view.findViewById<RecyclerView>(R.id.user_follow_up_recycler_view)
        val refreshFlowUp = view.findViewById<SwipeRefreshLayout>(R.id.user_follow_up_refresh)

        userViewModel.name.observe(viewLifecycleOwner, Observer {
            evaluateName.text = it
        })

        userViewModel.baseNeedfollow.observe(viewLifecycleOwner, Observer {
            when(it){
                1 -> {
                    baseNeedFollow.text = "是"
                    addFlowUp.visibility = View.VISIBLE
                }
                0 -> {
                    baseNeedFollow.text = "否"
                    addFlowUp.visibility = View.GONE
                }
            }
        })
        addFlowUp.setOnClickListener {
            val intent = Intent(requireActivity(),AddRandomVisitActivity::class.java)
            intent.putExtra("patient_id",userViewModel.uid.value)
            intent.putExtra("name",userViewModel.name.value)
//            intent.putExtra("userList",1)
//            intent.putExtra("height",userViewModel.baseHeight.value)
//            intent.putExtra("weight",userViewModel.baseWeight.value)
            startActivityForResult(intent,RANDOMVISIT_REQUESTCODE)
        }

        val userFollowUpAdapter = UserFollowUpAdapter(userViewModel,viewLifecycleOwner)
        val linearLayoutFollowUp = LinearLayoutManager(requireContext())
        recyclerViewFlowUp.apply {
            adapter = userFollowUpAdapter
            layoutManager = linearLayoutFollowUp

        }




        userViewModel.userFollowUpListLiveData.observe(viewLifecycleOwner, Observer {
            userFollowUpAdapter.notifyDataSetChanged()
            userFollowUpAdapter.submitList(it)

            if (userViewModel.needToScrollToTopFollowUp){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayoutFollowUp.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                userViewModel.needToScrollToTopFollowUp = false
            }

        })

        //下滑刷新重新请求列表
        refreshFlowUp.setOnRefreshListener {
            userViewModel.userFollowUpRefresh.value = 1

        }

        userViewModel.userFollowUpNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == UserFollowUpNetWorkStatus.USER_FOLLOW_UP_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayoutFollowUp.scrollToPositionWithOffset(0,0)
                userViewModel.needToScrollToTopFollowUp = true
            }
            userFollowUpAdapter.updateNetWorkStatus(it)
            refreshFlowUp.isRefreshing = it == UserFollowUpNetWorkStatus.USER_FOLLOW_UP_INITIAL_LOADING
        })

        //刷新列表
        userViewModel.userFollowUpRefresh.observe(requireActivity(), Observer {

            when(it){
                1 -> {
                    if (userViewModel.listSize.value == 0){
                        lifecycleScope.launch {
                            delay(70)
                            userFollowUpAdapter.notifyDataSetChanged()
                            userFollowUpAdapter.submitList(null)
                            userViewModel.userFollowUpRefresh.value = 0
                        }
                    }else {
                        userFollowUpAdapter.notifyDataSetChanged()
                        userViewModel.resetUserFollowUpQuery()
                        userViewModel.userFollowUpRefresh.value = 0
                    }

                }
                else -> {
                    Log.d("userTreatRecordRefresh", "不刷新 ")
                }
            }
        })

        userFollowUpAdapter.setOnItemDetailClickListener(object :UserFollowUpAdapter.OnItemClickListener{
            override fun onItemDetailClick(id: Int) {
                val intent = Intent(requireActivity(),CheckFollowUpDetailActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }

        })

        //问卷结果列表
        val addEvaluation = view.findViewById<Button>(R.id.screen_card_add_evaluation)
        val screenName = view.findViewById<TextView>(R.id.screen_card_name)
        val questionRefresh = view.findViewById<SwipeRefreshLayout>(R.id.user_question_refresh)
        val questionRecyclerView = view.findViewById<RecyclerView>(R.id.user_question_recycler_view)

        addEvaluation.setOnClickListener {
            val intent  = Intent(requireActivity(),UserScreenActivity::class.java)
            intent.putExtra("uid",userViewModel.uid.value)
            intent.putExtra("name",userViewModel.name.value)
            startActivityForResult(intent,ADD_SCREEN_REQUEST_CODE)
        }

        userViewModel.name.observe(viewLifecycleOwner, Observer {
            screenName.text = it
        })


        val userQuestionListAdapter = UserQuestionListAdapter(userViewModel,viewLifecycleOwner)
        val linearLayoutQuestionList = LinearLayoutManager(requireContext())
        questionRecyclerView.apply {
            adapter = userQuestionListAdapter
            layoutManager = linearLayoutQuestionList
        }


        userViewModel.userQuestionListLiveData.observe(viewLifecycleOwner, Observer {
            userQuestionListAdapter.notifyDataSetChanged()
            userQuestionListAdapter.submitList(it)

            if (userViewModel.needToScrollToTopQuestionList){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayoutQuestionList.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                userViewModel.needToScrollToTopQuestionList = false
            }



        })

        //下滑刷新重新请求列表
        questionRefresh.setOnRefreshListener {
            userViewModel.userQuestionListRefresh.value = 1

        }

        userViewModel.userQuestionListNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == UserQuestionListNetWorkStatus.USER_QUESTION_LIST_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayoutQuestionList.scrollToPositionWithOffset(0,0)
                userViewModel.needToScrollToTopQuestionList = true
            }

            userQuestionListAdapter.updateNetWorkStatus(it)
            questionRefresh.isRefreshing = it == UserQuestionListNetWorkStatus.USER_QUESTION_LIST_INITIAL_LOADING

        })

        //刷新列表
        userViewModel.userQuestionListRefresh.observe(requireActivity(), Observer {

            when(it){
                1 -> {
                    if (userViewModel.listSize.value == 0){

                        lifecycleScope.launch {
                            delay(70)
                            userQuestionListAdapter.notifyDataSetChanged()
                            userQuestionListAdapter.submitList(null)
                            userViewModel.userQuestionListRefresh.value = 0
                        }
                    }else {
                        userQuestionListAdapter.notifyDataSetChanged()
                        userViewModel.resetQuestionListQuery()
                        userViewModel.userQuestionListRefresh.value = 0

                    }

                }
                else -> {
                    Log.d("userQuestionListRefresh", "不刷新 ")
                }
            }
        })

        userQuestionListAdapter.setOnItemDetailClickListener(object :UserQuestionListAdapter.OnItemClickListener{
            override fun onItemDetailClick(id: Int,type:Int) {
                when (type){
                    13 -> {
                        val intent = Intent(requireActivity(),CheckPSQ_Activity::class.java)
                        intent.putExtra("taskid",id)
                        startActivity(intent)
                    }
                    14 -> {
                        val intent = Intent(requireActivity(),CheckOSA_Activity::class.java)
                        intent.putExtra("taskid",id)
                        startActivity(intent)
                    }
                }

            }

        })


        //检验报告
        val sleep = view.findViewById<Button>(R.id.user_report_bar_sleep)
        val image =view.findViewById<Button>(R.id.user_report_bar_image)
        val endoscope = view.findViewById<Button>(R.id.user_report_bar_endoscope)
        val biochemistry =view.findViewById<Button>(R.id.user_report_bar_biochemistry)
        val other =view.findViewById<Button>(R.id.user_report_bar_other)
        val update = view.findViewById<Button>(R.id.user_report_bar_upload)

        val userReportRefresh =  view.findViewById<SwipeRefreshLayout>(R.id.user_report_refresh)
        val userReportRecyclerView =  view.findViewById<RecyclerView>(R.id.user_report_recycler_view)

        sleep.setOnClickListener {
            sleep.isSelected = true
            sleep.setTextColor(Color.parseColor("#2051BD"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
            shp.saveToSp("reportbarpostion","1")
            reportbarpostion = 1
            shp.saveToSpInt("userreportsubtype",21)
            userViewModel.userReportListRefresh.value = 1
        }

        image.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = true
            image.setTextColor(Color.parseColor("#2051BD"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
            shp.saveToSp("reportbarpostion","2")
            reportbarpostion = 2
            shp.saveToSpInt("userreportsubtype",22)
            userViewModel.userReportListRefresh.value = 1
        }

        endoscope.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = true
            endoscope.setTextColor(Color.parseColor("#2051BD"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
            shp.saveToSp("reportbarpostion","3")
            reportbarpostion = 3
            shp.saveToSpInt("userreportsubtype",23)
            userViewModel.userReportListRefresh.value = 1
        }

        biochemistry.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = true
            biochemistry.setTextColor(Color.parseColor("#2051BD"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
            shp.saveToSp("reportbarpostion","4")
            reportbarpostion = 4
            shp.saveToSpInt("userreportsubtype",24)
            userViewModel.userReportListRefresh.value = 1
        }

        other.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = true
            other.setTextColor(Color.parseColor("#2051BD"))
            shp.saveToSp("reportbarpostion","5")
            reportbarpostion = 5
            shp.saveToSpInt("userreportsubtype",25)
            userViewModel.userReportListRefresh.value = 1
        }


        sleep.performClick()

        update.setOnClickListener {

            val intent = Intent(requireActivity(),UpdateReportActivity2::class.java)

            intent.putExtra("patient_id",userViewModel.uid.value)
            intent.putExtra("reportbarpostion",reportbarpostion)
            startActivityForResult(intent,ADD_REPORT_REQUEST_CODE)
        }

        val userReportListAdapter = UserReportListAdapter(userViewModel,viewLifecycleOwner)
        val linearLayoutReportList = LinearLayoutManager(requireContext())
        userReportRecyclerView.apply {
            adapter = userReportListAdapter
            layoutManager = linearLayoutReportList

        }


        userViewModel.userReportListLiveData.observe(viewLifecycleOwner, Observer {
            userReportListAdapter.notifyDataSetChanged()
            userReportListAdapter.submitList(it)

            if (userViewModel.needToScrollToTopReportList){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayoutReportList.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                userViewModel.needToScrollToTopReportList = false
            }

        })

        //下滑刷新重新请求列表
        userReportRefresh.setOnRefreshListener {
            userViewModel.userReportListRefresh.value = 1

        }

        userViewModel.userReportListNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == UserReportListNetWorkStatus.USER_REPORT_LIST_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayoutReportList.scrollToPositionWithOffset(0,0)
                userViewModel.needToScrollToTopReportList = true
            }
            userReportListAdapter.updateNetWorkStatus(it)
            userReportRefresh.isRefreshing = it == UserReportListNetWorkStatus.USER_REPORT_LIST_INITIAL_LOADING
        })

        //刷新列表
        userViewModel.userReportListRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    if (userViewModel.listSize.value == 0){
                        lifecycleScope.launch {
                            delay(70)
                            userReportListAdapter.notifyDataSetChanged()
                            userReportListAdapter.submitList(null)
                            userViewModel.userReportListRefresh.value = 0
                        }
                    }else {
                        userReportListAdapter.notifyDataSetChanged()
                        userViewModel.resetReportListQuery()
                        userViewModel.userReportListRefresh.value = 0
                    }

                }
                else -> {
                    Log.d("userTreatRecordRefresh", "不刷新 ")
                }
            }
        })

        userReportListAdapter.setOnItemDetailClickListener(object :UserReportListAdapter.OnItemClickListener{
            override fun onItemDetailClick(id: Int,reportId:String,fileName:String) {
                if (reportId.equals("")){
                    val intent = Intent(requireActivity(),CheckReportActivity::class.java)
                    intent.putExtra("name",userViewModel.name.value)
                    intent.putExtra("id",id)
                    startActivity(intent)
                }else {
                    val intent = Intent(requireActivity(), ReportInfoActivity::class.java)
                    intent.putExtra("url",reportId)
                    intent.putExtra("fileName",fileName)
                    startActivity(intent)
                }
            }

        })
//        userReportListAdapter.setOnItemDetailClickListener(object :UserTreatRecordAdapter.OnItemClickListener{
//            override fun onItemDetailClick(id: Int) {
//                val intent = Intent(requireActivity(),CheckTreatmentRecords::class.java)
//                intent.putExtra("name",userViewModel.name.value)
//                intent.putExtra("id",id)
//                startActivity(intent)
//            }
//
//        })


        baseInfoMore.setOnClickListener {
            userViewModel.userBarPosition.value = 5
            sleep.isSelected = true
            sleep.setTextColor(Color.parseColor("#2051BD"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
            shp.saveToSp("reportbarpostion","1")
            reportbarpostion = 1
            shp.saveToSpInt("userreportsubtype",21)
            userViewModel.userReportListRefresh.value = 1
        }


        userViewModel.listSize.observe(requireActivity(), Observer {
            if (it == 0){
                userViewModel.name.value = "--"
                lifecycleScope.launch {
                    delay(70)
                    //基本信息
                    baseInfoEdit.visibility = View.GONE
                    baseInfoName.text = "--"
                    baseInfoSex.text = "--"
                    baseInfoAge.text = "--"
                    baseResult.text = "--"
                    baseNextFollow.text = "--"
                    baseNeedFollow.text = "--"
                    baseOahiRes.text = "--"
                    baseCheckDetail.visibility = View.GONE

                    if (charFragment != null){
                        requireActivity().supportFragmentManager.beginTransaction().remove(charFragment!!).commit()
                    }
                }

                //诊疗记录
                addRecord.visibility = View.GONE
               userViewModel.userTreatRecordRefresh.value = 1

                //筛查问卷
                addEvaluation.visibility  = View.GONE
                userViewModel.userQuestionListRefresh.value = 1

                //随访记录
                addFlowUp.visibility = View.GONE
                userViewModel.userFollowUpRefresh.value = 1

                //检验报告
                update.visibility = View.GONE
                userViewModel.userReportListRefresh.value = 1
            }else {
                baseInfoEdit.visibility = View.VISIBLE
                baseCheckDetail.visibility = View.VISIBLE
                addRecord.visibility = View.VISIBLE
                addEvaluation.visibility  = View.VISIBLE
                update.visibility = View.VISIBLE
            }
        })



        userViewModel.userBarPosition.observe(viewLifecycleOwner, Observer {

            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#2051BD"))

            }
            mapLayout.forEach {
                it.value.visibility = View.GONE
            }
            when(it){
                1 -> {
                    if (userViewModel.listSize.value== 0 ){
                        lifecycleScope.launch {
                            delay(70)
                            //基本信息
                            baseInfoEdit.visibility = View.GONE
                            baseInfoName.text = "--"
                            baseInfoSex.text = "--"
                            baseInfoAge.text = "--"
                            baseResult.text = "--"
                            baseNextFollow.text = "--"
                            baseNeedFollow.text = "--"
                            baseOahiRes.text = "--"
                            baseCheckDetail.visibility = View.GONE

                            if (charFragment != null){
                                requireActivity().supportFragmentManager.beginTransaction().remove(charFragment!!).commit()
                            }
                            binding.userBarInfo.also {
                                it.isSelected = true
                                it.setTextColor(Color.parseColor("#FFFFFF"))

                            }
                        }
                    }else {
                        binding.userBarInfo.also {
                            it.isSelected = true
                            it.setTextColor(Color.parseColor("#FFFFFF"))
                            val maps = HashMap<String, String>()
                            maps["hospitalid"] = shp.getHospitalId().toString()
//                maps["uid"] = "$it"
                            maps["uid"] = userViewModel.uid.value.toString()
                            maps["oahi"] = "1"
                            //请求用户详情
                            val url  = OkhttpSingleton.BASE_URL+"/v2/user/info"
                            postUserDetailInfo(url,maps)

                        }
                    }

                    binding.userCardInfo.visibility = View.VISIBLE
                }
                2  -> {
                    binding.userBarRecord.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        userViewModel.userTreatRecordRefresh.value = 1
                    }
                    binding.userCardRecord.visibility = View.VISIBLE
                }

                3 -> {
                    binding.userBarQuestion.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        userViewModel.userQuestionListRefresh.value = 1
                    }
                    binding.userCardQuestion.visibility = View.VISIBLE
                }
                4 -> {
                    binding.userBarEvaluate.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        userViewModel.userFollowUpRefresh.value = 1

                    }
                    binding.userCardEvaluate.visibility = View.VISIBLE
                }
                5 -> {
                    binding.userBarReport.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        userViewModel.userReportListRefresh.value = 1
                    }
                    binding.userCardReport.visibility = View.VISIBLE
                }
                else ->{
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#2051BD"))
                    }
                    mapLayout.forEach {
                        it.value.visibility = View.GONE
                    }
                }
            }
        })

        userViewModel.baseUid.observe(requireActivity(), Observer {
            recordRefresh.isEnabled = !it.equals("")
            refreshFlowUp.isEnabled = !it.equals("")
            questionRefresh.isEnabled = !it.equals("")
            userReportRefresh.isEnabled = !it.equals("")
        })
        setBaseInfoCard(view)
        setRecordCard(view)
        setReportCard(view)
        setEvaluateCard(view)
        setQuestionCard(view)

    }


    fun postUserDetailInfo(url:String,map: HashMap<String, String>) {
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



                val gson = Gson()
                val dataClassUserDetailInfo= gson.fromJson(res, DataClassUserDetailInfo::class.java)


//                val dataClassUserDetailInfo = DataClassUserDetailInfo()
//                val dataUserDetailInfo = DataUserDetailInfo()
//                val userinfoUserDetailInfo = UserinfoUserDetailInfo()
//                val oahiList :MutableList<OahiUserDetailInfo> = ArrayList()
//                try {
//                    val jsonObject = JSONObject(res)
//                    //第一层解析
//                    val code = jsonObject.optInt("code")
//                    val msg = jsonObject.optString("msg")
//                    val data = jsonObject.optJSONObject("data")
//
//                    //第一层封装
//                    dataClassUserDetailInfo.code = code
//                    dataClassUserDetailInfo.msg = msg
//                    //                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();
//
//                    //第二层解析
//                    if (data != null) {
//                        val userinfo = jsonObject.optJSONObject("userinfo")
//                        val oahi = jsonObject.getJSONArray("oahi")
//
//                        for (i in 0 until oahi.length()) {
//                            val jsonObject1 = oahi.getJSONObject(i)
//                            if (jsonObject1 != null) {
//                                val oahi = jsonObject1.optString("oahi")
//                                val create_time = jsonObject1.optInt("create_time")
//                                //第二层封装
//                                val oahiUserDetailInfo = OahiUserDetailInfo()
//                                oahiUserDetailInfo.oahi = oahi
//                                oahiUserDetailInfo.createTime = create_time
//
//                                oahiList.add(oahiUserDetailInfo)
//                            }
//                        }
//
//
//                        if (userinfo != null){
//                            val uid = userinfo.optInt("uid")
//                            val truename = userinfo.optString("truename")
//                            val age = userinfo.optInt("age")
//                            val height = userinfo.optString("height")
//                            val weight = userinfo.optString("weight")
//                            val sex = userinfo.optInt("sex")
//                            val mobile = userinfo.optString("mobile")
//                            val needfollow = userinfo.optInt("needfollow")
//                            val oahi = userinfo.optString("oahi")
//                            val follow_time = userinfo.optInt("oahiRes")
//                            val oahiRes = userinfo.optString("oahiRes")
//                            val estimate_res = userinfo.optString("estimate_res")
//
//                            userinfoUserDetailInfo.uid = uid
//                            userinfoUserDetailInfo.truename = truename
//                            userinfoUserDetailInfo.age = age
//                            userinfoUserDetailInfo.height = height
//                            userinfoUserDetailInfo.weight = weight
//                            userinfoUserDetailInfo.sex = sex
//                            userinfoUserDetailInfo.mobile = mobile
//                            userinfoUserDetailInfo.needfollow = needfollow
//                            userinfoUserDetailInfo.followTime = follow_time
//                            userinfoUserDetailInfo.oahiRes = oahiRes
//                            userinfoUserDetailInfo.estimateRes = estimate_res
//                            userinfoUserDetailInfo.oahi = oahi
//                        }
//
//                        dataUserDetailInfo.oahi = oahiList
//                        dataUserDetailInfo.userinfo = userinfoUserDetailInfo
//
//                        dataClassUserDetailInfo.data = dataUserDetailInfo
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
                requireActivity().runOnUiThread {
                    if (dataClassUserDetailInfo != null){
                        if (dataClassUserDetailInfo.code == 0) {
                            userViewModel.baseName.value = dataClassUserDetailInfo.data.userinfo.truename
                            userViewModel.baseAge.value = dataClassUserDetailInfo.data.userinfo.age
                            userViewModel.baseEstimatres.value = dataClassUserDetailInfo.data.userinfo.estimateRes
                            userViewModel.baseFollowtime.value = dataClassUserDetailInfo.data.userinfo.followTime
                            userViewModel.baseNeedfollow.value = dataClassUserDetailInfo.data.userinfo.needfollow

                            userViewModel.baseOahi.value = dataClassUserDetailInfo.data.userinfo.oahi
                            userViewModel.baseOahires.value = dataClassUserDetailInfo.data.userinfo.oahiRes
                            userViewModel.baseSex.value = dataClassUserDetailInfo.data.userinfo.sex
                            userViewModel.baseEstimatId.value = dataClassUserDetailInfo.data.userinfo.estimateId


                            userViewModel.baseMzh.value = dataClassUserDetailInfo.data.userinfo.mzh
                            userViewModel.baseMobile.value = dataClassUserDetailInfo.data.userinfo.mobile
                            userViewModel.baseHeight.value = dataClassUserDetailInfo.data.userinfo.height
                            userViewModel.baseWeight.value = dataClassUserDetailInfo.data.userinfo.weight
                            userViewModel.baseUid.value = dataClassUserDetailInfo.data.userinfo.uid.toString()



                            val list = dataClassUserDetailInfo.data.oahi
                            val list1 :MutableList<Float>  = ArrayList()
                            for ( i in 0..list.size-1){
//                               list1.add(list[i].oahi.toFloat())

                                if (list[i].oahi.equals("")){
                                    list1.add(0f)
                                }else {
                                    list1.add(list[i].oahi.toFloat())
                                }

                            }


                            userViewModel.baseOahiList.value = list1


                        }else if ( dataClassUserDetailInfo.code == 10010 ||  dataClassUserDetailInfo.code == 10004) {
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
                                "${dataClassUserDetailInfo.msg}"
                            )
                        }
                    }else {

                    }

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val maps = HashMap<String, String>()
                    maps["hospitalid"] = shp.getHospitalId().toString()
//                maps["uid"] = "$it"
                    maps["uid"] = userViewModel.uid.value.toString()
                    maps["oahi"] = "1"


                    //请求用户详情
                    val url  = OkhttpSingleton.BASE_URL+"/v2/user/info"
                    postUserDetailInfo(url,maps)
                    userViewModel.frontUserRefresh.value = 1
//                    deviceViewModel.frontDeviceRefresh.value = 1
                }

            }
            6 -> {
                if (resultCode == RESULT_OK) {

                    userViewModel.userTreatRecordRefresh.value = 1
                    userViewModel.frontUserRefresh.value = 1
                }
            }
            RANDOMVISIT_REQUESTCODE -> {
                if (resultCode == RESULT_OK) {
                    userViewModel.userFollowUpRefresh.value = 1
                    userViewModel.frontUserRefresh.value = 1
                }

            }
            ADD_SCREEN_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    userViewModel.userQuestionListRefresh.value = 1
                    userViewModel.frontUserRefresh.value = 1
                }

            }
            ADD_REPORT_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    userViewModel.userReportListRefresh.value = 1
                    userViewModel.frontUserRefresh.value = 1
                }

            }
            else -> {
                Log.d("UserFragment", "onActivityResult:没刷新 ")
            }
        }
    }


    fun hideKeyboard(view: View,context: Context) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



    fun getTotalCount(retrofit: RetrofitSingleton, kewords:String){


        retrofit.api().getFrontUserList(kewords,shp.getHospitalId()!!,1,1,0).enqueue(object :Callback<DataClassFrontUser>{
            override fun onResponse(
                call: Call<DataClassFrontUser>,
                response: Response<DataClassFrontUser>
            ) {
                if (response.body()?.code == 0){
                    userViewModel.totalCount.value = response.body()?.data?.total

                    Log.d("sinoryTotal", "onResponse:${response.body()?.data?.total}")
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

            override fun onFailure(call: Call<DataClassFrontUser>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"获取用户总数网络请求失败")
            }

        })
    }


    private fun setBaseInfoCard(view: View){



    }



    private fun setRecordCard(view: View){

    }

    private fun setQuestionCard(view: View){

    }

    private fun setEvaluateCard(view: View){

    }

    private fun setReportCard(view: View){


    }



}





