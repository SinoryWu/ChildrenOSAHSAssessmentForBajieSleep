package com.hzdq.bajiesleepchildrenHD.frontpagefragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentRandomVisitBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontFollowUp
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.activity.AddRandomVisitActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.activity.CheckFollowUpDetailActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.adapter.FollowUpAdapter
import com.hzdq.bajiesleepchildrenHD.randomvisit.adapter.FrontFollowUpAdapter
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FrontFollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.adapter.UserFollowUpAdapter
import com.hzdq.bajiesleepchildrenHD.user.paging.UserFollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideKeyboard
import com.hzdq.bajiesleepchildrenHD.utils.HideKeyboard.hideKeyboard
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 随访fragment
 */

const val ADD_FOLLOW_UP_REQUEST_CODE = 8
class RandomVisitFragment : Fragment() {
    private lateinit var binding: FragmentRandomVisitBinding
    private lateinit var randomViewModel: RandomViewModel
    private lateinit var shp:Shp
    private lateinit var retrofitSingleton: RetrofitSingleton
    private var tokenDialog:TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_random_visit,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        randomViewModel = ViewModelProvider(requireActivity()).get(RandomViewModel::class.java)
        shp = Shp(requireContext())
        shp.saveToSp("frontfollowupkeyword","")

        randomViewModel.followUpNumber.observe(viewLifecycleOwner, Observer {
            binding.selectFollowUp.text = "本月应访${it}人"
        })

        randomViewModel.allNumber.observe(viewLifecycleOwner, Observer {
            binding.selectAll.text = "全部${it}人"
        })
        getFrontFollowUpNumber(retrofitSingleton,"")

        val frontFollowUpAdapter = FrontFollowUpAdapter(randomViewModel,requireActivity())
        val linearLayout1 = LinearLayoutManager(requireContext())
        binding.frontFollowUpRecyclerView.apply {
            adapter = frontFollowUpAdapter
            layoutManager = linearLayout1

        }


        randomViewModel.frontFollowUpListLiveData.observe(viewLifecycleOwner, Observer {
            frontFollowUpAdapter.notifyDataSetChanged()
            frontFollowUpAdapter.submitList(it)

            if (randomViewModel.needToScrollToTopFrontFollowUp){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout1.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                randomViewModel.needToScrollToTopFrontFollowUp = false
            }



            if (it.size == 0){
                randomViewModel.listSize.value = 0
            }else {
                randomViewModel.listSize.value = 1
            }


        })


//        deviceViewModel.needToScrollToTop

        //下滑刷新重新请求列表
        binding.frontFollowUpSwiperefresh.setOnRefreshListener {
            randomViewModel.frontFollowUpRefresh.value = 1

        }

        randomViewModel.frontFollowUpNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == FrontFollowUpNetWorkStatus.FRONT_FOLLOW_UP_INITIAL_LOADING){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout1.scrollToPositionWithOffset(0,0)
                randomViewModel.needToScrollToTopFrontFollowUp = true
            }
            frontFollowUpAdapter.updateNetWorkStatus(it)
            binding.frontFollowUpSwiperefresh.isRefreshing = it == FrontFollowUpNetWorkStatus.FRONT_FOLLOW_UP_INITIAL_LOADING
        })


       // 刷新列表
        randomViewModel.frontFollowUpRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    lifecycleScope.launch {
                        delay(50)
                        frontFollowUpAdapter.notifyDataSetChanged()
                        randomViewModel.resetFrontFollowUpQuery()
                        randomViewModel.frontFollowUpPosition.value = 0
                        randomViewModel.frontFollowUpRefresh.value = 0
                        getFrontFollowUpNumber(retrofitSingleton,shp.getFrontFollowUpKeyWord()!!)
                    }


                }
                else -> {
                    Log.d("frontUserRefresh", "不刷新 ")
                }
            }
        })
        binding.frontFollowUpListSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
//                randomViewModel.setkeyword(binding.userUserListSearch.text.toString().trim())
                shp.saveToSp("frontfollowupkeyword",binding.frontFollowUpListSearch.text.toString().trim())
                randomViewModel.frontFollowUpRefresh.value = 1
                HideKeyboard.hideKeyboard(v,requireContext())

            }
            false
        }

        binding.cancelSearch.setOnClickListener {
            hideKeyboard(it,requireContext())
            binding.frontFollowUpListSearch.setText("")
            randomViewModel.frontFollowUpRefresh.value = 1


        }

        binding.frontFollowUpListSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    shp.saveToSp("frontfollowupkeyword",s.toString())
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
        //倒计时方法
        var countTIme: CountDownTimer
        binding.selectAll.setOnClickListener {
            randomViewModel.selectState.value = 0
            countTIme  = object :CountDownTimer(300,300){
                override fun onTick(millisUntilFinished: Long) {
                    binding.selectFollowUp.isClickable = false

                }
                override fun onFinish() {
                    binding.selectFollowUp.isClickable = true

                }

            }
            countTIme.start()

        }



        binding.selectFollowUp.setOnClickListener {
            randomViewModel.selectState.value = 1
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

        randomViewModel.selectState.observe(viewLifecycleOwner, Observer {

            if (it == 0) {
                binding.selectAll.isChecked = true
                binding.selectFollowUp.isChecked = false
                //这里接口还没有
                shp.saveToSpInt("frontfollowupstatus", 0)
                shp.saveToSpInt("frontfollow", 1)
                randomViewModel.frontFollowUpRefresh.value = 1


            } else if (it == 1) {
                binding.selectAll.isChecked = false
                binding.selectFollowUp.isChecked = true
                //这里接口还没有
                shp.saveToSpInt("frontfollowupstatus", 0)
                shp.saveToSpInt("frontfollow", 2)
                randomViewModel.frontFollowUpRefresh.value = 1

            }
        })



        randomViewModel.uid.observe(viewLifecycleOwner, Observer {
            shp.saveToSpInt("followuppatientid",it)
        })


        //随访列表

        val followUpAdapter = FollowUpAdapter(randomViewModel,viewLifecycleOwner)
        val linearLayoutFollowUp = LinearLayoutManager(requireContext())
        binding.followUpRecyclerView.apply {
            adapter = followUpAdapter
            layoutManager = linearLayoutFollowUp

        }




        randomViewModel.followUpListLiveData.observe(viewLifecycleOwner, Observer {
            followUpAdapter.notifyDataSetChanged()
            followUpAdapter.submitList(it)

            if (randomViewModel.needToScrollToTopFollowUp){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayoutFollowUp.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                randomViewModel.needToScrollToTopFollowUp = false
            }

        })

        //下滑刷新重新请求列表
        binding.followUpRefresh.setOnRefreshListener {
            randomViewModel.followUpRefresh.value = 1

        }

        randomViewModel.followUpNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == FollowUpNetWorkStatus.FOLLOW_UP_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayoutFollowUp.scrollToPositionWithOffset(0,0)
                randomViewModel.needToScrollToTopFollowUp = true
            }
            followUpAdapter.updateNetWorkStatus(it)
            binding.followUpRefresh.isRefreshing = it == FollowUpNetWorkStatus.FOLLOW_UP_INITIAL_LOADING
        })

        randomViewModel.listSize.observe(requireActivity(), Observer {
            if (it == 0){
                binding.addFollowUp.visibility = View.GONE
                randomViewModel.followUpRefresh.value = 1
            }else {
                binding.addFollowUp.visibility = View.VISIBLE
            }
        })
        //刷新列表
        randomViewModel.followUpRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {

                    if (randomViewModel.listSize.value == 0){
                        lifecycleScope.launch {
                            delay(50)
                            followUpAdapter.notifyDataSetChanged()
                            followUpAdapter.submitList(null)
                            randomViewModel.followUpRefresh.value = 0
                        }
                    }else {
                        followUpAdapter.notifyDataSetChanged()
                        randomViewModel.resetFollowUpQuery()

                        randomViewModel.followUpRefresh.value = 0

                    }


                }
                else -> {
                    Log.d("userTreatRecordRefresh", "不刷新 ")
                }
            }
        })

        followUpAdapter.setOnItemDetailClickListener(object : FollowUpAdapter.OnItemClickListener{
            override fun onItemDetailClick(id: Int) {
                val intent = Intent(requireActivity(), CheckFollowUpDetailActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }

        })

        binding.addFollowUp.setOnClickListener {
            val intent = Intent(requireActivity(), AddRandomVisitActivity::class.java)
            intent.putExtra("patient_id",randomViewModel.uid.value)
            intent.putExtra("name",randomViewModel.name.value)
            startActivityForResult(intent, ADD_FOLLOW_UP_REQUEST_CODE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {

            ADD_FOLLOW_UP_REQUEST_CODE -> {
//                randomViewModel.followUpRefresh.value = 1
                if (resultCode == RESULT_OK){
                    randomViewModel.frontFollowUpRefresh.value = 1
                }

            }
            else -> {
                Log.d("UserFragment", "onActivityResult:没刷新 ")
            }
        }
    }



    fun getFrontFollowUpNumber(retrofitSingleton: RetrofitSingleton, keywords: String) {
        retrofitSingleton.api().getFrontFollowUpList(keywords, shp.getHospitalId()!!, 1, 1,0,1)
            .enqueue(object : Callback<DataClassFrontFollowUp> {
                override fun onResponse(
                    call: Call<DataClassFrontFollowUp>,
                    response: Response<DataClassFrontFollowUp>
                ) {

                    if (response.body()?.code == 0) {
                        randomViewModel.allNumber.value = response.body()?.data?.total
                        randomViewModel.followUpNumber.value = response.body()?.followNum
                    }
//                    else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
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
//                    }
                }

                override fun onFailure(call: Call<DataClassFrontFollowUp>, t: Throwable) {
                    ToastUtils.showTextToast(requireContext(), "首页随访数量网络请求失败")
                }

            })
    }
}