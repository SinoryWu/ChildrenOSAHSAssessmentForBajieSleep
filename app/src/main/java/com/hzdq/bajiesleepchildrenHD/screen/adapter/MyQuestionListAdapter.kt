package com.hzdq.bajiesleepchildrenHD.screen.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataMyQuestionList
import com.hzdq.bajiesleepchildrenHD.dataclass.ReportRecoverDeviceInfo
import com.hzdq.bajiesleepchildrenHD.device.adapter.DeviceRecoveryViewHolder
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepViewHolder
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.setting.adapter.SettingAccountViewHolder

class MyQuestionListAdapter(val  screenViewModel: ScreenViewModel, val lifecycleOwner: LifecycleOwner):ListAdapter<DataMyQuestionList,MyQuestionListViewHolder>(DIFFCALLBACK) {


    object DIFFCALLBACK: DiffUtil.ItemCallback<DataMyQuestionList>() {
        override fun areItemsTheSame(oldItem: DataMyQuestionList, newItem: DataMyQuestionList): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.assessmentId === newItem.assessmentId
        }

        override fun areContentsTheSame(oldItem: DataMyQuestionList, newItem: DataMyQuestionList): Boolean {
            //判断两个item内容是否相同
            return oldItem.assessmentId == newItem.assessmentId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestionListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_question,parent,false)
        val holder = MyQuestionListViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: MyQuestionListViewHolder, position: Int) {
        when(getItem(position).assessmentId){
            26 -> holder.title.text = "CAT量表问卷"
            27 -> holder.title.text = "PHQ量表问卷"
            28 -> holder.title.text = "GAD量表问卷"
            35 -> holder.title.text = "急性加重问卷"
            42 -> holder.title.text = "中医体质量表问卷"
            109 -> holder.title.text = "烟草依赖评估量表问卷"
            116 -> holder.title.text = "烟草戒断症状量表问卷"
            126 -> holder.title.text = "PSQ问卷"
            149 -> holder.title.text = "OSA-18问卷"
        }

        holder.total.text = "${getItem(position).attend_num}人已测"
        holder.questionNumber.text = "共${getItem(position).topic_mum}问，约${getItem(position).topic_time}分钟"

        holder.itemView.setOnClickListener {
            screenViewModel.myQuestionPosition.value = holder.absoluteAdapterPosition
//            if (mClickListener != null){
////                getItem(position)?.id?.let { it1 -> mClickListener!!.onItemClick(it1) }
//
//            }
        }

        screenViewModel.myQuestionPosition.observe(lifecycleOwner, Observer {

            if (holder.absoluteAdapterPosition == it){
                screenViewModel.myQuestionType.value = getItem(it)?.type
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF2F8"))
            }else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        })
    }


}

class MyQuestionListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.item_my_question_title)
    val questionNumber = itemView.findViewById<TextView>(R.id.item_my_question_question_number)
    val total = itemView.findViewById<TextView>(R.id.item_my_question_total)
}