package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

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
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateFamilyBinding
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.utils.clickDelay

class EvaluateFamilyFragment : Fragment() {
    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:FragmentEvaluateFamilyBinding
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate_family, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)
        navController = Navigation.findNavController(view)

        evaluateViewModel.name.observe(requireActivity(), Observer {
            binding.name.text = it
        })

        binding.frontPage.clickDelay {
            navController.navigate(R.id.action_evaluateFamilyFragment_to_evaluateBaseInfoFragment)
        }

        setLine1()
        setLine2()
        setLine3()
        setLine4()
        setLine5()
        setLine6()
        setLine7()
        setLine8()


        binding.nextPage.clickDelay {
            evaluateViewModel.fhs.value =
                "{\"打鼾-祖父\":${evaluateViewModel.f11.value},\"打鼾-祖母\":${evaluateViewModel.f12.value},\"打鼾-外公\":${evaluateViewModel.f13.value},\"打鼾-外婆\":${evaluateViewModel.f14.value},\"打鼾-父亲\":${evaluateViewModel.f15.value},\"打鼾-母亲\":${evaluateViewModel.f16.value},\"打鼾-兄弟\":${evaluateViewModel.f17.value},\"打鼾-姐妹\":${evaluateViewModel.f18.value}," +
                "\"肥胖-祖父\":${evaluateViewModel.f21.value},\"肥胖-祖母\":${evaluateViewModel.f22.value},\"肥胖-外公\":${evaluateViewModel.f23.value},\"肥胖-外婆\":${evaluateViewModel.f24.value},\"肥胖-父亲\":${evaluateViewModel.f25.value},\"肥胖-母亲\":${evaluateViewModel.f26.value},\"肥胖-兄弟\":${evaluateViewModel.f27.value},\"肥胖-姐妹\":${evaluateViewModel.f28.value}," +
                "\"高血脂-祖父\":${evaluateViewModel.f31.value},\"高血脂-祖母\":${evaluateViewModel.f32.value},\"高血脂-外公\":${evaluateViewModel.f33.value},\"高血脂-外婆\":${evaluateViewModel.f34.value},\"高血脂-父亲\":${evaluateViewModel.f35.value},\"高血脂-母亲\":${evaluateViewModel.f36.value},\"高血脂-兄弟\":${evaluateViewModel.f37.value},\"高血脂-姐妹\":${evaluateViewModel.f38.value}," +
                "\"高血压-祖父\":${evaluateViewModel.f41.value},\"高血压-祖母\":${evaluateViewModel.f42.value},\"高血压-外公\":${evaluateViewModel.f43.value},\"高血压-外婆\":${evaluateViewModel.f44.value},\"高血压-父亲\":${evaluateViewModel.f45.value},\"高血压-母亲\":${evaluateViewModel.f46.value},\"高血压-兄弟\":${evaluateViewModel.f47.value},\"高血压-姐妹\":${evaluateViewModel.f48.value}," +
                "\"糖尿病-祖父\":${evaluateViewModel.f51.value},\"糖尿病-祖母\":${evaluateViewModel.f52.value},\"糖尿病-外公\":${evaluateViewModel.f53.value},\"糖尿病-外婆\":${evaluateViewModel.f54.value},\"糖尿病-父亲\":${evaluateViewModel.f55.value},\"糖尿病-母亲\":${evaluateViewModel.f56.value},\"糖尿病-兄弟\":${evaluateViewModel.f57.value},\"糖尿病-姐妹\":${evaluateViewModel.f58.value}," +
                "\"肿瘤-祖父\":${evaluateViewModel.f61.value},\"肿瘤-祖母\":${evaluateViewModel.f62.value},\"肿瘤-外公\":${evaluateViewModel.f63.value},\"肿瘤-外婆\":${evaluateViewModel.f64.value},\"肿瘤-父亲\":${evaluateViewModel.f65.value},\"肿瘤-母亲\":${evaluateViewModel.f66.value},\"肿瘤-兄弟\":${evaluateViewModel.f67.value},\"肿瘤-姐妹\":${evaluateViewModel.f68.value}," +
                "\"精神病-祖父\":${evaluateViewModel.f71.value},\"精神病-祖母\":${evaluateViewModel.f72.value},\"精神病-外公\":${evaluateViewModel.f73.value},\"精神病-外婆\":${evaluateViewModel.f74.value},\"精神病-父亲\":${evaluateViewModel.f75.value},\"精神病-母亲\":${evaluateViewModel.f76.value},\"精神病-兄弟\":${evaluateViewModel.f77.value},\"精神病-姐妹\":${evaluateViewModel.f78.value}," +
                "\"家族遗传病-祖父\":${evaluateViewModel.f81.value},\"家族遗传病-祖母\":${evaluateViewModel.f82.value},\"家族遗传病-外公\":${evaluateViewModel.f83.value},\"家族遗传病-外婆\":${evaluateViewModel.f84.value},\"家族遗传病-父亲\":${evaluateViewModel.f85.value},\"家族遗传病-母亲\":${evaluateViewModel.f86.value},\"家族遗传病-兄弟\":${evaluateViewModel.f87.value},\"家族遗传病-姐妹\":${evaluateViewModel.f88.value}}"


            navController.navigate(R.id.action_evaluateFamilyFragment_to_evaluateLifeQualityFragment)













        }

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
   private fun setLine1(){
        binding.c11.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f11.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f11.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f11.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c11.isChecked = true
                0 -> binding.c11.isChecked = false
            }
        })


        binding.c12.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f12.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f12.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f12.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c12.isChecked = true
                0 -> binding.c12.isChecked = false
            }
        })

        binding.c13.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f13.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f13.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f13.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c13.isChecked = true
                0 -> binding.c13.isChecked = false
            }
        })

        binding.c14.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f14.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f14.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f14.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c14.isChecked = true
                0 -> binding.c14.isChecked = false
            }
        })

        binding.c15.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f15.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f15.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f15.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c15.isChecked = true
                0 -> binding.c15.isChecked = false
            }
        })


        binding.c16.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f16.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f16.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f16.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c16.isChecked = true
                0 -> binding.c16.isChecked = false
            }
        })


        binding.c17.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f17.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f17.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f17.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c17.isChecked = true
                0 -> binding.c17.isChecked = false
            }
        })


        binding.c18.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f18.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f18.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f18.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc1()
            when(it){
                1 -> binding.c18.isChecked = true
                0 -> binding.c18.isChecked = false
            }
        })

        evaluateViewModel.fc1.observe(viewLifecycleOwner, Observer {
            binding.t1.text = "$it"
        })



    }

    private fun setLine2(){
        binding.c21.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f21.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f21.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f21.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c21.isChecked = true
                0 -> binding.c21.isChecked = false
            }
        })


        binding.c22.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f22.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f22.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f22.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c22.isChecked = true
                0 -> binding.c22.isChecked = false
            }
        })

        binding.c23.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f23.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f23.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f23.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c23.isChecked = true
                0 -> binding.c23.isChecked = false
            }
        })

        binding.c24.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f24.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f24.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f24.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c24.isChecked = true
                0 -> binding.c24.isChecked = false
            }
        })

        binding.c25.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f25.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f25.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f25.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c25.isChecked = true
                0 -> binding.c25.isChecked = false
            }
        })


        binding.c26.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f26.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f26.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f26.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c26.isChecked = true
                0 -> binding.c26.isChecked = false
            }
        })


        binding.c27.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f27.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f27.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f27.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c27.isChecked = true
                0 -> binding.c27.isChecked = false
            }
        })


        binding.c28.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f28.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f28.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f28.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc2()
            when(it){
                1 -> binding.c28.isChecked = true
                0 -> binding.c28.isChecked = false
            }
        })

        evaluateViewModel.fc2.observe(viewLifecycleOwner, Observer {
            binding.t2.text = "$it"
        })



    }

    private fun setLine3(){
        binding.c31.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f31.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f31.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f31.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c31.isChecked = true
                0 -> binding.c31.isChecked = false
            }
        })


        binding.c32.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f32.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f32.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f32.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c32.isChecked = true
                0 -> binding.c32.isChecked = false
            }
        })

        binding.c33.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f33.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f33.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f33.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c33.isChecked = true
                0 -> binding.c33.isChecked = false
            }
        })

        binding.c34.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f34.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f34.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f34.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c34.isChecked = true
                0 -> binding.c34.isChecked = false
            }
        })

        binding.c35.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f35.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f35.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f35.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c35.isChecked = true
                0 -> binding.c35.isChecked = false
            }
        })


        binding.c36.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f36.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f36.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f36.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c36.isChecked = true
                0 -> binding.c36.isChecked = false
            }
        })


        binding.c37.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f37.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f37.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f37.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c37.isChecked = true
                0 -> binding.c37.isChecked = false
            }
        })


        binding.c38.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f38.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f38.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f38.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc3()
            when(it){
                1 -> binding.c38.isChecked = true
                0 -> binding.c38.isChecked = false
            }
        })

        evaluateViewModel.fc3.observe(viewLifecycleOwner, Observer {
            binding.t3.text = "$it"
        })



    }

    private fun setLine4(){
        binding.c41.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f41.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f41.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f41.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c41.isChecked = true
                0 -> binding.c41.isChecked = false
            }
        })


        binding.c42.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f42.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f42.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f42.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c42.isChecked = true
                0 -> binding.c42.isChecked = false
            }
        })

        binding.c43.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f43.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f43.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f43.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c43.isChecked = true
                0 -> binding.c43.isChecked = false
            }
        })

        binding.c44.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f44.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f44.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f44.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c44.isChecked = true
                0 -> binding.c44.isChecked = false
            }
        })

        binding.c45.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f45.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f45.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f45.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c45.isChecked = true
                0 -> binding.c45.isChecked = false
            }
        })


        binding.c46.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f46.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f46.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f46.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c46.isChecked = true
                0 -> binding.c46.isChecked = false
            }
        })


        binding.c47.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f47.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f47.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f47.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c47.isChecked = true
                0 -> binding.c47.isChecked = false
            }
        })


        binding.c48.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f48.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f48.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f48.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc4()
            when(it){
                1 -> binding.c48.isChecked = true
                0 -> binding.c48.isChecked = false
            }
        })

        evaluateViewModel.fc4.observe(viewLifecycleOwner, Observer {
            binding.t4.text = "$it"
        })



    }

    private fun setLine5(){
        binding.c51.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f51.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f51.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f51.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c51.isChecked = true
                0 -> binding.c51.isChecked = false
            }
        })


        binding.c52.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f52.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f52.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f52.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c52.isChecked = true
                0 -> binding.c52.isChecked = false
            }
        })

        binding.c53.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f53.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f53.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f53.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c53.isChecked = true
                0 -> binding.c53.isChecked = false
            }
        })

        binding.c54.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f54.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f54.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f54.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c54.isChecked = true
                0 -> binding.c54.isChecked = false
            }
        })

        binding.c55.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f55.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f55.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f55.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c55.isChecked = true
                0 -> binding.c55.isChecked = false
            }
        })


        binding.c56.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f56.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f56.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f56.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c56.isChecked = true
                0 -> binding.c56.isChecked = false
            }
        })


        binding.c57.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f57.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f57.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f57.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c57.isChecked = true
                0 -> binding.c57.isChecked = false
            }
        })


        binding.c58.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f58.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f58.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f58.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc5()
            when(it){
                1 -> binding.c58.isChecked = true
                0 -> binding.c58.isChecked = false
            }
        })

        evaluateViewModel.fc5.observe(viewLifecycleOwner, Observer {
            binding.t5.text = "$it"
        })



    }


    private fun setLine6(){
        binding.c61.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f61.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f61.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f61.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c61.isChecked = true
                0 -> binding.c61.isChecked = false
            }
        })


        binding.c62.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f62.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f62.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f62.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c62.isChecked = true
                0 -> binding.c62.isChecked = false
            }
        })

        binding.c63.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f63.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f63.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f63.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c63.isChecked = true
                0 -> binding.c63.isChecked = false
            }
        })

        binding.c64.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f64.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f64.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f64.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c64.isChecked = true
                0 -> binding.c64.isChecked = false
            }
        })

        binding.c65.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f65.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f65.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f65.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c65.isChecked = true
                0 -> binding.c65.isChecked = false
            }
        })


        binding.c66.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f66.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f66.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f66.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c66.isChecked = true
                0 -> binding.c66.isChecked = false
            }
        })


        binding.c67.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f67.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f67.value = 0
                evaluateViewModel.noOption.value = 0
            }
        }
        evaluateViewModel.f67.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c67.isChecked = true
                0 -> binding.c67.isChecked = false
            }
        })


        binding.c68.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f68.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f68.value = 0
                evaluateViewModel.noOption.value = 0
            }

        }
        evaluateViewModel.f68.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc6()
            when(it){
                1 -> binding.c68.isChecked = true
                0 -> binding.c68.isChecked = false
            }
        })

        evaluateViewModel.fc6.observe(viewLifecycleOwner, Observer {
            binding.t6.text = "$it"
        })



    }


    private fun setLine7(){
        binding.c71.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f71.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f71.value = 0
            }
        }
        evaluateViewModel.f71.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c71.isChecked = true
                0 -> binding.c71.isChecked = false
            }
        })


        binding.c72.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f72.value = 1
                evaluateViewModel.noOption.value = 1
            }else {
                evaluateViewModel.f72.value = 0
            }
        }
        evaluateViewModel.f72.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c72.isChecked = true
                0 -> binding.c72.isChecked = false
            }
        })

        binding.c73.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f73.value = 1
            }else {
                evaluateViewModel.f73.value = 0
            }
        }
        evaluateViewModel.f73.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c73.isChecked = true
                0 -> binding.c73.isChecked = false
            }
        })

        binding.c74.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f74.value = 1
            }else {
                evaluateViewModel.f74.value = 0
            }
        }
        evaluateViewModel.f74.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c74.isChecked = true
                0 -> binding.c74.isChecked = false
            }
        })

        binding.c75.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f75.value = 1
            }else {
                evaluateViewModel.f75.value = 0
            }
        }
        evaluateViewModel.f75.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c75.isChecked = true
                0 -> binding.c75.isChecked = false
            }
        })


        binding.c76.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f76.value = 1
            }else {
                evaluateViewModel.f76.value = 0
            }
        }
        evaluateViewModel.f76.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c76.isChecked = true
                0 -> binding.c76.isChecked = false
            }
        })


        binding.c77.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f77.value = 1
            }else {
                evaluateViewModel.f77.value = 0
            }
        }
        evaluateViewModel.f77.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c77.isChecked = true
                0 -> binding.c77.isChecked = false
            }
        })


        binding.c78.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f78.value = 1
            }else {
                evaluateViewModel.f78.value = 0
            }

        }
        evaluateViewModel.f78.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc7()
            when(it){
                1 -> binding.c78.isChecked = true
                0 -> binding.c78.isChecked = false
            }
        })

        evaluateViewModel.fc7.observe(viewLifecycleOwner, Observer {
            binding.t7.text = "$it"
        })



    }


    private fun setLine8(){
        binding.c81.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f81.value = 1
            }else {
                evaluateViewModel.f81.value = 0
            }
        }
        evaluateViewModel.f81.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c81.isChecked = true
                0 -> binding.c81.isChecked = false
            }
        })


        binding.c82.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f82.value = 1
            }else {
                evaluateViewModel.f82.value = 0
            }
        }
        evaluateViewModel.f82.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c82.isChecked = true
                0 -> binding.c82.isChecked = false
            }
        })

        binding.c83.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f83.value = 1
            }else {
                evaluateViewModel.f83.value = 0
            }
        }
        evaluateViewModel.f83.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c83.isChecked = true
                0 -> binding.c83.isChecked = false
            }
        })

        binding.c84.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f84.value = 1
            }else {
                evaluateViewModel.f84.value = 0
            }
        }
        evaluateViewModel.f84.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c84.isChecked = true
                0 -> binding.c84.isChecked = false
            }
        })

        binding.c85.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f85.value = 1
            }else {
                evaluateViewModel.f85.value = 0
            }
        }
        evaluateViewModel.f85.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c85.isChecked = true
                0 -> binding.c85.isChecked = false
            }
        })


        binding.c86.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f86.value = 1
            }else {
                evaluateViewModel.f86.value = 0
            }
        }
        evaluateViewModel.f86.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c86.isChecked = true
                0 -> binding.c86.isChecked = false
            }
        })


        binding.c87.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f87.value = 1
            }else {
                evaluateViewModel.f87.value = 0
            }
        }
        evaluateViewModel.f87.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c87.isChecked = true
                0 -> binding.c87.isChecked = false
            }
        })


        binding.c88.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                evaluateViewModel.f88.value = 1
            }else {
                evaluateViewModel.f88.value = 0
            }

        }
        evaluateViewModel.f88.observe(viewLifecycleOwner, Observer {
            evaluateViewModel.setfc8()
            when(it){
                1 -> binding.c88.isChecked = true
                0 -> binding.c88.isChecked = false
            }
        })

        evaluateViewModel.fc8.observe(viewLifecycleOwner, Observer {
            binding.t8.text = "$it"
        })



    }




}