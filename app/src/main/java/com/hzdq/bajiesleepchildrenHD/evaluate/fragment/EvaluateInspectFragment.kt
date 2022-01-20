package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateInspectBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataAddEvaluate
import com.hzdq.bajiesleepchildrenHD.dataclass.DataClassAddEvaluate
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


class EvaluateInspectFragment : Fragment() {

private lateinit var binding:FragmentEvaluateInspectBinding
    private var popupwindow1: PopupWindow? = null
    private var popupwindow2: PopupWindow? = null
    private var customView1: View? = null
    private var customView2: View? = null
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var navController: NavController
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp:Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate_inspect, container, false)

        return binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)
        navController = Navigation.findNavController(view)
        shp = Shp(requireContext())
        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })
        binding.frontPage.clickDelay {
            navController.navigate(R.id.action_evaluateInspectFragment_to_evaluatePastMedicalFragment)
        }

        binding.nextPage.clickDelay {
            onOption()
            val map = getMap()
            val url = OkhttpSingleton.BASE_URL+"/v2/estimateSave"



            if (evaluateViewModel.noOption1.value == 0 && evaluateViewModel.noOption2.value == 0 && evaluateViewModel.noOption3.value == 0){
                ToastUtils.showTextToast(requireContext(),"未填任何选项")
                return@clickDelay
            }
            if (evaluateViewModel.height.value.equals("0")){
                ToastUtils.showTextToast(requireContext(),"身高必须为正数")
                return@clickDelay
            }

            if (evaluateViewModel.weight.value.equals("0")){
                ToastUtils.showTextToast(requireContext(),"体重必须为正数")
                return@clickDelay
            }

            if (evaluateViewModel.neck.value.equals("0")){
                ToastUtils.showTextToast(requireContext(),"颈围必须为正数")
                return@clickDelay
            }

            if (evaluateViewModel.npc.value.equals("0")){
                ToastUtils.showTextToast(requireContext(),"鼻咽侧位必须为正数")
                return@clickDelay
            }
//            navController.navigate(R.id.action_evaluateInspectFragment_to_evaluateTreatmentPlanFragment)
            postAddEvaluate(url,map)
        }


        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })


        setHeight()
        setWeight()
        setBmi()
        setBrodsky4()
        setBrodsky5()
        setOAHI()
        setNeck()
        setNpc()


        setLine1()
        setLine2()
        setLine3()
        setLine6()
        setLine7()
        setLine8()

        navBtn()

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
    fun getMap():HashMap<String,String>{
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
        return maps

    }
    fun postAddEvaluate(url:String,map: HashMap<String, String>) {
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

                Log.d("evaluatepost", "onResponse:$res ")

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
                        evaluateViewModel.result.value = dataAddEvaluate.result
                        evaluateViewModel.id.value = dataAddEvaluate.id

                        navController.navigate(R.id.action_evaluateInspectFragment_to_evaluateTreatmentPlanFragment)
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
                    } else {
                        ToastUtils.showTextToast(
                            requireContext(),
                            "${dataClassAddEvaluate.msg}"
                        )
                    }
                }
            }
        })
    }
    fun onOption(){
        val list2 = listOf(
            evaluateViewModel.f11.value,
            evaluateViewModel.f12.value,
            evaluateViewModel.f13.value,
            evaluateViewModel.f14.value,
            evaluateViewModel.f15.value,
            evaluateViewModel.f16.value,
            evaluateViewModel.f17.value,
            evaluateViewModel.f18.value,
            evaluateViewModel.f21.value,
            evaluateViewModel.f22.value,
            evaluateViewModel.f23.value,
            evaluateViewModel.f24.value,
            evaluateViewModel.f25.value,
            evaluateViewModel.f26.value,
            evaluateViewModel.f27.value,
            evaluateViewModel.f28.value,
            evaluateViewModel.f31.value,
            evaluateViewModel.f32.value,
            evaluateViewModel.f33.value,
            evaluateViewModel.f34.value,
            evaluateViewModel.f35.value,
            evaluateViewModel.f36.value,
            evaluateViewModel.f37.value,
            evaluateViewModel.f38.value,
            evaluateViewModel.f41.value,
            evaluateViewModel.f42.value,
            evaluateViewModel.f43.value,
            evaluateViewModel.f44.value,
            evaluateViewModel.f45.value,
            evaluateViewModel.f46.value,
            evaluateViewModel.f47.value,
            evaluateViewModel.f48.value,
            evaluateViewModel.f51.value,
            evaluateViewModel.f52.value,
            evaluateViewModel.f53.value,
            evaluateViewModel.f54.value,
            evaluateViewModel.f55.value,
            evaluateViewModel.f56.value,
            evaluateViewModel.f57.value,
            evaluateViewModel.f58.value,
            evaluateViewModel.f61.value,
            evaluateViewModel.f62.value,
            evaluateViewModel.f63.value,
            evaluateViewModel.f64.value,
            evaluateViewModel.f65.value,
            evaluateViewModel.f66.value,
            evaluateViewModel.f67.value,
            evaluateViewModel.f68.value,
            evaluateViewModel.f71.value,
            evaluateViewModel.f72.value,
            evaluateViewModel.f73.value,
            evaluateViewModel.f74.value,
            evaluateViewModel.f75.value,
            evaluateViewModel.f76.value,
            evaluateViewModel.f77.value,
            evaluateViewModel.f78.value,
            evaluateViewModel.f81.value,
            evaluateViewModel.f82.value,
            evaluateViewModel.f83.value,
            evaluateViewModel.f84.value,
            evaluateViewModel.f85.value,
            evaluateViewModel.f86.value,
            evaluateViewModel.f87.value,
            evaluateViewModel.f88.value,
            0
        )

        for (i in 0..list2.size-1) {
            if(list2[i] == 0){
                if (i == 64){
                    Log.d("onOption", "onOption2:不成功 ")
                    evaluateViewModel.noOption2.value = 0
                }
                continue
            }else {
                evaluateViewModel.noOption2.value = 1
                break
            }
        }

        val list1 = listOf(
            evaluateViewModel.sleep.value!!,
            evaluateViewModel.pih.value!!,
            evaluateViewModel.dm.value!!,
            evaluateViewModel.mode.value!!,
            evaluateViewModel.birth.value!!,
            evaluateViewModel.pomr.value!!,
            evaluateViewModel.cmv.value!!,
            evaluateViewModel.rhinitis.value!!,
            evaluateViewModel.asthma.value!!,
            evaluateViewModel.eczema.value!!,
            evaluateViewModel.urticaria.value!!,
            evaluateViewModel.tnb.value!!,
            evaluateViewModel.mdd.value!!,
            evaluateViewModel.ekc.value!!,
            evaluateViewModel.ihb.value!!,
            evaluateViewModel.thyroid.value!!,
            evaluateViewModel.fat.value!!,
            evaluateViewModel.tonsils.value!!,
            evaluateViewModel.adenoid.value!!,

            evaluateViewModel.dns.value!!,
            evaluateViewModel.hypertrophy.value!!,
            evaluateViewModel.polyp.value!!,
            evaluateViewModel.face.value!!,
            evaluateViewModel.occlusion.value!!,
            evaluateViewModel.crossbite.value!!,
            evaluateViewModel.brodskyAdenoid.value!!,
            evaluateViewModel.brodskyTonsils.value!!,

            evaluateViewModel.noise.value!!,
            evaluateViewModel.lamp.value!!,
            evaluateViewModel.crowd.value!!,
            evaluateViewModel.smoke.value!!,
            0
        )

        for (i in 0..list1.size-1) {
            if(list1[i] == 0){

                if (i == 31){
                    Log.d("onOption", "onOption1:不成功 ")

                    evaluateViewModel.noOption1.value = 0
                }
                continue
            }else {

                evaluateViewModel.noOption1.value = 1
                break
            }
        }

        val list3 = listOf(
            evaluateViewModel.height.value!!,
            evaluateViewModel.weight.value!!,
            evaluateViewModel.bmi.value!!,
            evaluateViewModel.neck.value!!,
            evaluateViewModel.npc.value!!,
            evaluateViewModel.oahi.value!!,
            evaluateViewModel.assessment.value!!,
            evaluateViewModel.osa.value!!,
            evaluateViewModel.psq.value!!,
            evaluateViewModel.osa1.value!!,
            evaluateViewModel.psq1.value!!,
            ""
        )


        for (i in 0..list3.size-1) {


            if(list3[i].equals("")){

                if (i == 11 ){
                    Log.d("onOption", "onOption3:不成功 ")

                    evaluateViewModel.noOption3.value = 0
                }
                continue
            }else {
                evaluateViewModel.noOption3.value = 1
                break
            }
        }

    }



    fun initPopupWindowView1() {
        // // 获取自定义布局文件pop.xml的视图

        binding.i1.setImageResource(R.mipmap.pupop_up_icon)
        customView1 = layoutInflater.inflate(
            R.layout.brodsky4_layout,
            null, false
        )
        customView1?.systemUiVisibility = HideUI(requireActivity()).uiOptions()
        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindow1 = PopupWindow(
            customView1,
            dip2px(requireContext(), 196f),
            dip2px(requireContext(), 166f)
        )
        val t1 = customView1?.findViewById<TextView>(R.id.pop_item_brodsky4_1)
        val t2 = customView1?.findViewById<TextView>(R.id.pop_item_brodsky4_2)
        val t3 = customView1?.findViewById<TextView>(R.id.pop_item_brodsky4_3)
        val t4 = customView1?.findViewById<TextView>(R.id.pop_item_brodsky4_4)
        t1?.setOnClickListener {
//           binding.t1.text = "1级"
            evaluateViewModel.brodskyAdenoid.value = 1
            if (popupwindow1 != null && popupwindow1!!.isShowing()) {
                popupwindow1!!.dismiss()
                popupwindow1 = null
            }
        }
        t2?.setOnClickListener {
//            binding.t1.text = "2级"
            evaluateViewModel.brodskyAdenoid.value = 2
            if (popupwindow1 != null && popupwindow1!!.isShowing()) {
                popupwindow1!!.dismiss()
                popupwindow1 = null
            }
        }
        t3?.setOnClickListener {
//            binding.t1.text = "3级"
            evaluateViewModel.brodskyAdenoid.value = 3
            if (popupwindow1 != null && popupwindow1!!.isShowing()) {
                popupwindow1!!.dismiss()
                popupwindow1 = null
            }
        }
        t4?.setOnClickListener {
//            binding.t1.text = "4级"
            evaluateViewModel.brodskyAdenoid.value = 4
            if (popupwindow1 != null && popupwindow1!!.isShowing()) {
                popupwindow1!!.dismiss()
                popupwindow1 = null
            }
        }
        popupwindow1?.setOutsideTouchable(true)
        popupwindow1?.setFocusable(true)
        popupwindow1?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        customView1?.setOnTouchListener(View.OnTouchListener { v, event ->
            if (popupwindow1 != null && popupwindow1!!.isShowing()) {
                popupwindow1!!.dismiss()
                popupwindow1 = null
            }
            true
        })
        popupwindow1?.setOnDismissListener(PopupWindow.OnDismissListener {
            popupwindow1 = null
            binding.i1.setImageResource(R.mipmap.pupop_down_icon)
        })
    }

    fun initPopupWindowView2() {
        // // 获取自定义布局文件pop.xml的视图

        binding.i2.setImageResource(R.mipmap.pupop_up_icon)
        customView2 = layoutInflater.inflate(
            R.layout.brodsky5_layout,
            null, false
        )
        customView2?.systemUiVisibility = HideUI(requireActivity()).uiOptions()
        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindow2 = PopupWindow(
            customView2,
            dip2px(requireContext(), 196f),
            dip2px(requireContext(), 208f)
        )
        val t1 = customView2?.findViewById<TextView>(R.id.pop_item_brodsky5_1)
        val t2 = customView2?.findViewById<TextView>(R.id.pop_item_brodsky5_2)
        val t3 = customView2?.findViewById<TextView>(R.id.pop_item_brodsky5_3)
        val t4 = customView2?.findViewById<TextView>(R.id.pop_item_brodsky5_4)
        val t5 = customView2?.findViewById<TextView>(R.id.pop_item_brodsky5_5)
        t1?.setOnClickListener {
//            binding.t2.text = "1级"
            evaluateViewModel.brodskyTonsils.value = 1
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
        }
        t2?.setOnClickListener {
//            binding.t2.text = "2级"
            evaluateViewModel.brodskyTonsils.value = 2
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
        }
        t3?.setOnClickListener {
//            binding.t2.text = "3级"
            evaluateViewModel.brodskyTonsils.value = 3
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
        }
        t4?.setOnClickListener {
//            binding.t2.text = "4级"
            evaluateViewModel.brodskyTonsils.value = 4
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
        }
        t5?.setOnClickListener {
//            binding.t2.text = "5级"
            evaluateViewModel.brodskyTonsils.value = 5
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
        }
        popupwindow2?.setOutsideTouchable(true)
        popupwindow2?.setFocusable(true)
        popupwindow2?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        customView2?.setOnTouchListener(View.OnTouchListener { v, event ->
            if (popupwindow2 != null && popupwindow2!!.isShowing()) {
                popupwindow2!!.dismiss()
                popupwindow2 = null
            }
            true
        })
        popupwindow2?.setOnDismissListener(PopupWindow.OnDismissListener {
            popupwindow2 = null
            binding.i2.setImageResource(R.mipmap.pupop_down_icon)
        })
    }

    fun setHeight(){
        //身高
        binding.height.setText(evaluateViewModel.height.value)
        binding.height.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.height.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        evaluateViewModel.height.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setBmi()
        })
    }

    fun setWeight(){
        //体重
        binding.weight.setText(evaluateViewModel.weight.value)
        binding.weight.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.weight.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        evaluateViewModel.weight.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setBmi()
        })
    }

    fun setBmi(){

        evaluateViewModel.bmi.observe(viewLifecycleOwner, Observer {
            if (it.equals("")){
                binding.bmi.text = "BMI：--"
            }else {
                binding.bmi.text = "BMI：${it}"
            }

        })
    }



    fun setBrodsky4(){
        binding.l4.setOnClickListener {
            HideKeyboard.hideKeyboard(it,requireContext())
            lifecycleScope.launch {
                delay(70)
                if (popupwindow1 == null){
                    initPopupWindowView1()
                    popupwindow1?.showAsDropDown(it, 0, dip2px(requireContext(), 3f))

                } else {
                    popupwindow1 = null
                }
            }

        }
        evaluateViewModel.brodskyAdenoid.observe(viewLifecycleOwner, Observer {
            when(it){
                0 ->  binding.t1.text = "请选择分级"
                else -> binding.t1.text = "${it}级"
            }
        })
    }

    fun setBrodsky5(){
        binding.l5.setOnClickListener {
            HideKeyboard.hideKeyboard(it,requireContext())
            lifecycleScope.launch {
                delay(70)
                if (popupwindow2 == null ){

                    initPopupWindowView2()
                    popupwindow2?.showAsDropDown(it, 0, dip2px(requireContext(), 3f))

                } else {
                    popupwindow2 = null
                }
            }

        }
        evaluateViewModel.brodskyTonsils.observe(viewLifecycleOwner, Observer {
            when(it){
                0 ->  binding.t2.text = "请选择分级"
                else -> binding.t2.text = "${it}级"
            }
        })
    }

    fun setOAHI(){
        binding.oahi.setText(evaluateViewModel.oahi.value)
        binding.oahi.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.oahi.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun setNeck(){
        binding.neckCircumference.setText(evaluateViewModel.neck.value)

        binding.neckCircumference.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.neck.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun setNpc(){
        binding.npc.setText(evaluateViewModel.npc.value)

        binding.npc.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateViewModel.npc.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun setLine1(){
        val map  = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )

        evaluateViewModel.dns.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b11.isSelected = true
                    binding.b11.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b12.isSelected = true
                    binding.b12.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b11.setOnClickListener { evaluateViewModel.dns.value = 2 }
            binding.b12.setOnClickListener { evaluateViewModel.dns.value = 1 }
        })
    }

    fun setLine2(){
        val map  = mapOf(
            1 to binding.b21,
            2 to binding.b22
        )

        evaluateViewModel.polyp.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b21.isSelected = true
                    binding.b21.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b22.isSelected = true
                    binding.b22.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b21.setOnClickListener { evaluateViewModel.polyp.value = 2 }
            binding.b22.setOnClickListener { evaluateViewModel.polyp.value = 1 }
        })
    }

    fun setLine3(){
        val map  = mapOf(
            1 to binding.b31,
            2 to binding.b32
        )

        evaluateViewModel.occlusion.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b31.isSelected = true
                    binding.b31.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.l8.visibility  = View.VISIBLE
                }
                1 -> {
                    binding.b32.isSelected = true
                    binding.b32.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.l8.visibility  = View.GONE
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b31.setOnClickListener {
                evaluateViewModel.occlusion.value = 2
            }
            binding.b32.setOnClickListener {
                evaluateViewModel.occlusion.value = 1
                evaluateViewModel.crossbite.value = 0
            }
        })
    }

    fun setLine8(){
        val map  = mapOf(
            1 to binding.b81,
            2 to binding.b82
        )

        evaluateViewModel.crossbite.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b81.isSelected = true
                    binding.b81.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b82.isSelected = true
                    binding.b82.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b81.setOnClickListener { evaluateViewModel.crossbite.value = 2 }
            binding.b82.setOnClickListener { evaluateViewModel.crossbite.value = 1 }
        })
    }

    fun setLine6(){
        val map  = mapOf(
            1 to binding.b61,
            2 to binding.b62
        )

        evaluateViewModel.hypertrophy.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b61.isSelected = true
                    binding.b61.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b62.isSelected = true
                    binding.b62.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b61.setOnClickListener { evaluateViewModel.hypertrophy.value = 2 }
            binding.b62.setOnClickListener { evaluateViewModel.hypertrophy.value = 1 }
        })
    }
    fun setLine7(){
        val map  = mapOf(
            1 to binding.b71,
            2 to binding.b72
        )

        evaluateViewModel.face.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 -> {
                    binding.b71.isSelected = true
                    binding.b71.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b72.isSelected = true
                    binding.b72.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b71.setOnClickListener { evaluateViewModel.face.value = 2 }
            binding.b72.setOnClickListener { evaluateViewModel.face.value = 1 }
        })
    }



    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}