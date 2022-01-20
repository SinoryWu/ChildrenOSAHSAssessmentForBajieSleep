package com.hzdq.bajiesleepchildrenHD.home.fragment

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
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentHomeAddUserCompleteBinding
import com.hzdq.bajiesleepchildrenHD.evaluate.activity.EvaluateActivity
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2


class HomeAddUserCompleteFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeAddUserCompleteBinding
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private var tokenDialog: TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home_add_user_complete,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBindDeviceViewModel = ViewModelProvider(requireActivity()).get(HomeBindDeviceViewModel::class.java)
        navController = Navigation.findNavController(view)


        binding.back.setOnClickListener {
            requireActivity().finish()
        }

        binding.buttonComplete.setOnClickListener {
            requireActivity().finish()
        }

        binding.buttonBindDevice.setOnClickListener {
            navController.navigate(R.id.action_homeAddUserCompleteFragment_to_homeBindDeviceDeviceFragment2)
        }


        binding.buttonEvaluate.setOnClickListener {
            val intent = Intent(requireActivity(), EvaluateActivity::class.java)
            intent.putExtra("patientid",  homeBindDeviceViewModel.uid.value)
            intent.putExtra("name",  homeBindDeviceViewModel.name.value)

            startActivity(intent)
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()

        }

    }


}