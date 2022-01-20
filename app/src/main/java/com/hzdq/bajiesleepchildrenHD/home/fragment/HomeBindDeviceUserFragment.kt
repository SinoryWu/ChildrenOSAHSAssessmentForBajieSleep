package com.hzdq.bajiesleepchildrenHD.home.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentHomeBindDeviceUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceDeviceAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceUserAdapter
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeBindDeviceUserFragment : Fragment() {

    private lateinit var binding:FragmentHomeBindDeviceUserBinding
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private lateinit var homeBindDeviceUserAdapter: HomeBindDeviceUserAdapter
    private lateinit var shp:Shp
    private var tokenDialog: TokenDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_bind_device_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shp = Shp(requireContext())
        homeBindDeviceViewModel = ViewModelProvider(requireActivity()).get(HomeBindDeviceViewModel::class.java)

        homeBindDeviceUserAdapter = HomeBindDeviceUserAdapter(homeBindDeviceViewModel,requireActivity())
        binding.back.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        val linearLayout = LinearLayoutManager(requireContext())
        binding.choiceUserList.apply {
            adapter = homeBindDeviceUserAdapter
            layoutManager = linearLayout
        }

        homeBindDeviceViewModel.homeBindDeviceUserListLiveData?.observe(viewLifecycleOwner, Observer {

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
        binding.choiceUserRefresh.setOnRefreshListener {

            homeBindDeviceViewModel.resetHomeDeviceUserQuery()
        }
        homeBindDeviceViewModel.homeBindDeviceUserNetWorkStatus.observe(viewLifecycleOwner, Observer {

            if (it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                homeBindDeviceViewModel.needToScrollToTopBindUser = true
            }
            homeBindDeviceUserAdapter.updateNetWorkStatus(it)
            binding.choiceUserRefresh.isRefreshing =
                it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_LOADING
        })

        homeBindDeviceUserAdapter.setOnItemClickListener(object :HomeBindDeviceUserAdapter.OnItemClickListener{
            override fun onItemClick(dataItem:DataHomeBindDeviceUserX, itemView: View) {
                if (dataItem.using == true){
                    ToastUtils.showTextToast(requireContext(),"此用户已绑定设备")
                }else {
                    homeBindDeviceViewModel.uid.value = dataItem.uid
                    homeBindDeviceViewModel.name.value = dataItem.truename
                    Navigation.findNavController(itemView).navigate(R.id.action_homeBindDeviceUserFragment_to_homeBindDeviceBindFragment)
                }


            }

        })

        binding.choiceUserSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                hideKeyboard(v,requireContext())
                shp.saveToSp("homebinddeviceuserkeyword",binding.choiceUserSearch.text.toString())
                homeBindDeviceUserAdapter.notifyDataSetChanged()
                homeBindDeviceViewModel.resetHomeDeviceUserQuery()
            }
            false
        }

        binding.choiceUserSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    delay(200)
                    shp.saveToSp("homebinddeviceuserkeyword",s.toString())
                    delay(200)
                    homeBindDeviceUserAdapter.notifyDataSetChanged()
                    homeBindDeviceViewModel.resetHomeDeviceUserQuery()

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

    override fun onDestroyView() {
        super.onDestroyView()
        shp.saveToSp("homebinddeviceuserkeyword","")
    }
}