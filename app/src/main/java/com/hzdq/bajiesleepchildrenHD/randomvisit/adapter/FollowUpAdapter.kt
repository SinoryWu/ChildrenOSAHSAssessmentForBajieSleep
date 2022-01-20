package com.hzdq.bajiesleepchildrenHD.randomvisit.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFollowUpListX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFollowUpX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.paging.UserFollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.paging.UserTreatRecordNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.Shp
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class FollowUpAdapter(val randomViewModel: RandomViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataFollowUpX,RecyclerView.ViewHolder>(DIFFCALLBACK){


    init {
        randomViewModel.retryFollowUp()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FollowUpNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null

    //设置回调接口
    interface OnItemClickListener {
        fun onItemDetailClick(id: Int)
    }

    fun setOnItemDetailClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: FollowUpNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FollowUpNetWorkStatus.FOLLOW_UP_INITIAL_LOADING){
            hideFooter()
        }else{
            showFooter()
        }
    }


    //写两个函数根据网络状态决定显示footer或者不显示footer
    private fun hideFooter(){
        if(hasFooter){
            notifyItemRemoved(itemCount - 1)
        }
        hasFooter = false
    }
    private fun showFooter(){
        if (hasFooter){
            notifyItemChanged(itemCount - 1)
        }else {
            hasFooter = true
            notifyItemInserted(itemCount - 1)
        }
    }
    override fun getItemCount(): Int {
        return super.getItemCount() +if (hasFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        //根据position位置 如果等于最后一个item 返回FOOTER_VIEW_TYPE 否则返回NORMAL_VIEW_TYPE
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_follow_up else R.layout.item_follow_up
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


         return when(viewType){
             R.layout.item_follow_up ->{

                 FollowUpViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> FollowUpFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     randomViewModel.retryFollowUp()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_follow_up -> {
                (holder as FollowUpFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as FollowUpViewHolder).bindWithItem(dataItem,position)

                if(position % 2 == 0){
                    holder.back.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }else if(position % 2 == 1){
                    holder.back.setBackgroundColor(Color.parseColor("#F7F7F7"))
                }
                holder.detail.setOnClickListener {
                    if (mClickListener != null){
                        mClickListener!!.onItemDetailClick(getItem(position)?.id!!)
                    }
                }


            }
        }
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFollowUpX>() {
        override fun areItemsTheSame(oldItem: DataFollowUpX, newItem: DataFollowUpX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFollowUpX, newItem: DataFollowUpX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }




}



class FollowUpViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val time = itemView.findViewById<TextView>(R.id.item_follow_up_time)
    val name_follow_up = itemView.findViewById<TextView>(R.id.item_follow_up_name)
    val oahi_follow_up = itemView.findViewById<TextView>(R.id.item_follow_up_oahi)
    val detail = itemView.findViewById<LinearLayout>(R.id.follow_up_item_detail)
    val back = itemView.findViewById<ConstraintLayout>(R.id.item_follow_up_back)

    companion object{
        fun newInstance(parent: ViewGroup):FollowUpViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_up,parent,false)
            return FollowUpViewHolder(view)
        }
    }
    fun bindWithItem(dataFollowUpX: DataFollowUpX, position: Int){
        with(itemView){


            if (dataFollowUpX.createTime != null){
                time.text = "${timestamp2Date(dataFollowUpX.createTime.toString(),"yyyy/MM/dd")}"
            }





            if (dataFollowUpX.doctorName.equals("")){
                name_follow_up.text ="--"
            }else {
                name_follow_up.text = dataFollowUpX.doctorName
            }


            if (dataFollowUpX.oahi.equals("")){
                oahi_follow_up.text = "--"
            }else {
                oahi_follow_up.text = "OAHI：${dataFollowUpX.oahi}，${dataFollowUpX.oahiRes}"
            }

        }
    }
}

class FollowUpFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_follow_up)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_follow_up)
    companion object{
        fun newInstance(parent: ViewGroup): FollowUpFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_follow_up,parent,false)
            return FollowUpFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FollowUpNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                FollowUpNetWorkStatus.FOLLOW_UP_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FollowUpNetWorkStatus.FOLLOW_UP_COMPLETED -> {
                    textView.text = "全部加载完成"
                    progressBar.visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    textView.text = "正在加载"
                    progressBar.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
    }


}


