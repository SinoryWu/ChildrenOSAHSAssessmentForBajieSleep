package com.hzdq.bajiesleepchildrenHD.evaluate.fragment

import android.graphics.Color
import android.os.Bundle
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
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentEvaluateBaseInfoBinding
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.utils.clickDelay


class EvaluateBaseInfoFragment : Fragment() {

    private var tokenDialog: TokenDialog? = null
    private lateinit var binding:FragmentEvaluateBaseInfoBinding
    private lateinit var evaluateViewModel:EvaluateViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_evaluate_base_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        evaluateViewModel = ViewModelProvider(requireActivity()).get(EvaluateViewModel::class.java)
        navController = Navigation.findNavController(view)
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

        val map = mapOf(
            1 to evaluateViewModel.sleep.value,
            2 to evaluateViewModel.pih.value,
            3 to evaluateViewModel.dm.value,
            4 to evaluateViewModel.mode.value,
            5 to evaluateViewModel.birth.value,
            6 to evaluateViewModel.pomr.value,
            7 to evaluateViewModel.cmv.value
        )
        binding.nextPage.clickDelay {
            navController.navigate(R.id.action_evaluateBaseInfoFragment_to_evaluateFamilyFragment)
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

    fun setLine1(){
        val map = mapOf(
            1 to binding.b11,
            2 to binding.b12,
            3 to binding.b13,
            4 to binding.b14
        )
        evaluateViewModel.sleep.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 ->{
                    binding.b11.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))

                    }
                }

                2 ->{
                    binding.b12.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))

                    }
                }

                3 ->{
                    binding.b13.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))

                    }
                }
                4 ->{
                    binding.b14.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))

                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })


        binding.b11.setOnClickListener { evaluateViewModel.sleep.value = 1
        evaluateViewModel.noOption.value = 1}
        binding.b12.setOnClickListener { evaluateViewModel.sleep.value = 2
            evaluateViewModel.noOption.value = 1}
        binding.b13.setOnClickListener { evaluateViewModel.sleep.value = 3
            evaluateViewModel.noOption.value = 1}
        binding.b14.setOnClickListener { evaluateViewModel.sleep.value = 4
            evaluateViewModel.noOption.value = 1}
    }

    fun setLine2(){
        val map = mapOf(
            1 to binding.b21,
            2 to binding.b22
        )

        evaluateViewModel.pih.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b21.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                1 ->{
                    binding.b22.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b21.setOnClickListener { evaluateViewModel.pih.value = 2
            evaluateViewModel.noOption.value = 1}
        binding.b22.setOnClickListener { evaluateViewModel.pih.value = 1
            evaluateViewModel.noOption.value = 1}
    }

    fun setLine3(){
        val map = mapOf(
            1 to binding.b31,
            2 to binding.b32
        )

        evaluateViewModel.dm.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                2 ->{
                    binding.b31.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                1 ->{
                    binding.b32.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })
        binding.b31.setOnClickListener { evaluateViewModel.dm.value = 2
            evaluateViewModel.noOption.value = 1}
        binding.b32.setOnClickListener { evaluateViewModel.dm.value = 1
            evaluateViewModel.noOption.value = 1}

    }


    fun setLine4(){
        val map = mapOf(
            1 to binding.b41,
            2 to binding.b42,
            3 to binding.b43,
            4 to binding.b44,
            5 to binding.b45
        )
        evaluateViewModel.mode.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 ->{
                    binding.b41.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                2 ->{
                    binding.b42.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                3 ->{
                    binding.b43.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                4 ->{
                    binding.b44.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                5 ->{
                    binding.b45.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })


        binding.b41.setOnClickListener { evaluateViewModel.mode.value = 1
            evaluateViewModel.noOption.value = 1}
        binding.b42.setOnClickListener { evaluateViewModel.mode.value = 2
            evaluateViewModel.noOption.value = 1}
        binding.b43.setOnClickListener { evaluateViewModel.mode.value = 3
            evaluateViewModel.noOption.value = 1}
        binding.b44.setOnClickListener { evaluateViewModel.mode.value = 4
            evaluateViewModel.noOption.value = 1}
        binding.b45.setOnClickListener { evaluateViewModel.mode.value = 5
            evaluateViewModel.noOption.value = 1}

    }


    fun setLine5(){
        val map = mapOf(
            1 to binding.b51,
            2 to binding.b52,
            3 to binding.b53,
            4 to binding.b54
        )
        evaluateViewModel.birth.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 ->{
                    binding.b51.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                2 ->{
                    binding.b52.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                3 ->{
                    binding.b53.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                4 ->{
                    binding.b54.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })


        binding.b51.setOnClickListener { evaluateViewModel.birth.value = 1
            evaluateViewModel.noOption.value = 1}
        binding.b52.setOnClickListener { evaluateViewModel.birth.value = 2
            evaluateViewModel.noOption.value = 1}
        binding.b53.setOnClickListener { evaluateViewModel.birth.value = 3
            evaluateViewModel.noOption.value = 1}
        binding.b54.setOnClickListener { evaluateViewModel.birth.value = 4
            evaluateViewModel.noOption.value = 1}


    }

    fun setLine6(){
        val map = mapOf(
            1 to binding.b61,
            2 to binding.b62
        )

        evaluateViewModel.pomr.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
                binding.l7.visibility = View.GONE
            }
            when(it){
                1 ->{
                    binding.b61.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.l7.visibility = View.GONE
                    }
                }

                2 ->{
                    binding.b62.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.l7.visibility = View.VISIBLE
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                        binding.l7.visibility = View.GONE
                    }
                }
            }
        })

        binding.b61.setOnClickListener { evaluateViewModel.pomr.value = 1
         evaluateViewModel.cmv.value = 0
            evaluateViewModel.noOption.value = 1}
        binding.b62.setOnClickListener { evaluateViewModel.pomr.value = 2
            evaluateViewModel.noOption.value = 1}


    }

    fun setLine7(){
        val map = mapOf(
            1 to binding.b71,
            2 to binding.b72
        )

        evaluateViewModel.cmv.observe(viewLifecycleOwner, Observer {
            map.forEach{
                it.value.isSelected = false
                it.value.setTextColor(Color.parseColor("#C1C1C1"))
            }
            when(it){
                1 ->{
                    binding.b71.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                2 ->{
                    binding.b72.also {
                        it.isSelected = true
                        it.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                else  -> {
                    map.forEach{
                        it.value.isSelected = false
                        it.value.setTextColor(Color.parseColor("#C1C1C1"))
                    }
                }
            }
        })

        binding.b71.setOnClickListener { evaluateViewModel.cmv.value = 1
            evaluateViewModel.noOption.value = 1}
        binding.b72.setOnClickListener { evaluateViewModel.cmv.value = 2
            evaluateViewModel.noOption.value = 1}


    }
}