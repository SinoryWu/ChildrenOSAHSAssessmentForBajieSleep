package com.hzdq.bajiesleepchildrenHD.device.devicefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentChoiceDeviceBinding

import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel


class ChoiceDeviceFragment : Fragment() {


    private lateinit var binding: FragmentChoiceDeviceBinding
    private lateinit var navController: NavController
    private var tokenDialog: TokenDialog? = null
    private lateinit var viewModel: DeviceViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choice_device, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(
            DeviceViewModel::class.java
        )


        binding.choiceDeviceBack.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigateUp()
        }





//        ToastUtils.showTextToast(requireContext(),"${viewModel?.a}")
    }

}