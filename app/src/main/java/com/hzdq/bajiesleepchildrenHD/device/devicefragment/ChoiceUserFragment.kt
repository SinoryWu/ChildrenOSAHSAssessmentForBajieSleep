package com.hzdq.bajiesleepchildrenHD.device.devicefragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.MainViewModel
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentChoiceDeviceBinding
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentChoiceUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceUserAdapter
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.utils.HideKeyboard
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChoiceUserFragment : Fragment() {

    private lateinit var binding:FragmentChoiceUserBinding
    private lateinit var navController:NavController
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private lateinit var homeBindDeviceUserAdapter: HomeBindDeviceUserAdapter
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp: Shp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_choice_user,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel::class.java)
        binding.choiceUserBack.setOnClickListener {

            navController.navigateUp()
        }

        shp = Shp(requireContext())

        homeBindDeviceViewModel = ViewModelProvider(requireActivity()).get(HomeBindDeviceViewModel::class.java)
        homeBindDeviceUserAdapter = HomeBindDeviceUserAdapter(homeBindDeviceViewModel,requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.choiceUserList.apply {
            adapter = homeBindDeviceUserAdapter
            layoutManager = linearLayout
        }
        homeBindDeviceViewModel.homeBindDeviceUserListLiveData.observe(requireActivity(), Observer {

            homeBindDeviceUserAdapter.submitList(it)

            if (homeBindDeviceViewModel.needToScrollToTopBindUser) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                homeBindDeviceUserAdapter.notifyDataSetChanged()
                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0, 0)
                }

                homeBindDeviceViewModel.needToScrollToTopBindUser = false
            }

        })

        //下滑刷新重新请求列表
        binding.choiceUserListRefresh.setOnRefreshListener {

            homeBindDeviceViewModel.resetHomeDeviceUserQuery()
        }
        homeBindDeviceViewModel.homeBindDeviceUserNetWorkStatus.observe(requireActivity(), Observer {

            if (it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                homeBindDeviceViewModel.needToScrollToTopBindUser = true
            }
            homeBindDeviceUserAdapter.updateNetWorkStatus(it)
            binding.choiceUserListRefresh.isRefreshing =
                it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_LOADING
        })

        homeBindDeviceUserAdapter.setOnItemClickListener(object :HomeBindDeviceUserAdapter.OnItemClickListener{
            override fun onItemClick(dataItem: DataHomeBindDeviceUserX, itemView: View) {
                if (dataItem.using == true){
                    ToastUtils.showTextToast(requireContext(),"此用户已绑定设备")
                }else {
                    homeBindDeviceViewModel.uid.value = dataItem.uid
                    deviceViewModel.userName.value = dataItem.truename
                    deviceViewModel.uid.value = dataItem.uid.toString()
                    navController.navigateUp()

                }
            }

        })

        binding.choiceUserSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                HideKeyboard.hideKeyboard(v,requireContext())
                shp.saveToSp("homebinddeviceuserkeyword",binding.choiceUserSearch.text.toString())
                homeBindDeviceUserAdapter.notifyDataSetChanged()
                homeBindDeviceViewModel.resetHomeDeviceUserQuery()
            }
            false
        }
        binding.choiceUserSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    shp.saveToSp("homebinddeviceuserkeyword",s.toString())
                    homeBindDeviceUserAdapter.notifyDataSetChanged()
                    homeBindDeviceViewModel.resetHomeDeviceUserQuery()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })





    }

    override fun onDestroyView() {
        super.onDestroyView()
        shp.saveToSp("homebinddeviceuserkeyword","")

    }



}