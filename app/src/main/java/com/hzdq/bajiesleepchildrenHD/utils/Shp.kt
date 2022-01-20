package com.hzdq.bajiesleepchildrenHD.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class Shp(val context: Context) {

    fun getUserAgent(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("user_agent", "")
    }

    fun getToken():String?{
        val sp = context.getSharedPreferences("sp",Context.MODE_PRIVATE)
        return sp.getString("token","")
    }

    fun getUid(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("uid", "")
    }

    fun saveToSp(key:String,value:String){
        context.getSharedPreferences("sp", AppCompatActivity.MODE_PRIVATE).apply {
            this.edit().putString(key,value).apply()
        }
    }

    fun saveToSpInt(key:String,value:Int){
        context.getSharedPreferences("sp", AppCompatActivity.MODE_PRIVATE).apply {
            this.edit().putInt(key,value).apply()
        }
    }
    fun getUserBarPosition(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("userbarpostion", "")
    }


    fun getReportBarPosition(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("reportbarpostion", "")
    }


    fun getFrontUserKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("frontuserkeyword", "")
    }

    fun getFrontFollowUpKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("frontfollowupkeyword", "")
    }

    fun getFrontEvaluateKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("frontevaluatekeyword", "")
    }
    fun getFrontDeviceKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("frontdevicekeyword", "")
    }

    fun getHomeBindDeviceDeviceKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("homebinddevicedevicekeyword", "")
    }

    fun getHomeBindDeviceUserKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("homebinddeviceuserkeyword", "")
    }

    fun getFrontDeviceStatus(): Int? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("frontdevicestatus", 0)
    }


    fun getHospitalId(): Int? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("hospitalid", 0)
    }


    fun getHomeUserSearchKeyWord(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("homeusersearchkeyword", "")
    }


    fun getDoctorName(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("doctorname","")
    }

    fun getPatientId(): Int? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("patientid", 0)
    }

    fun getTreatRecordPatientId():Int?{
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("treatrecordpatientid", 0)
    }

    fun getUserQuestionId():Int?{
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("userquestionsid", 0)
    }

    fun getFollowUpPatientId():Int?{
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("followuppatientid", 0)
    }

    fun getUserReportSubtype():Int?{
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("userreportsubtype", 0)
    }
    fun getFrontFollow():Int?{
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getInt("frontfollow", 1)
    }

    fun getWelcome(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("welcome","欢迎使用八戒睡眠儿童OSAHS评估系统！")
    }


    fun getHospitalName(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("hospitalname","")
    }

    fun getUserMobile(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("usermobile","")
    }


    fun getUserName(): String? {
        val sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
        return sp.getString("username","")
    }
}