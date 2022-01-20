package com.hzdq.dowloadapk

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.hzdq.bajiesleepchildrenHD.MainViewModel
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.utils.HideDialogUI

class DownLoadAPKDialog  (val mainViewModel: MainViewModel,val lifecycleOwner: LifecycleOwner,context: Context): Dialog(context, R.style.CustomDialog){
    private var screenWidth = 0
    private var screenHeight = 0


    init {


        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels - getDensityValue(950, context)
        screenHeight = metrics.heightPixels - getDensityValue(600,context)
        HideDialogUI.hideSystemUI(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_download_apk, null)
        setContentView(view)
        window!!.setLayout(screenWidth,screenHeight )

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_horizontal)
        val percent = view.findViewById<TextView>(R.id.download_percent)
        mainViewModel.percentageFloat.observe(lifecycleOwner, Observer {
            progressBar.setProgress(it.toInt())
        })

        mainViewModel.percentage.observe(lifecycleOwner, Observer {
            percent.text = "${it}%"
        })



    }

    fun getDensityValue(value: Int, activity: Context?): Int {
        val displayMetrics = activity!!.resources.displayMetrics
        return Math.ceil((value * displayMetrics.density).toDouble()).toInt()
    }

}