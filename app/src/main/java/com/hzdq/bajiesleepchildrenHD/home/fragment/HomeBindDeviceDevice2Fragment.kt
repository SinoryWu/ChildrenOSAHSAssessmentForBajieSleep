package com.hzdq.bajiesleepchildrenHD.home.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentHomeBindDeviceDeviceBinding

import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceDeviceAdapter
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeBindDeviceDevice2Fragment : Fragment() {
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private lateinit var binding: FragmentHomeBindDeviceDeviceBinding
    private lateinit var shp:Shp
    private lateinit var homeBindDeviceDeviceAdapter: HomeBindDeviceDeviceAdapter
    private var tokenDialog: TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_bind_device_device, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBindDeviceViewModel = ViewModelProvider(requireActivity()).get(HomeBindDeviceViewModel::class.java)
        shp = Shp(requireContext())
        homeBindDeviceDeviceAdapter = HomeBindDeviceDeviceAdapter(homeBindDeviceViewModel,requireActivity())
        val linearLayout = LinearLayoutManager(requireContext())
        binding.choiceDeviceList.apply {
            adapter = homeBindDeviceDeviceAdapter
            layoutManager = linearLayout
        }


        homeBindDeviceViewModel.homeBindDeviceDeviceListLiveData?.observe(viewLifecycleOwner, Observer {

            homeBindDeviceDeviceAdapter.submitList(it)

            if (homeBindDeviceViewModel.needToScrollToTopBindDevice) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                homeBindDeviceDeviceAdapter.notifyDataSetChanged()
                lifecycleScope.launch {
                    delay(50)
                    linearLayout.scrollToPositionWithOffset(0, 0)
                }

                homeBindDeviceViewModel.needToScrollToTopBindDevice = false
            }

        })
        //下滑刷新重新请求列表
        binding.choiceDeviceRefresh.setOnRefreshListener {

            homeBindDeviceViewModel.resetHomeDeviceDeviceQuery()
        }
        homeBindDeviceViewModel.homeBindDeviceDeviceNetWorkStatus?.observe(viewLifecycleOwner, Observer {
            if (it == HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                homeBindDeviceViewModel.needToScrollToTopBindDevice = true
            }
            homeBindDeviceDeviceAdapter.updateNetWorkStatus(it)
            binding.choiceDeviceRefresh.isRefreshing =
                it == HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_LOADING
        })




        homeBindDeviceDeviceAdapter.setOnItemClickListener(object :HomeBindDeviceDeviceAdapter.OnItemClickListener{
            override fun onItemClick(sn: String, status: Int,itemView:View) {
                when(status){
                    1 -> {
                        homeBindDeviceViewModel.sn.value = sn
                        Navigation.findNavController(itemView).navigate(R.id.action_homeBindDeviceDeviceFragment2_to_homeBindDeviceBindFragment2)
                    }
                    else -> {
                        ToastUtils.showTextToast(requireContext(),"此设备状态目前不可以绑定用户")
                    }
                }


            }

        })


        binding.back.setOnClickListener {
            requireActivity().finish()
        }


        binding.choiceDeviceSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                hideKeyboard(v,requireContext())
                shp.saveToSp("homebinddevicedevicekeyword",binding.choiceDeviceSearch.text.toString())

                homeBindDeviceViewModel.resetHomeDeviceDeviceQuery()
            }
            false
        }

        binding.choiceDeviceSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                lifecycleScope.launch {
                    delay(200)
                    shp.saveToSp("homebinddevicedevicekeyword","")
                    delay(200)
                    homeBindDeviceDeviceAdapter.notifyDataSetChanged()
                    homeBindDeviceViewModel.resetHomeDeviceDeviceQuery()
                }


            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }

    fun hideKeyboard(view: View,context: Context) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        shp.saveToSp("homebinddevicedevicekeyword","")
    }

}