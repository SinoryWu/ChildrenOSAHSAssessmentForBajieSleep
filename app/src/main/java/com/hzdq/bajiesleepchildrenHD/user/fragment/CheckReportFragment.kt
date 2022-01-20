package com.hzdq.bajiesleepchildrenHD.user.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentCheckReportBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassTreatmentDetail
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.adapter.TreatmentDetailPicAdapter
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class CheckReportFragment : Fragment() {
    private lateinit var treatmentDetailPicAdapter: TreatmentDetailPicAdapter
    private lateinit var retrofitSingleton: RetrofitSingleton
    private lateinit var userViewModel: UserViewModel
    private lateinit var shp: Shp
    private lateinit var navController: NavController
    private lateinit var binding:FragmentCheckReportBinding
    private var tokenDialog: TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_check_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        shp = Shp(requireContext())
        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        navController = Navigation.findNavController(view)

        binding.back.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }

        binding.checkRepoertName.text = userViewModel.name.value

        binding.checkReportDoctor.text = "管理医生：${shp.getDoctorName()}"

        getTreatDetail(retrofitSingleton, userViewModel.uid.value!!)

    }

    /**
     * 获取报告记录详情
     */
    fun getTreatDetail(retrofitSingleton: RetrofitSingleton,id:Int){
        retrofitSingleton.api().getTreatDetail(id).enqueue(object :
            Callback<DataClassTreatmentDetail> {
            override fun onResponse(
                call: Call<DataClassTreatmentDetail>,
                response: Response<DataClassTreatmentDetail>
            ) {
                if (response.body()?.code == 0){
//                    binding.checkReportDoctor.text = response.body()?.data?.doctorName
                    binding.time.text = "${timestamp2Date("${response.body()?.data?.createTime}","yyyy/MM//dd HH:mm")}"


                    binding.checkReportContent.text = "${response.body()?.data?.opinion}"
                    when(response.body()?.data?.subtype){
                        21 -> binding.title.text = "睡眠报告"
                        22 -> binding.title.text = "影像检查"
                        23 -> binding.title.text = "内镜检查"
                        24 -> binding.title.text = "生化检查"
                        25 -> binding.title.text = "其他"
                    }

                    if (response.body()?.data?.subtype == 21){
                        binding.scrollContent.visibility = View.GONE
                        binding.scrollSleepContent.visibility = View.VISIBLE
                        binding.textView80.visibility = View.VISIBLE
                        binding.textView81.visibility = View.GONE
                        binding.oahi.visibility = View.VISIBLE
                        binding.textView92.visibility = View.VISIBLE
                        binding.osas.visibility = View.VISIBLE
                        binding.checkSleepReportContent.text = response.body()?.data?.opinion
                        binding.oahi.text = response.body()?.data?.oahi
                        when(response.body()?.data?.osas){
                            1 -> binding.osas.text = "正常"
                            2 -> binding.osas.text = "轻度"
                            3 -> binding.osas.text = "中度"
                            4 -> binding.osas.text = "重度"
                            else -> binding.osas.text = ""
                        }

                    }else {
                        binding.scrollSleepContent.visibility = View.GONE
                        binding.scrollContent.visibility = View.VISIBLE
                        binding.textView80.visibility = View.GONE
                        binding.textView81.visibility = View.VISIBLE
                        binding.oahi.visibility = View.GONE
                        binding.textView92.visibility = View.GONE
                        binding.osas.visibility = View.GONE
                        binding.checkReportContent.text = response.body()?.data?.opinion
                    }




                    treatmentDetailPicAdapter = TreatmentDetailPicAdapter()
                    response.body()?.data?.attachment?.let { treatmentDetailPicAdapter.setList(it) }

                    val linearLayoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.apply {
                        adapter = treatmentDetailPicAdapter
                        layoutManager = linearLayoutManager
                    }

                    treatmentDetailPicAdapter.setOnItemClickListener(object :TreatmentDetailPicAdapter.OnItemClickListener{
                        override fun onItemClick(url: String) {
                            userViewModel.checkUrl.value = url

                            userViewModel.fileNames.value = url.replace("http://hxjlforhos.oss-cn-hangzhou.aliyuncs.com/article/","")

                            navController.navigate(R.id.action_checkReportFragment_to_checkReportDetailPicFragment)
                        }

                    })

                }else if (response.body()?.code == 10010 || response.body()?.code == 10004) {
                    if (tokenDialog == null) {
                        tokenDialog = TokenDialog(
                            requireContext(),
                            object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            LoginActivity::class.java
                                        )
                                    )
                                    ActivityCollector2.finishAll()
                                }
                            })
                        tokenDialog!!.show()
                        tokenDialog!!.setCanceledOnTouchOutside(false)
                    } else {
                        tokenDialog!!.show()
                        tokenDialog!!.setCanceledOnTouchOutside(false)
                    }
                }else {
                    ToastUtils.showTextToast(requireContext(),"${response.body()?.msg}")
                }
            }

            override fun onFailure(call: Call<DataClassTreatmentDetail>, t: Throwable) {
                ToastUtils.showTextToast(requireContext(),"获取诊疗记录详情网络请求失败")
            }

        })
    }
}