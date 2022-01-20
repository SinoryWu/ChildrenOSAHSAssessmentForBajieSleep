package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.hzdq.bajiesleepchildrenHD.MainActivity
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluatePastMedicalBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.MessageDateClass
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import com.hzdq.bajiesleepchildrenHD.utils.clickDelay
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

/**
 * 既往病史
 */
class EvaluatePastMedicalFragment : Fragment() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var binding:FragmentEvaluatePastMedicalBinding
    private lateinit var navController: NavController
    private lateinit var shp: Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate_past_medical, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)

        binding.frontPage.clickDelay {
            navController.navigate(R.id.action_evaluatePastMedicalFragment_to_evaluateLifeQualityFragment)
        }
        shp = Shp(requireContext())


        navBtn()
        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })
        setLine1()
        setLine2()
        setLine3()
        setLine4()
        setLine5()
        setLine6()
        setLine7()
        setLine8()
        setLine9()
        setLine10()
        setLine11()
        setLine12()
        binding.nextPage.clickDelay {
            navController.navigate(R.id.action_evaluatePastMedicalFragment_to_evaluateInspectFragment)
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

//    fun addEvaluate(url:String,map: Map<String,String>){
//        //1.拿到okhttp对象
//
//
//        //2.构造request
//        //2.1构造requestbody
//        val params = HashMap<String?, Any?>()
//        Log.e("params:", params.toString())
//        val keys: Set<String> = map.keys
//        for (key in keys) {
//            params[key] = map[key]
//        }
//        val jsonObject = JSONObject(params)
//        val jsonStr = jsonObject.toString()
//        val requestBodyJson =
//            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr)
////        Log.d("useragent", GetShp.getUserAgent(applicationContext))
//        val request = Request.Builder()
//            .addHeader("User-Agent", shp.getUserAgent())
//            .addHeader("token",shp.getToken())
//            .addHeader("uid",shp.getUid())
//            .url(url)
//            .post(requestBodyJson)
//            .build()
//        //3.将request封装为call
//        val call = OkhttpSingleton.ok()?.newCall(request)
//
//        //4.执行call
////        同步执行
////        Response response = call.execute();
//
//        //异步执行
//        call?.enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//
//                runOnUiThread { ToastUtils.showTextToast2(this@LoginActivity, "网络请求失败") }
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//
//                val res = response.body()!!.string()
//                runOnUiThread {
//                    //                        L.e(res);
//                    val gson = Gson()
//                    val messageDateClass: MessageDateClass =
//                        gson.fromJson(res, MessageDateClass::class.java)
//                    if (messageDateClass.code == 0) {
//                        messageDateClass.data.token.let { it1 ->
//                            shp.saveToSp(
//                                "token",
//                                it1
//                            )
//                        }
//                        shp.saveToSp("uid", "${messageDateClass.data.uid}")
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//
//                    } else {
//                        val msg: String = messageDateClass.msg
//                        ToastUtils.showTextToast2(this@LoginActivity, msg)
//                    }
//                }
//            }
//        })
//    }

    private fun setLine1(){
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12
        )
        evaluateViewModel.rhinitis.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b11.setOnClickListener { evaluateViewModel.rhinitis.value = 2 }
        binding.b12.setOnClickListener { evaluateViewModel.rhinitis.value = 1 }
    }

    private fun setLine2(){
        val map = mapOf(
            1 to binding.b21,
            2 to binding.b22
        )
        evaluateViewModel.asthma.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b21.setOnClickListener { evaluateViewModel.asthma.value = 2 }
        binding.b22.setOnClickListener { evaluateViewModel.asthma.value = 1 }
    }

    private fun setLine3(){
        val map = mapOf(
            1 to binding.b31,
            2 to binding.b32
        )
        evaluateViewModel.eczema.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b31.setOnClickListener { evaluateViewModel.eczema.value = 2 }
        binding.b32.setOnClickListener { evaluateViewModel.eczema.value = 1 }
    }

    private fun setLine4(){
        val map = mapOf(
            1 to binding.b41,
            2 to binding.b42
        )
        evaluateViewModel.urticaria.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b41.setOnClickListener { evaluateViewModel.urticaria.value = 2 }
        binding.b42.setOnClickListener { evaluateViewModel.urticaria.value = 1 }
    }

    private fun setLine5(){
        val map = mapOf(
            1 to binding.b51,
            2 to binding.b52
        )
        evaluateViewModel.tnb.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b51.isSelected = true
                    binding.b51.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b52.isSelected = true
                    binding.b52.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b51.setOnClickListener { evaluateViewModel.tnb.value = 2 }
        binding.b52.setOnClickListener { evaluateViewModel.tnb.value = 1 }
    }

    private fun setLine6(){
        val map = mapOf(
            1 to binding.b61,
            2 to binding.b62
        )
        evaluateViewModel.mdd.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b61.setOnClickListener { evaluateViewModel.mdd.value = 2 }
        binding.b62.setOnClickListener { evaluateViewModel.mdd.value = 1 }
    }
    private fun setLine7(){
        val map = mapOf(
            1 to binding.b71,
            2 to binding.b72
        )
        evaluateViewModel.ekc.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b71.setOnClickListener { evaluateViewModel.ekc.value = 2 }
        binding.b72.setOnClickListener { evaluateViewModel.ekc.value = 1 }
    }

    private fun setLine8(){
        val map = mapOf(
            1 to binding.b81,
            2 to binding.b82
        )
        evaluateViewModel.ihb.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
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
        })

        binding.b81.setOnClickListener { evaluateViewModel.ihb.value = 2 }
        binding.b82.setOnClickListener { evaluateViewModel.ihb.value = 1 }
    }

    private fun setLine9(){
        val map = mapOf(
            1 to binding.b91,
            2 to binding.b92
        )
        evaluateViewModel.thyroid.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b91.isSelected = true
                    binding.b91.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b92.isSelected = true
                    binding.b92.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b91.setOnClickListener { evaluateViewModel.thyroid.value = 2 }
        binding.b92.setOnClickListener { evaluateViewModel.thyroid.value = 1 }
    }

    private fun setLine10(){
        val map = mapOf(
            1 to binding.b101,
            2 to binding.b102
        )
        evaluateViewModel.fat.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b101.isSelected = true
                    binding.b101.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b102.isSelected = true
                    binding.b102.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b101.setOnClickListener { evaluateViewModel.fat.value = 2 }
        binding.b102.setOnClickListener { evaluateViewModel.fat.value = 1 }
    }

    private fun setLine11(){
        val map = mapOf(
            1 to binding.b111,
            2 to binding.b112
        )
        evaluateViewModel.tonsils.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b111.isSelected = true
                    binding.b111.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b112.isSelected = true
                    binding.b112.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b111.setOnClickListener { evaluateViewModel.tonsils.value = 2 }
        binding.b112.setOnClickListener { evaluateViewModel.tonsils.value = 1 }
    }

    private fun setLine12(){
        val map = mapOf(
            1 to binding.b121,
            2 to binding.b122
        )
        evaluateViewModel.adenoid.observe(viewLifecycleOwner, Observer {
            map.forEach {
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b121.isSelected = true
                    binding.b121.setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    binding.b122.isSelected = true
                    binding.b122.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    map.forEach {
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b121.setOnClickListener { evaluateViewModel.adenoid.value = 2 }
        binding.b122.setOnClickListener { evaluateViewModel.adenoid.value = 1 }
    }



}