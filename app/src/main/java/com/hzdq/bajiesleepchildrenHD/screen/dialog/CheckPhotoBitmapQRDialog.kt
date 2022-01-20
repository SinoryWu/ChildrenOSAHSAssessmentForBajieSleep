package com.hzdq.bajiesleepchildrenHD.screen.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI
import com.hzdq.bajiesleepchildrenHD.utils.ToastUtils
import uk.co.senab.photoview.PhotoView

class CheckPhotoBitmapQRDialog(context: Context, bitmap: Bitmap,confirmAction: ConfirmAction):Dialog(context, R.style.CustomDialog) {
    private var photoView: PhotoView? = null
    private var bitmap: Bitmap? = null
    private var activity: Activity? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var confirmAction: ConfirmAction? = null
    interface ConfirmAction {
        fun onRightClick(bitmap: Bitmap)
    }
  init {
      this.bitmap = bitmap
      this.activity = activity
      this.confirmAction = confirmAction
      val metrics = context.resources.displayMetrics
      screenWidth = metrics.widthPixels - getDensityValue(0, context)
      screenHeight = metrics.heightPixels - getDensityValue(0,context)
      HideDialogUI.hideSystemUI(this)
  }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_check_photo_uri_qr)
//        window!!.setLayout(screenWidth,screenHeight )



        val photoView = findViewById<ImageView>(R.id.check_photo_dialog_uri_qr)
        photoView.setImageBitmap(bitmap)

        photoView.setOnClickListener {
            Toast.makeText(context,"长按图片保存",Toast.LENGTH_SHORT).show()
        }

        val bmp = createBitmap3(photoView,500,500)
        photoView.setOnLongClickListener {
            bmp?.let { it1 -> confirmAction?.onRightClick(it1) }
            return@setOnLongClickListener true
        }

    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

    private fun createBitmap(view: View): Bitmap? {
        view.buildDrawingCache()
        return view.drawingCache
    }

    fun createBitmap3(v: View, width: Int, height: Int): Bitmap? {
        //测量使得view指定大小
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        v.measure(measuredWidth, measuredHeight)
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }
}