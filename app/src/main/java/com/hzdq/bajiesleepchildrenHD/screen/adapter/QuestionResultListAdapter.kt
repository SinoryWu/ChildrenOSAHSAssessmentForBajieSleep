package com.hzdq.bajiesleepchildrenHD.screen.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeReportSleepX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataQuestionResultListX
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepReportListAdapter
import com.hzdq.bajiesleepchildrenHD.home.adapter.HomeSleepViewHolder
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date

class QuestionResultListAdapter: ListAdapter<DataQuestionResultListX, QuestionResultListViewHolder>(DIFFCALLBACK) {
    private var mClickListener: OnItemDetailClickListener? = null
    //设置回调接口
    interface OnItemDetailClickListener {
        fun onItemDetailClick(taskid: Int,type:Int)
    }

    fun setOnItemDetailClickListener(listener: OnItemDetailClickListener?) {
        mClickListener = listener
    }



    object DIFFCALLBACK: DiffUtil.ItemCallback<DataQuestionResultListX>() {
        override fun areItemsTheSame(oldItem: DataQuestionResultListX, newItem: DataQuestionResultListX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataQuestionResultListX, newItem: DataQuestionResultListX): Boolean {
            //判断两个item内容是否相同
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionResultListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screen_result_list,parent,false)
        val holder = QuestionResultListViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: QuestionResultListViewHolder, position: Int) {
        if (position % 2 == 1){
            holder.layout.setBackgroundColor(Color.parseColor("#F7F7F7"))
        }else if (position % 2 == 0){
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.time.text = timestamp2Date(getItem(position).createTime.toString(),"yyyy/MM/dd")
        holder.name.text = getItem(position).truename
        if (getItem(position).total != null || getItem(position).total != 0){
            holder.score.text = "${getItem(position).total}分，${getItem(position).result}"
        }else {
            holder.score.text = getItem(position).result
        }
        holder.detail.setOnClickListener {
            if (mClickListener != null){
                mClickListener!!.onItemDetailClick(getItem(position).id,getItem(position).type)
            }
        }


    }
}

class QuestionResultListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout = itemView.findViewById<ConstraintLayout>(R.id.screen_result_list_result_item_layout)
    val time = itemView.findViewById<TextView>(R.id.screen_result_list_result_item_time)
    val name = itemView.findViewById<TextView>(R.id.screen_result_list_result_item_name)
    val score = itemView.findViewById<TextView>(R.id.screen_result_list_result_item_score)
    val detail = itemView.findViewById<LinearLayout>(R.id.screen_result_list_result_item_detail)
}