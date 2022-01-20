package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateLifeQualityBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAdd2Activity
import com.hzdq.bajiesleepchildrenHD.user.activities.UserScreenAddActivity
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.HashMap


class EvaluateLifeQualityFragment : Fragment() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding: FragmentEvaluateLifeQualityBinding
    private lateinit var navController: NavController
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var shp: Shp
    private lateinit var retrofitSingleton: RetrofitSingleton
    val assessment: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_evaluate_life_quality,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)

        shp = Shp(requireContext())
        retrofitSingleton = RetrofitSingleton.getInstance(requireContext())
        val addEvaluateBody = AddEvaluateBody()
        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })
        val map1 = mapOf(
            1 to evaluateViewModel.sleep.value!!,
            2 to evaluateViewModel.pih.value!!,
            3 to evaluateViewModel.dm.value!!,
            4 to evaluateViewModel.mode.value!!,
            5 to evaluateViewModel.birth.value!!,
            6 to evaluateViewModel.pomr.value!!,
            7 to evaluateViewModel.cmv.value!!,


            8 to evaluateViewModel.rhinitis.value!!,
            9 to evaluateViewModel.asthma.value!!,
            10 to evaluateViewModel.eczema.value!!,
            11 to evaluateViewModel.urticaria.value!!,
            12 to evaluateViewModel.tnb.value!!,
            13 to evaluateViewModel.mdd.value!!,
            14 to evaluateViewModel.ekc.value!!,
            15 to evaluateViewModel.ihb.value!!,
            16 to evaluateViewModel.thyroid.value!!,
            17 to evaluateViewModel.fat.value!!,
            18 to evaluateViewModel.tonsils.value!!,
            19 to evaluateViewModel.adenoid.value!!,

            20 to evaluateViewModel.dns.value!!,
            21 to evaluateViewModel.hypertrophy.value!!,
            22 to evaluateViewModel.polyp.value!!,
            23 to evaluateViewModel.face.value!!,
            24 to evaluateViewModel.occlusion.value!!,
            25 to evaluateViewModel.crossbite.value!!,
            26 to evaluateViewModel.brodskyAdenoid.value!!,
            27 to evaluateViewModel.brodskyTonsils.value!!,

            28 to evaluateViewModel.noise.value!!,
            29 to evaluateViewModel.lamp.value!!,
            30 to evaluateViewModel.crowd.value!!,
            31 to evaluateViewModel.smoke.value!!
        )

        val map2 = mapOf(
            1 to evaluateViewModel.fhs.value!!,
            2 to evaluateViewModel.height.value!!,
            3 to evaluateViewModel.weight.value!!,
            4 to evaluateViewModel.bmi.value!!,
            5 to evaluateViewModel.neck.value!!,
            6 to evaluateViewModel.npc.value!!,
            7 to evaluateViewModel.oahi.value!!,
            8 to evaluateViewModel.assessment.value!!,
            9 to evaluateViewModel.osa.value!!,
            10 to evaluateViewModel.psq.value!!,
            11 to evaluateViewModel.osa1.value!!,
            12 to evaluateViewModel.psq1.value!!,
        )





        binding.osaButton.setOnClickListener {
            val intent = Intent(requireActivity(), UserScreenAddActivity::class.java)
            intent.putExtra("evaluate", 1)
            intent.putExtra("uid", evaluateViewModel.patient_id.value)
            intent.putExtra("name", evaluateViewModel.name.value)
            startActivityForResult(intent, 4)
        }

        binding.psqButton.setOnClickListener {
            val intent = Intent(requireActivity(), UserScreenAdd2Activity::class.java)
            intent.putExtra("evaluate", 1)
            intent.putExtra("uid", evaluateViewModel.patient_id.value)
            intent.putExtra("name", evaluateViewModel.name.value)
            startActivityForResult(intent, 5)
        }

        setOsaResult()
        setPsqResult()
        setLine1()
        setLine2()
        setLine3()
        setLine4()



        binding.frontPage.setOnClickListener {
            navController.navigate(R.id.action_evaluateLifeQualityFragment_to_evaluateFamilyFragment)
            if (!evaluateViewModel.osa.value.equals("") && evaluateViewModel.psq.value.equals("")) {
                assessment.add(evaluateViewModel.osa.value!!)
            } else if (!evaluateViewModel.psq.value.equals("") && evaluateViewModel.osa.value.equals(
                    ""
                )
            ) {
                assessment.add(evaluateViewModel.psq.value!!)
            } else if (!evaluateViewModel.psq.value.equals("") && !evaluateViewModel.osa.value.equals(
                    ""
                )
            ) {
                assessment.add(evaluateViewModel.osa.value!!)
                assessment.add(evaluateViewModel.psq.value!!)
            }

            if (assessment.size == 0){
                evaluateViewModel.assessment.value = ""
            }else {
                evaluateViewModel.assessment.value = "${assessment}"
            }



        }

        navBtn()
        binding.nextPage.setOnClickListener {
            navController.navigate(R.id.action_evaluateLifeQualityFragment_to_evaluatePastMedicalFragment)
            if (!evaluateViewModel.osa.value.equals("") && evaluateViewModel.psq.value.equals("")) {
                assessment.add(evaluateViewModel.osa.value!!)
            } else if (!evaluateViewModel.psq.value.equals("") && evaluateViewModel.osa.value.equals(
                    ""
                )
            ) {
                assessment.add(evaluateViewModel.psq.value!!)
            } else if (!evaluateViewModel.psq.value.equals("") && !evaluateViewModel.osa.value.equals(
                    ""
                )
            ) {
                assessment.add(evaluateViewModel.osa.value!!)
                assessment.add(evaluateViewModel.psq.value!!)
            }


            if (assessment.size == 0){
                evaluateViewModel.assessment.value = ""
            }else {
                evaluateViewModel.assessment.value = "${assessment}"
            }

            addEvaluateBody.noise = evaluateViewModel.noise.value!!
            addEvaluateBody.lamp = evaluateViewModel.lamp.value!!
            addEvaluateBody.crowd = evaluateViewModel.crowd.value!!
            addEvaluateBody.smoke = evaluateViewModel.smoke.value!!
            addEvaluateBody.assessment = evaluateViewModel.assessment.value!!

//            onOption()


//            val map = getMap()
//            val url = OkhttpSingleton.BASE_URL+"/v2/estimateSave"
//            Log.d("evaluatepost", "onResponse:$map ")
//
//
//            if (evaluateViewModel.noOption1.value == 0 && evaluateViewModel.noOption2.value == 0 && evaluateViewModel.noOption3.value == 0){
//                ToastUtils.showTextToast(requireContext(),"未填任何选项")
//                return@setOnClickListener
//            }
//            if (evaluateViewModel.height.value.equals("0")){
//                ToastUtils.showTextToast(requireContext(),"身高必须为正数")
//                return@setOnClickListener
//            }
//
//            if (evaluateViewModel.weight.value.equals("0")){
//                ToastUtils.showTextToast(requireContext(),"体重必须为正数")
//                return@setOnClickListener
//            }
//
//            if (evaluateViewModel.neck.value.equals("0")){
//                ToastUtils.showTextToast(requireContext(),"颈围必须为正数")
//                return@setOnClickListener
//            }
//
//            if (evaluateViewModel.npc.value.equals("0")){
//                ToastUtils.showTextToast(requireContext(),"鼻咽侧位必须为正数")
//                return@setOnClickListener
//            }



//                postAddEvaluate(url,map)



//            postAddEvaluate(retrofitSingleton, addEvaluateBody)
//            navController.navigate(R.id.action_evaluateLifeQualityFragment_to_evaluateTreatmentPlanFragment)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            4 -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        evaluateViewModel.osa.value = "{\"id\":${
                            data?.getIntExtra(
                                "id",
                                0
                            )
                        }, \"name\":\"${data?.getStringExtra("name")}\", \"score\":${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }, \"result\": \"${data?.getStringExtra("result")}\"}"
                        evaluateViewModel.osa1.value = "得分：${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }分；结果：${data?.getStringExtra("result")}"


                    }

                }

            }
            5 -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        evaluateViewModel.psq.value = "{\"id\":${
                            data?.getIntExtra(
                                "id",
                                0
                            )
                        }, \"name\":\"${data?.getStringExtra("name")}\", \"score\":${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }, \"result\": \"${data?.getStringExtra("result")}\"}"
                        evaluateViewModel.psq1.value = "得分：${
                            data?.getIntExtra(
                                "score",
                                0
                            )
                        }分；结果：${data?.getStringExtra("result")}"

                    }
                }

            }
            else -> {
                Log.d("DeviceFragment", "onActivityResult:没刷新 ")
            }
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

    fun setOsaResult() {
        evaluateViewModel.osa1.observe(viewLifecycleOwner, Observer {
            if (!it.equals("")) {
                binding.osaScore.text = "${it}                                       "
                binding.osaButton.text = "重新评测"
            } else {
                binding.osaScore.text = it
            }

        })
    }

    fun setPsqResult() {
        evaluateViewModel.psq1.observe(viewLifecycleOwner, Observer {
            if (!it.equals("")) {
                binding.psqScore.text = "${it}                                       "
                binding.psqButton.text = "重新评测"
            } else {
                binding.psqScore.text = it
            }
        })
    }

    fun setLine1() {
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )

        evaluateViewModel.noise.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
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

            binding.b11.setOnClickListener { evaluateViewModel.noise.value = 2 }
            binding.b12.setOnClickListener { evaluateViewModel.noise.value = 1 }
        })
    }

    fun setLine2() {
        val map = mapOf(
            1 to binding.b21,
            2 to binding.b22
        )

        evaluateViewModel.lamp.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
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

            binding.b21.setOnClickListener { evaluateViewModel.lamp.value = 2 }
            binding.b22.setOnClickListener { evaluateViewModel.lamp.value = 1 }
        })
    }

    fun setLine3() {
        val map = mapOf(
            1 to binding.b31,
            2 to binding.b32
        )

        evaluateViewModel.crowd.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
                2 -> {
                    binding.b31.isSelected = true
                    binding.b31.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b32.isSelected = true
                    binding.b32.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b31.setOnClickListener { evaluateViewModel.crowd.value = 2 }
            binding.b32.setOnClickListener { evaluateViewModel.crowd.value = 1 }
        })
    }

    fun setLine4() {
        val map = mapOf(
            1 to binding.b41,
            2 to binding.b42
        )

        evaluateViewModel.smoke.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when (it) {
                2 -> {
                    binding.b41.isSelected = true
                    binding.b41.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b42.isSelected = true
                    binding.b42.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }

            binding.b41.setOnClickListener { evaluateViewModel.smoke.value = 2 }
            binding.b42.setOnClickListener { evaluateViewModel.smoke.value = 1 }
        })
    }


}