package com.hzdq.bajiesleepchildrenHD.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.hzdq.bajiesleepchildrenHD.R

object ToastUtils {
    fun showTextToast(context: Context?, message: String?) {
        val toastview: View = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val text = toastview.findViewById<View>(R.id.tv_toast) as TextView
        text.text = message
        val toast = Toast(context)
        //        toast.setGravity(Gravity.CENTER,0,0);
        toast.duration = Toast.LENGTH_SHORT
        toast.setView(toastview)
        toast.show()
    }

    fun showTextToast2(context: Context?, message: String?) {
        val toastview: View = LayoutInflater.from(context).inflate(R.layout.layout_toast2, null)
        val text = toastview.findViewById<View>(R.id.tv_toast2) as TextView
        text.text = message
        val toast = Toast(context)
        //        toast.setGravity(Gravity.CENTER,0,0);
        toast.duration = Toast.LENGTH_SHORT
        toast.setView(toastview)
        toast.show()
    }
}