package com.hzdq.bajiesleepchildrenHD.user.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUserScreenChoiceUserBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceUserAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeBindDeviceUserViewHolder
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2
import com.hzdq.bajiesleepchildrenHD.utils.HideKeyboard
import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserScreenChoiceUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserScreenChoiceUserBinding
    private lateinit var homeBindDeviceViewModel: HomeBindDeviceViewModel
    private lateinit var homeBindDeviceUserAdapter: HomeBindDeviceUserAdapter
    private lateinit var userViewHolder: UserViewModel
    private var tokenDialog: TokenDialog? = null
    private lateinit var shp: Shp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideUI(this).hideSystemUI()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_screen_choice_user)
        ActivityCollector2.addActivity(this)
        shp = Shp(this)
        val retrofitSingleton = RetrofitSingleton.getInstance(this)
        homeBindDeviceViewModel = ViewModelProvider(this).get(HomeBindDeviceViewModel::class.java)
        binding.choiceUserBack.setOnClickListener {
            finish()
        }

        homeBindDeviceUserAdapter = HomeBindDeviceUserAdapter(homeBindDeviceViewModel,this)
        val linearLayout = LinearLayoutManager(this)
        binding.choiceUserList.apply {
            adapter = homeBindDeviceUserAdapter
            layoutManager = linearLayout
        }

        homeBindDeviceViewModel.homeBindDeviceUserListLiveData.observe(this, Observer {

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
        homeBindDeviceViewModel.homeBindDeviceUserNetWorkStatus.observe(this, Observer {

            if (it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_INITIAL_LOADED) {
//                binding.userFragmentRecyclerView.scrollToPosition(0)
                linearLayout.scrollToPositionWithOffset(0, 0)
                homeBindDeviceViewModel.needToScrollToTopBindUser = true
            }
            homeBindDeviceUserAdapter.updateNetWorkStatus(it)
            binding.choiceUserRefresh.isRefreshing =
                it == HomeBindDeviceUserNetWorkStatus.HOME_BIND_DEVICE_USER_LOADING
        })

        binding.choiceUserSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                HideKeyboard.hideKeyboard(v,this)
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

        homeBindDeviceUserAdapter.setOnItemClickListener(object :HomeBindDeviceUserAdapter.OnItemClickListener{
            override fun onItemClick(dataItem:DataHomeBindDeviceUserX, itemView: View) {
                var sex = "男"
                if (dataItem.sex.equals("1")){
                    sex = "男"
                }else {
                    sex ="女"
                }
                val userInfo = "\"truename\":\"${dataItem.truename}\",\"age\":\"${dataItem.age}\",\"sex\":\"${sex}\",\"height\":\"${dataItem.height}\",\"weigth\":\"${dataItem.weight}\""

                val intent = Intent()
                intent.putExtra("uid",dataItem.uid.toString())
                intent.putExtra("userInfo",userInfo)
                intent.putExtra("name",dataItem.truename)
                setResult(RESULT_OK,intent)
                finish()
            }

        })


    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
        shp.saveToSp("homebinddeviceuserkeyword","")
    }



}