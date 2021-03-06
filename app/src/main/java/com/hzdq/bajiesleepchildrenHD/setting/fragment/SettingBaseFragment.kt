package com.hzdq.bajiesleepchildrenHD.setting.fragment

import android.R.attr.bitmap
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentSettingBaseBinding
import com.hzdq.bajiesleepchildrenHD.setting.activity.SettingEditBaseInfoActivity
import com.hzdq.bajiesleepchildrenHD.setting.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.activity_test.view.*
import java.io.ByteArrayOutputStream


const val EDIT_BASE_INFO_REQUEST_CODE = 10

class SettingBaseFragment : Fragment() {

    private lateinit var binding: FragmentSettingBaseBinding
    private var tokenDialog: TokenDialog? = null
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setting_base, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        binding.edit.setOnClickListener {
            val intent = Intent(requireActivity(), SettingEditBaseInfoActivity::class.java)
            intent.putExtra("hospitalName", settingViewModel.hospitalName.value)
            intent.putExtra("hospitalIcon", settingViewModel.hospitalIcon.value)
            intent.putExtra("welcome", settingViewModel.welcome.value)
            intent.putExtra("reportName", settingViewModel.reportName.value)
            intent.putExtra("reportIcon", settingViewModel.reportIcon.value)
            intent.putExtra("standard", settingViewModel.standard.value)
            intent.putExtra("standardContent", settingViewModel.standardContent.value)
            intent.putExtra("evaluate", settingViewModel.evaluate.value)
            intent.putExtra("evaluateContent", settingViewModel.evaluateContent.value)
            settingViewModel.hospitalIconBitmap.value?.let {
                intent.putExtra(
                    "hospitalIconBitmap",
                    it
                )
            }
            settingViewModel.reportIconBitmap.value?.let { intent.putExtra("reportIconBitmap", it) }


//            intent.putExtra("evaluateContent","")
            startActivityForResult(intent, EDIT_BASE_INFO_REQUEST_CODE)
        }

        settingViewModel.hospitalIcon.observe(requireActivity(), Observer {
            binding.hospitalIconShimmer.apply {
                setShimmerColor(0x55FFFFFF) //??????????????????
                setShimmerAngle(0) //??????????????????
                setShimmerAnimationDuration(600)
                startShimmerAnimation() //????????????
            }

            Glide.with(requireActivity())
                .asBitmap()
                .load(it) //???????????????
                .placeholder(R.drawable.treat_detail_initial_background)
//            .placeholder(R.drawable.treat_detail_initial_background) //??????????????????????????????
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.hospitalIconShimmer.stopShimmerAnimation()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false.also {
                            binding.hospitalIconShimmer.stopShimmerAnimation()


                            val output = ByteArrayOutputStream() //????????????????????????

                            resource?.compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                output
                            ) //???bitmap100%??????????????? ??? output?????????

                            val result = output.toByteArray() //???????????????  result????????????bit???????????????
                            settingViewModel.hospitalIconBitmap.value = result

                        }
                    }
                })
                .into(binding.hospitalIconPic)

        })


        settingViewModel.reportIcon.observe(requireActivity(), Observer {
            binding.sleepIconShimmer.apply {
                setShimmerColor(0x55FFFFFF) //??????????????????
                setShimmerAngle(0) //??????????????????
                setShimmerAnimationDuration(600)
                startShimmerAnimation() //????????????
            }

            Glide.with(requireActivity())
                .asBitmap()
                .load(it) //???????????????
                .placeholder(R.drawable.treat_detail_initial_background)
//            .placeholder(R.drawable.treat_detail_initial_background) //??????????????????????????????
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.sleepIconShimmer.stopShimmerAnimation()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false.also {
                            binding.sleepIconShimmer.stopShimmerAnimation()
                            val output = ByteArrayOutputStream() //????????????????????????

                            resource?.compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                output
                            ) //???bitmap100%??????????????? ??? output?????????

                            val result = output.toByteArray() //???????????????  result????????????bit???????????????
                            settingViewModel.reportIconBitmap.value = result
                        }
                    }
                })
                .into(binding.sleepIconPic)

        })


        settingViewModel.hospitalName.observe(requireActivity(), Observer {
            binding.hospitalName.text = it
        })
        settingViewModel.reportName.observe(requireActivity(), Observer {
            binding.sleepName.text = it
        })

        settingViewModel.welcome.observe(requireActivity(), Observer {
            binding.welcome.text = it
        })

        settingViewModel.standard.observe(requireActivity(), Observer {

            if (it.equals("1")) {
                binding.opinion.text = "2007???????????????"
            } else if (it.equals("2")) {
                binding.opinion.text = "2020???????????????/2014???????????????"
            }
        })

        settingViewModel.standardContent.observe(requireActivity(), Observer {
            binding.opinionContent.text = it
        })

        settingViewModel.evaluate.observe(requireActivity(), Observer {
            if (it.equals("1")) {
                binding.assessment.text = "????????????"
            } else if (it.equals("2")) {
                binding.assessment.text = "????????????"
            } else if (it.equals("3")) {
                binding.assessment.text = "????????????"
            } else if (it.equals("4")) {
                binding.assessment.text = "????????????"
            }

        })

        settingViewModel.evaluateContent.observe(requireActivity(), Observer {
            binding.assessmentContent.text = it
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EDIT_BASE_INFO_REQUEST_CODE
        when (requestCode) {
            EDIT_BASE_INFO_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    settingViewModel.refreshBase.value = 1
                }
            }
            else -> {
                Log.d("onActivityResult", "onActivityResult: ?????????")
            }
        }
    }
}