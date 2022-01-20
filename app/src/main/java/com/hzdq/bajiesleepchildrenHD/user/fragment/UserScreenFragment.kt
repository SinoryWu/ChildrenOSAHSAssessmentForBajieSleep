package com.hzdq.bajiesleepchildrenHD.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentUserBinding
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentUserScreenBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2


class UserScreenFragment : Fragment() {

    private lateinit var binding:FragmentUserScreenBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_screen,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userScreenOSA.setOnClickListener {
           navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_userScreenFragment_to_userAddScreenFragment)

        }

        binding.userScreenBack.setOnClickListener {
            ActivityCollector2.removeActivity(requireActivity())
            requireActivity().finish()
        }
    }


}