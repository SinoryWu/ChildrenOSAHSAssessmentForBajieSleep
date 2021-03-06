package com.hzdq.bajiesleepchildrenHD.user.activities

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.TokenDialog
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityUpdateReportBinding
import com.hzdq.bajiesleepchildrenHD.utils.ActivityCollector2

import com.hzdq.bajiesleepchildrenHD.utils.HideUI
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UpdateReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateReportBinding
    private var animation: Animation? = null
    private var popupWindow: PopupWindow? = null
    private var tokenDialog: TokenDialog? = null
    var photoFile: File? = null
    var listPicString: MutableList<String> = ArrayList()
    private val listpic: MutableList<Bitmap> = ArrayList()
    private var mCameraImagePath: String? = null
    // ?????????Android 10????????????
    private val isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    //??????????????????????????????requestCode
    private val CAMERA_REQUEST_CODE = 2

    //??????????????????????????????requestCode
    private val CHOOSE_PHOTO_REQUEST_CODE = 3

    // ?????????????????????requestCode
    private val PERMISSION_CAMERA_REQUEST_CODE = 0x00000012

    //???????????????????????????requestCode
    private val PERMISSION_ALBUM_REQUEST_CODE = 0x00000013

//    lateinit var upDataReportAddPicAdapter: UpDataReportAddPicAdapter
    //???????????????????????????uri
    private var mCameraUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_report)
        ActivityCollector2.addActivity(this)
        HideUI(this).hideSystemUI()

        binding.updateBack.setOnClickListener {
            finish()
        }
        setBar()
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        super.onDestroy()
    }


    private fun setBar(){
        val sleep = findViewById<Button>(R.id.update_report_bar_sleep)
        val image =findViewById<Button>(R.id.update_report_bar_image)
        val endoscope = findViewById<Button>(R.id.update_report_bar_endoscope)
        val biochemistry =findViewById<Button>(R.id.update_report_bar_biochemistry)
        val other =findViewById<Button>(R.id.update_report_bar_other)

        sleep.setOnClickListener {
            sleep.isSelected = true
            sleep.setTextColor(Color.parseColor("#2051BD"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
        }

        image.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = true
            image.setTextColor(Color.parseColor("#2051BD"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
        }

        endoscope.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = true
            endoscope.setTextColor(Color.parseColor("#2051BD"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
        }

        biochemistry.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = true
            biochemistry.setTextColor(Color.parseColor("#2051BD"))
            other.isSelected = false
            other.setTextColor(Color.parseColor("#C1C1C1"))
        }

        other.setOnClickListener {
            sleep.isSelected = false
            sleep.setTextColor(Color.parseColor("#C1C1C1"))
            image.isSelected = false
            image.setTextColor(Color.parseColor("#C1C1C1"))
            endoscope.isSelected = false
            endoscope.setTextColor(Color.parseColor("#C1C1C1"))
            biochemistry.isSelected = false
            biochemistry.setTextColor(Color.parseColor("#C1C1C1"))
            other.isSelected = true
            other.setTextColor(Color.parseColor("#2051BD"))
        }

        when(Shp(this).getReportBarPosition()){
            "1" ->  sleep.performClick()
            "2" ->  image.performClick()
            "3" ->  endoscope.performClick()
            "4" ->  biochemistry.performClick()
            "5" ->  other.performClick()
        }




    }
    private fun initRecyclePic() {
        // ????????????
        val linearLayoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        binding.picReclerView.setLayoutManager(linearLayoutManager)
        //   ??????????????????????????????????????????????????????
//        list = initData();
//        upDataReportAddPicAdapter = UpDataReportAddPicAdapter(this, listpic)
//        binding.picReclerView.setAdapter(upDataReportAddPicAdapter)
        //   ????????????
        binding.picReclerView.setItemAnimator(DefaultItemAnimator())
    }

    /**
     *
     * @param popupView
     */
    fun showPuPopWindow(popupView1: View) {
        var popupView = popupView1
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.popview_item, null)
            // ??????2,3?????????popupwindow??????????????????
            popupWindow = PopupWindow(
                popupView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            // ????????????????????? ????????????????????????????????????
            popupWindow!!.setBackgroundDrawable(BitmapDrawable())
            popupWindow!!.setFocusable(true)
            popupWindow!!.setOutsideTouchable(true)
            // ???????????????????????????????????????????????????X????????????Y??????1???0
            animation = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F,
                Animation.RELATIVE_TO_PARENT, 1F, Animation.RELATIVE_TO_PARENT, 0F
            )
            (animation as TranslateAnimation).interpolator = AccelerateInterpolator()
            (animation as TranslateAnimation).duration = 250

            // ??????popupWindow????????????????????????????????????????????????????????????????????????
            popupWindow!!.showAtLocation(
                this.findViewById<View>(R.id.update_report_bottom_view),
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0,
                0
            )
            popupView.startAnimation(animation)
            popupWindow!!.setOnDismissListener(PopupWindow.OnDismissListener { popupWindow = null })
            popupView.findViewById<View>(R.id.ecology_popwindow_tv_camera).setOnClickListener {
                checkPermissionAndCamera()
                popupWindow!!.dismiss()
            }
            popupView.findViewById<View>(R.id.ecology_popwindow_tv_photo).setOnClickListener {
                checkPermissionAndAlbum()
                popupWindow!!.dismiss()
            }
            popupView.findViewById<View>(R.id.ecology_popwindow_tv_cancel)
                .setOnClickListener { popupWindow!!.dismiss() }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????????????????????????????????
                    openCamera()
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show()
                }
            }
            PERMISSION_ALBUM_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //??????????????????????????????
                    openAlbum()
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this, "???????????????????????????", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private fun checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //??????????????????????????????
            openAlbum()
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_ALBUM_REQUEST_CODE)
        }
    }

    /**
     * ????????????
     */
    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        //???????????????????????????
        intent.type = "image/*"
        startActivityForResult(
            intent,
            CHOOSE_PHOTO_REQUEST_CODE
        )
    }

    /**
     * ????????????????????????
     * ?????????????????????????????????
     */
    private fun checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //??????????????????????????????
            openCamera()
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA_REQUEST_CODE
            )
        }
    }

    /**
     * ??????????????????
     */
    private fun openCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // ?????????????????????
        if (captureIntent.resolveActivity(packageManager) != null) {
            photoFile = null
            var photoUri: Uri? = null
            if (isAndroidQ) {
                // ??????android 10
                photoUri = createImageUri()
            } else {
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (photoFile != null) {
                    mCameraImagePath = photoFile!!.absolutePath
                    photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
                        FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile!!)
                    } else {
                        Uri.fromFile(photoFile)
                    }
                }
            }
            mCameraUri = photoUri
            Log.d("opencamera", mCameraUri.toString())
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }


    /**
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private fun createImageUri(): Uri? {
        val status = Environment.getExternalStorageState()
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        return if (status == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        } else {
            contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, ContentValues())
        }
    }


    /**
     * ???????????????????????????
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        var imageName: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageName = "output_image.jpg"
        }
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) {
            storageDir.mkdir()
        }
        val tempFile = File(storageDir, imageName)
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }


    /**
     * android10??????uri???file
     * @param uri
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun uriToFileApiQ(uri: Uri, context: Context): File? {
        var file: File? = null
        //android10????????????
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            file = File(uri.path)
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //??????????????????????????????
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                try {
                    val `is` = contentResolver.openInputStream(uri)
                    val cache = File(
                        context.externalCacheDir!!.absolutePath,
                        Math.round((Math.random() + 1) * 1000).toString() + displayName
                    )
                    val fos = FileOutputStream(cache)
                    FileUtils.copy(`is`!!, fos)
                    file = cache
                    fos.close()
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file
    }
}