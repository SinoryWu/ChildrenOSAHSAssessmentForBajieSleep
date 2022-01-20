package com.hzdq.bajiesleepchildrenHD.login.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentPassWordBinding
import com.hzdq.bajiesleepchildrenHD.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PassWordFragment : Fragment() {

    private lateinit var binding: FragmentPassWordBinding
    private lateinit var loginViewModel:LoginViewModel
    private var phoneNumber =""
    private var password =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pass_word, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(requireActivity()).get(
            LoginViewModel::class.java
        )

        binding.loginAccount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.phoneNumber = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.loginPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.passWord = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.loginUseMessage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_passWordFragment_to_messageFragment)

        }


        binding.loginAccount.setText(loginViewModel.phoneNumber)
        binding.loginPassword.setText(loginViewModel.passWord)
    }


}