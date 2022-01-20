package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateTreatmentPlanBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataAddEvaluate
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassAddEvaluate
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateDetailActivity
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap


class EvaluateTreatmentPlanFragment : Fragment() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:FragmentEvaluateTreatmentPlanBinding
    private lateinit var evaluateViewModel: EvaluateViewModel
    private var popupwindow: PopupWindow? = null
    private var customView: View? = null
    private lateinit var shp: Shp
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate_treatment_plan, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        shp = Shp(requireContext())
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)

        EPSoftKeyBoardListener.setListener(requireActivity(),object :EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                binding.evaluateTreatmentMotion.transitionToEnd()
            }

            override fun keyBoardHide(height: Int) {
                binding.evaluateTreatmentMotion.transitionToStart()
            }

        })
        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })
        evaluateViewModel.result.observe(viewLifecycleOwner, Observer {
            binding.evaluateResult.text = "${it}，建议进一步完善检查"
        })
        setTreat()
        setLine1()
        setOpinion()
        setTreatment()
        setOsaResult()
        setPsqResult()
        setOAHI()

        navBtn()
        binding.complete.clickDelay {
            val map = getMap()
            val url = OkhttpSingleton.BASE_URL+"/v2/estimateSave"
            postAddEvaluate(url,map)
        }

    }

    fun navBtn(){
        binding.navBtn1.setOnClickListener {
            navController.navigate(R.id.evaluateBaseInfoFragment)
        }
        binding.navBtn2.setOnClickListener {
            navController.navigate(R.id.evaluateFamilyFragment)
        }
        binding.navBtn3.setOnClickListener {
            navController.navigate(R.id.evaluateLifeQualityFragment)
        }
        binding.navBtn4.setOnClickListener {
            navController.navigate(R.id.evaluatePastMedicalFragment)
        }
        binding.navBtn5.setOnClickListener {
            navController.navigate(R.id.evaluateInspectFragment)
        }
    }
    fun getMap(): HashMap<String, String> {
        val maps = HashMap<String, String>()
        maps["hospitalid"] = shp.getHospitalId().toString() //18158188052
        maps["patient_id"] = evaluateViewModel.patient_id.value.toString() //18158188052

        maps["id"] = "${evaluateViewModel.id.value}"//111
        maps["sleep"] = evaluateViewModel.sleep.value.toString() //111
        maps["pih"] = "${evaluateViewModel.pih.value}"
        maps["dm"] = "${evaluateViewModel.dm.value}"
        maps["mode"] = "${evaluateViewModel.mode.value}"
        maps["birth"] = "${evaluateViewModel.birth.value}"
        maps["pomr"] = "${evaluateViewModel.pomr.value}"
        maps["cmv"] = "${evaluateViewModel.cmv.value}"

        maps["fhs"] = "${evaluateViewModel.fhs.value}"

        maps["rhinitis"] ="${evaluateViewModel.rhinitis.value}"
        maps["ekc"] ="${evaluateViewModel.ekc.value}"
        maps["asthma"] ="${evaluateViewModel.asthma.value}"
        maps["ihb"] ="${evaluateViewModel.ihb.value}"
        maps["eczema"] ="${evaluateViewModel.eczema.value}"
        maps["thyroid"] ="${evaluateViewModel.thyroid.value}"
        maps["urticaria"] ="${evaluateViewModel.urticaria.value}"
        maps["fat"] ="${evaluateViewModel.fat.value}"
        maps["tnb"] ="${evaluateViewModel.tnb.value}"
        maps["tonsils"] ="${evaluateViewModel.tonsils.value}"
        maps["mdd"] ="${evaluateViewModel.mdd.value}"
        maps["adenoid"] ="${evaluateViewModel.adenoid.value}"
        maps["weight"] ="${evaluateViewModel.weight.value}"
        maps["height"] ="${evaluateViewModel.height.value}"
        maps["bmi"] ="${evaluateViewModel.bmi.value}"
        maps["dns"] ="${evaluateViewModel.dns.value}"
        maps["hypertrophy"] ="${evaluateViewModel.hypertrophy.value}"
        maps["polyp"] ="${evaluateViewModel.polyp.value}"
        maps["face"] ="${evaluateViewModel.face.value}"
        maps["occlusion"] ="${evaluateViewModel.occlusion.value}"
        maps["crossbite"] ="${evaluateViewModel.crossbite.value}"
        maps["brodskyAdenoid"] ="${evaluateViewModel.brodskyAdenoid.value}"
        maps["neck"] ="${evaluateViewModel.neck.value}"
        maps["brodskyTonsils"] ="${evaluateViewModel.brodskyTonsils.value}"
        maps["npc"] ="${evaluateViewModel.npc.value}"
        maps["oahi"] ="${evaluateViewModel.oahi.value}"
        maps["noise"] ="${evaluateViewModel.noise.value}"
        maps["lamp"] ="${evaluateViewModel.lamp.value}"
        maps["crowd"] ="${evaluateViewModel.crowd.value}"
        maps["smoke"] ="${evaluateViewModel.smoke.value}"
        maps["assessment"] ="${evaluateViewModel.assessment.value}"
        maps["osas"] ="${evaluateViewModel.osas.value}"
        maps["opinion"] ="${evaluateViewModel.opinion.value}"
        maps["treat"] ="${evaluateViewModel.treat.value}"
        maps["treatment"] ="${evaluateViewModel.treatment.value}"
        return maps

    }

    fun postAddEvaluate(url:String,map:HashMap<String,String>) {
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


                //封装java对象
                val dataClassAddEvaluate = DataClassAddEvaluate()
                val dataAddEvaluate = DataAddEvaluate()
                try {
                    val jsonObject = JSONObject(res)
                    //第一层解析
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("msg")
                    val data = jsonObject.optJSONObject("data")

                    //第一层封装
                    dataClassAddEvaluate.code = code
                    dataClassAddEvaluate.msg = msg
                    if (data != null) {
                        val id = data.optInt("id")
                        val result = data.optString("result")
                        dataAddEvaluate.id = id
                        dataAddEvaluate.result = result
                        dataClassAddEvaluate.data = dataAddEvaluate
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                requireActivity().runOnUiThread {
                    if (dataClassAddEvaluate.code == 0) {

                        val intent = Intent(requireActivity(),EvaluateDetailActivity::class.java)
                        intent.putExtra("id",dataAddEvaluate.id)
                        startActivity(intent)
                        requireActivity().setResult(RESULT_OK)
                        ActivityCollector2.removeActivity(requireActivity())
                        requireActivity().finish()

                    }else if ( dataClassAddEvaluate.code == 10010 ||  dataClassAddEvaluate.code == 10004) {
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
                    }  else {
                        ToastUtils.showTextToast(
                            requireContext(),
                            "${dataClassAddEvaluate.msg}"
                        )
                    }
                }
            }
        })
    }

    fun setLine1(){
        val map = mapOf(
            1 to binding.normal,
            2 to binding.light,
            3 to binding.moderate,
            4 to binding.severe
        )
        evaluateViewModel.osas.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 -> {
                    binding.normal.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                2 -> {
                    binding.light.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                3 -> {
                    binding.moderate.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                4 -> {
                    binding.severe.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.normal.setOnClickListener { evaluateViewModel.osas.value = 1 }
        binding.light.setOnClickListener { evaluateViewModel.osas.value = 2 }
        binding.moderate.setOnClickListener { evaluateViewModel.osas.value = 3 }
        binding.severe.setOnClickListener { evaluateViewModel.osas.value = 4 }
    }

    fun setOpinion(){

        binding.evaluateOpinion.setText(evaluateViewModel.opinion.value)
        binding.evaluateOpinion.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.opinion.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun setTreatment(){
        binding.treatmentPlan.setText(evaluateViewModel.treatment.value)
        binding.treatmentPlan.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.treatment.value =s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    fun setTreat(){
        binding.l1.setOnClickListener {
            HideKeyboard.hideKeyboard(it,requireContext())
            lifecycleScope.launch {
                delay(130)
                if (popupwindow == null){
                    initPopupWindowView()
                    popupwindow?.showAsDropDown(it, 0, dip2px(requireContext(), 3f))

                } else {
                    popupwindow = null
                }
            }
        }

        evaluateViewModel.treat.observe(viewLifecycleOwner, Observer {
            when(it){
                0 ->  binding.t1.text = "请选择"
                1 -> binding.t1.text = "手术治疗"
                2 -> binding.t1.text = "药物治疗"
                3 -> binding.t1.text = "NPPV治疗"
                4 -> binding.t1.text = "口腔矫治器治疗"
                5 -> binding.t1.text = "健康管理治疗"
                6 -> binding.t1.text = "无需治疗"
            }
        })
    }

    fun setOsaResult(){
        evaluateViewModel.osa1.observe(viewLifecycleOwner, Observer {
            if(!it.equals("")){
                binding.osaResult.text = it
            }else {
                binding.osaResult.text = "--"
            }

        })
    }

    fun setPsqResult(){
        evaluateViewModel.psq1.observe(viewLifecycleOwner, Observer {
            if(!it.equals("")){
                binding.psqResult.text = it

            }else {
                binding.psqResult.text = "--"
            }
        })
    }

    fun setOAHI(){
        evaluateViewModel.oahi.observe(viewLifecycleOwner, Observer {
            if (!it.equals("")){
                binding.oahiResult.text = it
            }else {
                binding.oahiResult.text = "--"
            }
        })
    }


    fun initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图

        binding.i1.setImageResource(R.mipmap.pupop_up_icon)
        customView = layoutInflater.inflate(
            R.layout.treat_layout,
            null, false
        )
        customView?.systemUiVisibility = HideUI(requireActivity()).uiOptions()
        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindow = PopupWindow(
            customView,
            dip2px(requireContext(), 196f),
            dip2px(requireContext(), 250f)
        )
        val t1 = customView?.findViewById<TextView>(R.id.pop_item_treat_1)
        val t2 = customView?.findViewById<TextView>(R.id.pop_item_treat_2)
        val t3 = customView?.findViewById<TextView>(R.id.pop_item_treat_3)
        val t4 = customView?.findViewById<TextView>(R.id.pop_item_treat_4)
        val t5 = customView?.findViewById<TextView>(R.id.pop_item_treat_5)
        val t6 = customView?.findViewById<TextView>(R.id.pop_item_treat_6)
        t1?.setOnClickListener {
//           binding.t1.text = "1级"
            evaluateViewModel.treat.value = 1
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }
        t2?.setOnClickListener {
//            binding.t1.text = "2级"
            evaluateViewModel.treat.value = 2
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }
        t3?.setOnClickListener {
//            binding.t1.text = "3级"
            evaluateViewModel.treat.value = 3
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }
        t4?.setOnClickListener {
//            binding.t1.text = "4级"
            evaluateViewModel.treat.value = 4
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }

        t5?.setOnClickListener {
//            binding.t1.text = "4级"
            evaluateViewModel.treat.value = 5
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }

        t6?.setOnClickListener {
//            binding.t1.text = "4级"
            evaluateViewModel.treat.value = 6
            if (popupwindow != null && popupwindow!!.isShowing()) {
                popupwindow!!.dismiss()
                popupwindow = null
            }
        }
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
        popupwindow?.setOnDismissListener(PopupWindow.OnDismissListener {
            popupwindow = null
            binding.i1.setImageResource(R.mipmap.pupop_down_icon)
        })
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}