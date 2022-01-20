package com.hzdq.bajiesleepchildrenHD.frontpagefragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog

import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassFrontUser
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateActivity
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateDetailActivity
import com.hzdq.bajiesleepchildrenHD.evaluate.adapter.EvaluateRecordAdapter
import com.hzdq.bajiesleepchildrenHD.evaluate.adapter.FrontEvaluateAdapter
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.EvaluateRecordNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.FrontEvaluateNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.adapter.FrontUserAdapter
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideKeyboard
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 评估fragment
 */
class EvaluateFragment : Fragment() {
    private lateinit var binding: FragmentEvaluateBinding
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var shp: Shp
    private lateinit var retrofitSingleton: RetrofitSingleton
    private var tokenDialog :TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)

        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())
        shp.saveToSp("frontevaluatekeyword","")
        getTotalCount(retrofitSingleton,"")


        binding.addEvaluate.setOnClickListener {
            val intent = Intent(requireActivity(),EvaluateActivity::class.java)
            intent.putExtra("patientid",evaluateViewModel.patient_id.value)
            intent.putExtra("name",evaluateViewModel.name.value)
//            patientid
            startActivityForResult(intent,5)
        }

        val frontEvaluateAdapter = FrontEvaluateAdapter(evaluateViewModel,requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            adapter = frontEvaluateAdapter
            layoutManager = linearLayout

        }

        evaluateViewModel.patient_id.observe(viewLifecycleOwner, Observer {
            shp.saveToSpInt("patientid",it)
            lifecycleScope.launch {
                delay(100)
                evaluateViewModel.evaluateRecordRefresh.value = 1
            }

        })


        evaluateViewModel.frontEvaluateListLiveData.observe(viewLifecycleOwner, Observer {
            frontEvaluateAdapter.notifyDataSetChanged()
            frontEvaluateAdapter.submitList(it)

            if (evaluateViewModel.needToScrollToTopFrontEvaluate){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                evaluateViewModel.needToScrollToTopFrontEvaluate = false
            }

            if (it.size == 0){
                evaluateViewModel.listSize.value = 0
            }else {
                evaluateViewModel.listSize.value = 1
            }
        })


        frontEvaluateAdapter.setOnItemClickListener(object : FrontEvaluateAdapter.OnItemClickListener{
            override fun onItemClick(dateItem: DataFrontUserX) {
                evaluateViewModel.firstItemPosition.value = linearLayout.findFirstVisibleItemPosition()
            }

        })

        //下滑刷新重新请求列表
        binding.frontEvaluateRefresh.setOnRefreshListener {
            evaluateViewModel.frontEvaluateRefresh.value = 1
//            frontEvaluateAdapter.notifyDataSetChanged()
//            evaluateViewModel.resetFrontEvaluateQuery()
//            evaluateViewModel.frontEvaluatePosition.value = 0

        }

        evaluateViewModel.frontEvaluateNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == FrontEvaluateNetWorkStatus.FRONT_EVALUATE_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0,0)
                evaluateViewModel.needToScrollToTopFrontEvaluate = true
            }
            frontEvaluateAdapter.updateNetWorkStatus(it)
            binding.frontEvaluateRefresh.isRefreshing = it == FrontEvaluateNetWorkStatus.FRONT_EVALUATE_INITIAL_LOADING
        })

        //刷新列表
        evaluateViewModel.frontEvaluateRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {
                    lifecycleScope.launch {
                        delay(50)
                        frontEvaluateAdapter.notifyDataSetChanged()
                        evaluateViewModel.resetFrontEvaluateQuery()
                        evaluateViewModel.frontEvaluatePosition.value = 0
                        evaluateViewModel.frontEvaluateRefresh.value = 0
                        getTotalCount(retrofitSingleton,shp.getFrontEvaluateKeyWord()!!)
                    }

                }
                else -> {
                    Log.d("frontUserRefresh", "不刷新 ")
                }
            }
        })


        evaluateViewModel.totalCount.observe(viewLifecycleOwner, Observer {
            binding.userCount.text = "共找到${it}位用户"
        })


        val evaluateRecordAdapter = EvaluateRecordAdapter(evaluateViewModel,requireActivity())
        val linearLayout2 = LinearLayoutManager(requireContext())
        binding.evaluateRecordRecyclerview.apply {
            adapter = evaluateRecordAdapter
            layoutManager = linearLayout2

        }

        evaluateViewModel.evaluateRecordListLiveData.observe(viewLifecycleOwner, Observer {
            evaluateRecordAdapter.notifyDataSetChanged()
            evaluateRecordAdapter.submitList(it)

            if (evaluateViewModel.needToScrollToTopEvaluateRecord){
//                binding.userFragmentRecyclerView.scrollToPosition(0)

                lifecycleScope.launch {
                    delay(50)
                    linearLayout2.scrollToPositionWithOffset(0,0)
//                    binding.userFragmentRecyclerView.scrollToPosition(10)
                }

                evaluateViewModel.needToScrollToTopEvaluateRecord = false
            }

        })


        //下滑刷新重新请求列表
        binding.evaluateRecordRefresh.setOnRefreshListener {
            evaluateViewModel.evaluateRecordRefresh.value = 1

        }
        evaluateViewModel.evaluateRecordNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == EvaluateRecordNetWorkStatus.EVALUATE_RECORD_INITIAL_LOADED){

//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout2.scrollToPositionWithOffset(0,0)
                evaluateViewModel.needToScrollToTopEvaluateRecord = true
            }
            evaluateRecordAdapter.updateNetWorkStatus(it)
            binding.evaluateRecordRefresh.isRefreshing = it == EvaluateRecordNetWorkStatus.EVALUATE_RECORD_INITIAL_LOADING
        })

        //刷新列表
        evaluateViewModel.evaluateRecordRefresh.observe(requireActivity(), Observer {
            when(it){
                1 -> {

                    if (evaluateViewModel.listSize.value == 0){
                        lifecycleScope.launch{
                            delay(50)
                            evaluateRecordAdapter.notifyDataSetChanged()
                            evaluateRecordAdapter.submitList(null)

                        }
                    }else {
                        evaluateRecordAdapter.notifyDataSetChanged()
                        evaluateViewModel.resetEvaluateRecordQuery()
                        linearLayout2.scrollToPositionWithOffset(0,0)
                        evaluateViewModel.evaluateRecordRefresh.value = 0
                    }

                }
                else -> {

                }
            }
        })

        evaluateRecordAdapter.setOnItemDetailClickListener(object :EvaluateRecordAdapter.OnItemDetailClickListener{
            override fun onItemDetailClick(id: Int) {
                val intent = Intent(requireActivity(),EvaluateDetailActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
//                Log.d("sadsadsad", "onItemDetailClick:$id ")
            }

        })

        //按下搜索键
        binding.listSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                shp.saveToSp("frontevaluatekeyword",binding.listSearch.text.toString().trim())
                evaluateViewModel.frontEvaluateRefresh.value = 1
                HideKeyboard.hideKeyboard(v,requireContext())
                getTotalCount(retrofitSingleton,binding.listSearch.text.toString().trim())
            }
            false
        }

        //取消搜索
        binding.cancelSearch.setOnClickListener {
            HideKeyboard.hideKeyboard(it,requireContext())
            binding.listSearch.setText("")
            evaluateViewModel.frontEvaluateRefresh.value = 1

            getTotalCount(retrofitSingleton,shp.getFrontEvaluateKeyWord()!!)
        }

        //搜索输入框输入内容
        binding.listSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    shp.saveToSp("frontevaluatekeyword","")

                    binding.cancelSearch.visibility = View.GONE
                }else {
                    binding.cancelSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        evaluateViewModel.listSize.observe(requireActivity(), Observer {
            if (it == 0){

                binding.addEvaluate.visibility = View.GONE
            }else {
                binding.addEvaluate.visibility = View.VISIBLE
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            5 -> {
                if(resultCode == RESULT_OK){
                    evaluateViewModel.evaluateRecordRefresh.value = 1
                    evaluateViewModel.frontEvaluateRefresh.value = 1
                }
            }
            else -> {
                Log.d("onActivityResult", "onActivityResult: 没刷新")
            }
        }
    }

    fun getTotalCount(retrofit: RetrofitSingleton, kewords:String){
        retrofit.api().getFrontUserList(kewords,shp.getHospitalId()!!,1,1,0).enqueue(object :
            Callback<DataClassFrontUser> {
            override fun onResponse(
                call: Call<DataClassFrontUser>,
                response: Response<DataClassFrontUser>
            ) {
                if (response.body()?.code == 0){
                    evaluateViewModel.totalCount.value = response.body()?.data?.total
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
                ToastUtils.showTextToast(requireContext(),"获取评估数量网络请求失败")
            }

        })
    }
}