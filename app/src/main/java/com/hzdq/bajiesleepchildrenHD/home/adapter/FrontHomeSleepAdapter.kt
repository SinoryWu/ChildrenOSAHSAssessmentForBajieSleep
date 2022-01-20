package com.hzdq.bajiesleepchildrenHD.home.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontHomeX
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date

class FrontHomeSleepAdapter:ListAdapter<DataFrontHomeX, FrontHomeViewHolder>(DIFFCALLBACK) {

    private var mClickListener: OnItemDetailClickListener? = null

    //设置回调接口
    interface OnItemDetailClickListener {
        fun onItemDetailClick(url: String,fileName:String)
    }

    fun setOnItemDetailClickListener(listener: OnItemDetailClickListener?) {
        mClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrontHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_report_list,parent,false)
        val holder = FrontHomeViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: FrontHomeViewHolder, position: Int) {
       if (position % 2 == 1){
           holder.layout.setBackgroundColor(Color.parseColor("#F7F7F7"))
       }else if (position % 2 == 0){
           holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
       }
        holder.time.text = timestamp2Date(getItem(position).createTime.toString(),"yyyy/MM/dd   HH:mm")


        holder.sn.text = getItem(position).sn

        holder.name.text = getItem(position).truename
        holder.detail.setOnClickListener {
//            mClickListener?.onItemDetailClick("https://www.baidu.com")
//            mClickListener?.onItemDetailClick("61aa93717ef12c0e0b706836","${getItem(position).truename}  ${timestamp2Date(getItem(position).createTime.toString(),"yyyy-MM-dd  HH:mm")}.pdf")
//            Log.d("asdasdasdas", "https://cloud.bajiesleep.com/report?userName=%E6%9D%8E%E4%BF%8A%E5%BC%BA&reportURL=61ca45a15c315310e18ae323&reportCreateTime=1640646002")
//            mClickListener?.onItemDetailClick("https://cloud.bajiesleep.com/report?reportURL=${getItem(position).reportUrl}","${getItem(position).truename}  ${timestamp2Date(getItem(position).createTime.toString(),"yyyy-MM-dd  HH:mm")}.pdf")
            mClickListener?.onItemDetailClick("${getItem(position).reportUrl}","${getItem(position).truename}  ${timestamp2Date(getItem(position).createTime.toString(),"yyyy-MM-dd  HH:mm")}.pdf")

        }

        if (getItem(position).complete == 1){
            holder.textView.setTextColor(Color.parseColor("#2051BD"))
            holder.icon.setImageResource(R.mipmap.right_blue_arrow)
            holder.oahi.setTextColor(Color.parseColor("#262626"))
            holder.detail.isClickable = true
        }else {
            holder.textView.setTextColor(Color.parseColor("#C1C1C1"))
            holder.icon.setImageResource(R.mipmap.sleep_report_detail_false_icon)
            holder.oahi.setTextColor(Color.parseColor("#F45C50"))
            holder.detail.isClickable = false
        }

        when(getItem(position).complete){
            1 ->  holder.oahi.text = getItem(position).ahi
            0 ->  holder.oahi.text = "未更新"
            2 ->  holder.oahi.text = "缺少血氧"
            3 ->  holder.oahi.text = "睡眠时间过短"
            4 ->  holder.oahi.text = "检测时间过短"
            5 ->  holder.oahi.text = "无效报告"
        }

    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFrontHomeX>() {
        override fun areItemsTheSame(oldItem: DataFrontHomeX, newItem: DataFrontHomeX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFrontHomeX, newItem: DataFrontHomeX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }


}

class FrontHomeViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    val layout = itemView.findViewById<ConstraintLayout>(R.id.home_list_report_item_layout)
    val time = itemView.findViewById<TextView>(R.id.home_list_report_item_time)
    val sn = itemView.findViewById<TextView>(R.id.home_list_report_item_sn)
    val oahi = itemView.findViewById<TextView>(R.id.home_list_report_item_oahi)
    val name = itemView.findViewById<TextView>(R.id.home_list_report_item_name)
    val detail = itemView.findViewById<LinearLayout>(R.id.home_report_list_item_detail)
    val textView = itemView.findViewById<TextView>(R.id.home_report_list_item_text)
    val icon = itemView.findViewById<ImageView>(R.id.home_report_list_item_icon)



}
