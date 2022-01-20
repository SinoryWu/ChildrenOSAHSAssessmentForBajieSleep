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
            setShimmerColor(0x55FFFFFF) //设置闪烁颜色
            setShimmerAngle(0) //设置闪烁角度
            setShimmerAnimationDuration(600)
            startShimmerAnimation() //开始闪烁
        }

        Glide.with(context)
            .asBitmap()
            .load(url) //加载的地址
//            .placeholder(R.drawable.treat_detail_initial_background) //加载成功前的占位图片
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
     * 保存图片
     * 将现有的图片转换为位图保存下来
     * 保存图片是一个耗时操作 需要开启一个协程执行这些操作
     * 先找到viewholder再找到图
     */
    private  fun  savePhoto(bitmap: Bitmap){
//        withContext(Dispatchers.IO) {
//            //从viewgroup的0号位置找到recyclerview 再找到viewholder
////            val holder = (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
//            //toBitmap里面传递的两个参数是宽和高
////            val bitmap = imageWidth?.let { imageHeight?.let { it1 ->
////                    photoView.drawable.toBitmap(it, it1)
////                }
//        }
        if (Build.VERSION.SDK_INT < 29){
            if (MediaStore.Images.Media.insertImage(context.contentResolver,bitmap,"","") == null){
                //吐司如果不放在主线程里面就会报错
                MainScope().launch {
                    ToastUtils.showTextToast(context,"保存失败")
                }
            }else {
                MainScope().launch {
                    ToastUtils.showTextToast(context,"保存成功")
                }
            }
        }else {
            //保存图片用的uri
            val saveUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?: kotlin.run {
                MainScope().launch {
                    ToastUtils.showTextToast(context,"保存失败")
                }
                return
            }

            //use在用完之后可以将流自动关闭 用OutputStream写入流
            context.contentResolver.openOutputStream(saveUri).use {
                //bitmap写入流都用compress
                //使用jpg格式压缩率为90
                if(bitmap?.compress(Bitmap.CompressFormat.JPEG,90,it) == true){
                    MainScope().launch {
                        ToastUtils.showTextToast(context,"保存成功")
                    }
                }else{
                    MainScope().launch {
                        ToastUtils.showTextToast(context,"保存失败")

                    }
                }
            }
        }


    }


}