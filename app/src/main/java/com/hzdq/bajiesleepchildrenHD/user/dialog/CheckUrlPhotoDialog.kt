package com.hzdq.bajiesleepchildrenHD.user.dialog

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.user.activities.REQUEST_WRITE_EXTERNAL_STORAGE
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUIJava
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import io.supercharge.shimmerlayout.ShimmerLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener

class CheckUrlPhotoDialog( context: Context,val url:String,val userViewModel: UserViewModel,val lifecycleOwner: LifecycleOwner,confirmAction: ConfirmAction):Dialog(context, R.style.CustomDialogPhoto) {
    private var screenWidth = 0
    private var screenHeight = 0
    private  var imageHeight:Int? = null
    private  var imageWidth:Int? = null
    private var bitmap :Bitmap? = null
    private var confirmAction: ConfirmAction? = null
    interface ConfirmAction {
        fun onClick(photoView: PhotoView)
    }
    init {
        this.confirmAction = confirmAction
        HideDialogUI.hideSystemUI(this)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_check_url_photo)

        val photoView = findViewById<PhotoView>(R.id.check_photo_dialog_url)
        val shimmer = findViewById<ShimmerLayout>(R.id.check_photo_dialog_shimmer)

        shimmer.apply {
            setShimmerColor(0x55FFFFFF) //??????????????????
            setShimmerAngle(0) //??????????????????
            setShimmerAnimationDuration(600)
            startShimmerAnimation() //????????????
        }

        Glide.with(context)
            .asBitmap()
            .load(url) //???????????????
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
                        shimmer.stopShimmerAnimation()
                        imageHeight = resource?.height
                        imageWidth = resource?.width

                        userViewModel.checkDetailBitmap.value = resource
                    }
                }
            })
            .into(photoView)

        photoView.setOnPhotoTapListener(object : OnPhotoTapListener {
            override fun onPhotoTap(view: View, x: Float, y: Float) {
//                dismiss()
                confirmAction?.onClick(photoView)
            }

            override fun onOutsidePhotoTap() {
                dismiss()
            }
        })


//        imageHeight = photoView.height
//        imageWidth = photoView.width
//        val bitmap = imageWidth?.let { imageHeight?.let { it1 -> photoView.drawable.toBitmap(it, it1) }}
//
////        photoView.setOnClickListener {
////            confirmAction?.onClick(photoView)
////            userViewModel.checkDetailBitmap.value?.let { it1 -> savePhoto(it1) }
//            savePhoto(userViewModel.checkDetailBitmap.value!!)

//        }






    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

    /**
     * ????????????
     * ?????????????????????????????????????????????
     * ????????????????????????????????? ??????????????????????????????????????????
     * ?????????viewholder????????????
     */
    private  fun  savePhoto(bitmap: Bitmap){
//        withContext(Dispatchers.IO) {
//            //???viewgroup???0???????????????recyclerview ?????????viewholder
////            val holder = (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
//            //toBitmap???????????????????????????????????????
////            val bitmap = imageWidth?.let { imageHeight?.let { it1 ->
////                    photoView.drawable.toBitmap(it, it1)
////                }
//        }
        if (Build.VERSION.SDK_INT < 29){
            if (MediaStore.Images.Media.insertImage(context.contentResolver,bitmap,"","") == null){
                //????????????????????????????????????????????????
                MainScope().launch {
                    ToastUtils.showTextToast(context,"????????????")
                }
            }else {
                MainScope().launch {
                    ToastUtils.showTextToast(context,"????????????")
                }
            }
        }else {
            //??????????????????uri
            val saveUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?: kotlin.run {
                MainScope().launch {
                    ToastUtils.showTextToast(context,"????????????")
                }
                return
            }

            //use??????????????????????????????????????? ???OutputStream?????????
            context.contentResolver.openOutputStream(saveUri).use {
                //bitmap???????????????compress
                //??????jpg??????????????????90
                if(bitmap?.compress(Bitmap.CompressFormat.JPEG,90,it) == true){
                    MainScope().launch {
                        ToastUtils.showTextToast(context,"????????????")
                    }
                }else{
                    MainScope().launch {
                        ToastUtils.showTextToast(context,"????????????")

                    }
                }
            }
        }


    }


}