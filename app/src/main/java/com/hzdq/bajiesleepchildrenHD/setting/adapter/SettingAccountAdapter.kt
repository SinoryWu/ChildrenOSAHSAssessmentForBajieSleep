package com.hzdq.bajiesleepchildrenHD.setting.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataAdministratorsX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX
import com.hzdq.bajiesleepchildrenHD.dataclass.ReportRecoverDeviceInfo
import com.hzdq.bajiesleepchildrenHD.device.adapter.DeviceRecoveryViewHolder
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepViewHolder

class SettingAccountAdapter:ListAdapter<DataAdministratorsX,SettingAccountViewHolder>(DIFFCALLBACK) {


    object DIFFCALLBACK: DiffUtil.ItemCallback<DataAdministratorsX>() {
        override fun areItemsTheSame(oldItem: DataAdministratorsX, newItem: DataAdministratorsX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.uid === newItem.uid
        }

        override fun areContentsTheSame(oldItem: DataAdministratorsX, newItem: DataAdministratorsX): Boolean {
            //判断两个item内容是否相同
            return oldItem.uid == newItem.uid
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingAccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_account,parent,false)
        val holder = SettingAccountViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: SettingAccountViewHolder, position: Int) {
        when(getItem(position).type){
            "1" -> holder.type.text = "管理员："
            "2" -> holder.type.text = "呼吸教练："
            "3" -> holder.type.text = "全科医师："
            "4" -> holder.type.text = "医院领导："
            "5" -> holder.type.text = "机构管理员："
            "6" -> holder.type.text = "线上咨询："
            "7" -> holder.type.text = "运营专员："
            "8" -> holder.type.text = "机构操作员："
        }

        holder.name.text = getItem(position).truename
        holder.mobile.text = getItem(position).mobile
    }


}

class SettingAccountViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val type = itemView.findViewById<TextView>(R.id.item_setting_account_type)
    val name = itemView.findViewById<TextView>(R.id.item_setting_account_name)
    val mobile = itemView.findViewById<TextView>(R.id.item_setting_account_mobile)
}