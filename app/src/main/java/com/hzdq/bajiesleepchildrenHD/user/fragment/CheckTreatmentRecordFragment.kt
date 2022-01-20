package com.hzdq.bajiesleepchildrenHD.user.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentCheckTreatmentRecordBinding
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


class CheckTreatmentRecordFragment : Fragment() {

    private lateinit var binding:FragmentCheckTreatmentRecordBinding
    private lateinit var treatmentDetailPicAdapter: TreatmentDetailPicAdapter
    private lateinit var retrofitSingleton: RetrofitSingleton
    private lateinit var userViewModel: UserViewModel
    private lateinit var shp: Shp
    private var tokenDialog: TokenDialog? = null
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_check_treatment_record, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        shp = Shp(requireContext())

        binding.back.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }

        binding.checkRepoertName.text = userViewModel.name.value

        binding.checkReportDoctor.text = "管理医生：${shp.getDoctorName()}"


        getTreatDetail(retrofitSingleton, userViewModel.uid.value!!)
    }


    /**
     * 获取诊疗记录详情
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
                    binding.oahi.text = response.body()?.data?.oahi
                    when(response.body()?.data?.osas){
                        1 -> binding.osas.text = "正常"
                        2 -> binding.osas.text = "轻度"
                        3 -> binding.osas.text = "中度"
                        4 -> binding.osas.text = "重度"
                    }

                    binding.diagnosis.text = response.body()?.data?.opinion
                    binding.checkReportContent.text = response.body()?.data?.treatment


                    treatmentDetailPicAdapter = TreatmentDetailPicAdapter()
                    response.body()?.data?.attachment?.let { treatmentDetailPicAdapter.setList(it) }

                    val param = binding.recyclerView.getLayoutParams()


                    if (response.body()?.data?.attachment?.size == 0){
                        param.height = dip2px(requireContext(),0f)
                        binding.recyclerView.setLayoutParams(param)
                    }else if (response.body()?.data?.attachment?.size == 1){
                        param.height = dip2px(requireContext(),293f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 2){
                        param.height = dip2px(requireContext(),586f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 3){
                        param.height = dip2px(requireContext(),879f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 4){
                        param.height = dip2px(requireContext(),1172f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 5){
                        param.height = dip2px(requireContext(),1465f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 6){
                        param.height = dip2px(requireContext(),1758f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 7){
                        param.height = dip2px(requireContext(),2051f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 8){
                        param.height = dip2px(requireContext(),2344f)
                        binding.recyclerView.setLayoutParams(param)

                    }else if (response.body()?.data?.attachment?.size == 9){
                        param.height = dip2px(requireContext(),2637f)
                        binding.recyclerView.setLayoutParams(param)

                    }
                    val linearLayoutManager: LinearLayoutManager =
                        object : LinearLayoutManager(requireContext(), VERTICAL, false) {
                            override fun canScrollVertically(): Boolean {
                                return false
                            }
                        }
                    binding.recyclerView.apply {
                        adapter = treatmentDetailPicAdapter
                        layoutManager = linearLayoutManager
                    }

                    treatmentDetailPicAdapter.setOnItemClickListener(object :TreatmentDetailPicAdapter.OnItemClickListener{
                        override fun onItemClick(url: String) {
                            userViewModel.checkUrl.value = url

                            userViewModel.fileNames.value = url.replace("http://hxjlforhos.oss-cn-hangzhou.aliyuncs.com/article/","")

                          navController.navigate(R.id.action_checkTreatmentRecordFragment_to_checkRecordDetailPicFragment)
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

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}