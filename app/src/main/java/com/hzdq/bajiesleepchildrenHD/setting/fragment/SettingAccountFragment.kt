package com.hzdq.bajiesleepchildrenHD.setting.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentSettingAccountBinding
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentSettingBaseBinding
import com.hzdq.bajiesleepchildrenHD.setting.activity.SettingAddUserActivity
import com.hzdq.bajiesleepchildrenHD.setting.adapter.SettingAccountAdapter
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel

const val SETTING_ADD_USER = 11;
class SettingAccountFragment : Fragment() {

    private lateinit var binding:FragmentSettingAccountBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting_account, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        val settingAccountAdapter = SettingAccountAdapter()
        settingViewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        binding.recyclerView.apply {
            adapter = settingAccountAdapter
            layoutManager = linearLayoutManager
        }

        settingViewModel.list.observe(requireActivity(), Observer {
            settingAccountAdapter.notifyDataSetChanged()
            settingAccountAdapter.submitList(it)

        })


        binding.addUser.setOnClickListener {
            startActivityForResult(Intent(requireActivity(),SettingAddUserActivity::class.java), SETTING_ADD_USER)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SETTING_ADD_USER -> {
                if(resultCode == AppCompatActivity.RESULT_OK){
                   settingViewModel.refreshAccount.value = 1
                }
            }
            else -> {
                Log.d("onActivityResult", "onActivityResult: 没刷新")
            }
        }

    }
}