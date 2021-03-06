package com.hzdq.bajiesleepchildrenHD.user.fragment


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentCheckRecordDetailPicBinding
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentCheckReportDetailPicBinding
import com.hzdq.bajiesleepchildrenHD.user.activities.REQUEST_WRITE_EXTERNAL_STORAGE
import com.hzdq.bajiesleepchildrenHD.user.activities.UpdateReportActivity2
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.DownloadUtilKotlin
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File

@RequiresApi(Build.VERSION_CODES.M)
class CheckReportDetailPicFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentCheckReportDetailPicBinding
    private lateinit var navController: NavController
    var spotDialog: AlertDialog? = null
    private var popupWindow:PopupWindow? = null
    var filePath:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_check_report_detail_pic, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.shimmer.apply {
            setShimmerColor(0x55FFFFFF) //??????????????????
            setShimmerAngle(0) //??????????????????
            setShimmerAnimationDuration(600)
            startShimmerAnimation() //????????????
        }

        Glide.with(requireContext())
            .asBitmap()
            .load(userViewModel.checkUrl.value) //???????????????
//            .placeholder(R.drawable.treat_detail_initial_background) //??????????????????????????????
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {

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
                        binding.shimmer.stopShimmerAnimation()
                        userViewModel.checkDetailBitmap.value = resource
                    }
                }
            })
            .into(binding.photo)



        binding.photo.setOnPhotoTapListener(object : PhotoViewAttacher.OnPhotoTapListener {
            override fun onPhotoTap(view: View, x: Float, y: Float) {

                navController.navigateUp()
            }

            override fun onOutsidePhotoTap() {
                navController.navigateUp()
            }
        })


        binding.photo.setOnLongClickListener(OnLongClickListener {
            showPuPopWindow(it)
            false
        })







    }


    fun showPuPopWindow(popupView: View) {
        Log.d("TAG", "showPuPopWindow: ")
        var popupView = popupView
        popupView = View.inflate(requireContext(), R.layout.file_popview_item, null)

        var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //???????????????????????????
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //??????
                View.SYSTEM_UI_FLAG_FULLSCREEN or  //???????????????
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        uiOptions = uiOptions or 0x00001000
        popupView.systemUiVisibility = uiOptions

        // ??????2,3?????????popupwindow??????????????????

        // ??????2,3?????????popupwindow??????????????????
        popupWindow = PopupWindow(
            popupView, UpdateReportActivity2.dip2px(requireContext(), 427f),
            UpdateReportActivity2.dip2px(requireContext(), 170f)
        )

        // ????????????????????? ????????????????????????????????????
//        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow!!.setFocusable(true)
        popupWindow!!.setOutsideTouchable(true)
        var animation: Animation
        // ???????????????????????????????????????????????????X????????????Y??????1???0
        animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F,
            Animation.RELATIVE_TO_PARENT, 1F, Animation.RELATIVE_TO_PARENT, 0F
        )
        animation.setInterpolator(AccelerateInterpolator())
        animation.setDuration(250)

        //??????popupWindow????????????????????????????????????????????????????????????????????????
        popupWindow!!.showAtLocation(
            binding.bottom,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )

//            popupWindow!!.showAsDropDown(binding.viewPager2,0,dip2px(requireContext(),3f))
        popupView.startAnimation(animation)

//            popupWindow!!.setOnDismissListener{
//                popupWindow == null
//            }
        popupView.findViewById<View>(R.id.popwindow_save).setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }else {
                lifecycleScope.launch {
                    userViewModel.checkDetailBitmap.value?.let { it1 -> savePhoto(it1) }
                }
            }
            popupWindow!!.dismiss()

        }

        popupView.findViewById<View>(R.id.popwindow_share).setOnClickListener {
            spotDialog = SpotsDialog.Builder().setContext(requireContext())
                .setTheme(R.style.SpotDialogCustom)
                .setCancelable(false).build()
            spotDialog?.show()
            spotDialog?.getWindow()?.getDecorView()?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            spotDialog?.getWindow()?.getDecorView()?.setOnSystemUiVisibilityChangeListener {
                var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //???????????????????????????
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //??????
                        View.SYSTEM_UI_FLAG_FULLSCREEN or  //???????????????
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                uiOptions = uiOptions or 0x00001000
                spotDialog?.getWindow()?.getDecorView()?.setSystemUiVisibility(uiOptions)
            }
            DownLoadFile(userViewModel.checkUrl.value!!,"${requireActivity().cacheDir}/${userViewModel.fileNames.value}")
            popupWindow!!.dismiss()


        }
        popupView.findViewById<View>(R.id.popwindow_cancel).setOnClickListener {

            popupWindow!!.dismiss()


        }

        popupWindow!!.setOnDismissListener {
            popupWindow == null
        }

    }

    /**
     * ?????????????????????
     * @param url
     * @param path
     */
    private fun DownLoadFile(url: String, path: String) {
        DownloadUtilKotlin.download(url,path,object : DownloadUtilKotlin.OnDownloadListener{
            override fun onDownloadSuccess(path: String?) {

                Log.d("downloadfile", "????????????: ")
                lifecycleScope.launch {
                    delay(1500)
                    spotDialog!!.dismiss()
                    if (path != null) {
                        shareFile(path)
                    }
                }
            }

            override fun onDownloading(progress: Int) {

            }

            override fun onDownloadFailed(msg: String?) {
                lifecycleScope.launch {
                    delay(1500)
                    spotDialog!!.dismiss()
                    ToastUtils.showTextToast(requireContext(),"????????????")
                }
            }

        })
    }


    /**
     * ????????????????????????   image
     */
    private fun shareFile(path: String){

        val sharingIntent = Intent(Intent.ACTION_SEND)

        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //???????????????

        sharingIntent.type = "image/*" //????????????????????????type

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharingIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    requireContext(), "${requireActivity().packageName}.fileProvider",
                    File(path)
                )
            )
        } else {
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        }

        startActivity(Intent.createChooser(sharingIntent, "??????"))


    }


    //????????????????????????
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //????????????????????????????????????????????????
                    lifecycleScope.launch {
                        userViewModel.checkDetailBitmap.value?.let { it1 -> savePhoto(it1) }
                    }
                }else {
                    ToastUtils.showTextToast(requireContext(),"????????????")
                }
            }
        }
    }

    /**
     * ????????????
     * ?????????????????????????????????????????????
     * ????????????????????????????????? ??????????????????????????????????????????
     * ?????????viewholder????????????
     */
    private suspend fun  savePhoto(bitmap: Bitmap){
        withContext(Dispatchers.IO) {
            //???viewgroup???0???????????????recyclerview ?????????viewholder
//            val holder = (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
            //toBitmap???????????????????????????????????????
//            val bitmap = imageWidth?.let { imageHeight?.let { it1 ->
//                    photoView.drawable.toBitmap(it, it1)
//                }
        }
        if (Build.VERSION.SDK_INT < 29){
            if (MediaStore.Images.Media.insertImage(requireContext().contentResolver,bitmap,"","") == null){
                //????????????????????????????????????????????????
                MainScope().launch {
                    ToastUtils.showTextToast(requireContext(),"????????????")
                }
            }else {
                MainScope().launch {
                    ToastUtils.showTextToast(requireContext(),"????????????")
                }
            }
        }else {
            //??????????????????uri
            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?: kotlin.run {
                MainScope().launch {
                    ToastUtils.showTextToast(requireContext(),"????????????")
                }
                return
            }

            //use??????????????????????????????????????? ???OutputStream?????????
            requireContext().contentResolver.openOutputStream(saveUri).use {
                //bitmap???????????????compress
                //??????jpg??????????????????90
                if(bitmap?.compress(Bitmap.CompressFormat.JPEG,90,it) == true){
                    MainScope().launch {
                        ToastUtils.showTextToast(requireContext(),"????????????")
                    }
                }else{
                    MainScope().launch {
                        ToastUtils.showTextToast(requireContext(),"????????????")

                    }
                }
            }
        }


    }
}