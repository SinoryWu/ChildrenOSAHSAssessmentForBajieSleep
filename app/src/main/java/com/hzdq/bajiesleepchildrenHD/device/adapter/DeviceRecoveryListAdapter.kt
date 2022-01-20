package com.hzdq.bajiesleepchildrenHD.device.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX
import com.hzdq.bajiesleepchildrenHD.dataclass.ReportRecoverDeviceInfo
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepViewHolder

class DeviceRecoveryListAdapter:ListAdapter<ReportRecoverDeviceInfo, DeviceRecoveryViewHolder>(DIFFCALLBACK) {
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(reportUrl: String,quality:Int,createTime:Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mClickListener = listener
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<ReportRecoverDeviceInfo>() {
        override fun areItemsTheSame(oldItem: ReportRecoverDeviceInfo, newItem: ReportRecoverDeviceInfo): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: ReportRecoverDeviceInfo, newItem: ReportRecoverDeviceInfo): Boolean {
            //判断两个item内容是否相同
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceRecoveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recovery_report_list,parent,false)
        return DeviceRecoveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceRecoveryViewHolder, position: Int) {
        holder.name.text = getItem(position).reportId
        when(getItem(position).quality){
            1 -> {
                holder.name.setTextColor(Color.parseColor("#6CC291"))
                holder.quality.setTextColor(Color.parseColor("#352641"))
                holder.quality.text = "有效报告"
            }
            2 -> {
                holder.name.setTextColor(Color.parseColor("#F45C50"))
                holder.quality.setTextColor(Color.parseColor("#F45C50"))
                holder.quality.text = "缺少血氧"
            }
            3 -> {
                holder.name.setTextColor(Color.parseColor("#F45C50"))
                holder.quality.setTextColor(Color.parseColor("#F45C50"))
                holder.quality.text = "无效报告"
            }
        }

        holder.itemView.setOnClickListener {
            if (mClickListener != null){
                mClickListener!!.onItemClick(getItem(position).reportUrl,getItem(position).quality,getItem(position).createTime)
            }
        }
    }


}

class DeviceRecoveryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    val name = itemView.findViewById<TextView>(R.id.recovery_report_list_name)
    val quality = itemView.findViewById<TextView>(R.id.recovery_report_list_quality)
}