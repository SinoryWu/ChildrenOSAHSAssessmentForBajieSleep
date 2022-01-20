package com.hzdq.bajiesleepchildrenHD.screen.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.device.adapter.DeviceRecoveryViewHolder
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepViewHolder
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.setting.adapter.SettingAccountViewHolder

class QuestionMarketListAdapter():ListAdapter<DataQuestionMarketList,QuestionMarketListViewHolder>(DIFFCALLBACK) {
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(type: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataQuestionMarketList>() {
        override fun areItemsTheSame(oldItem: DataQuestionMarketList, newItem: DataQuestionMarketList): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.assessmentId === newItem.assessmentId
        }

        override fun areContentsTheSame(oldItem: DataQuestionMarketList, newItem: DataQuestionMarketList): Boolean {
            //判断两个item内容是否相同
            return oldItem.assessmentId == newItem.assessmentId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionMarketListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question_market,parent,false)
        val holder = QuestionMarketListViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: QuestionMarketListViewHolder, position: Int) {
       holder.title.text = getItem(position).title
        if (getItem(position).type == 13 || getItem(position).type == 14){
            holder.open.visibility = View.VISIBLE
        }

        holder.back.setOnClickListener {
            if (mClickListener!= null){
                mClickListener!!.onItemClick(getItem(position).type)
            }

        }
    }


}

class QuestionMarketListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.item_question_market_title)
    val title2 = itemView.findViewById<TextView>(R.id.item_question_market_title2)
    val open = itemView.findViewById<TextView>(R.id.item_question_market_open)
    val back = itemView.findViewById<ConstraintLayout>(R.id.item_question_market_back)
}