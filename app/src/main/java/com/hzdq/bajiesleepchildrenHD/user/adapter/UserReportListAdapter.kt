package com.hzdq.bajiesleepchildrenHD.user.adapter

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
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataReportListX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataTreatRecordX
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.paging.UserReportListNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.paging.UserTreatRecordNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class UserReportListAdapter(val userViewModel: UserViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataReportListX,RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        userViewModel.retryUserReportList()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: UserReportListNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null

    //设置回调接口
    interface OnItemClickListener {
        fun onItemDetailClick(id: Int,reportId:String,fileName:String)
    }

    fun setOnItemDetailClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: UserReportListNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == UserReportListNetWorkStatus.USER_REPORT_LIST_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_report_list else R.layout.item_user_report_list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

         return when(viewType){
             R.layout.item_user_report_list ->{

                 UserReportListViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> UserReportListFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     userViewModel.retryUserReportList()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_report_list -> {
                (holder as UserReportListFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as UserReportListViewHolder).bindWithItem(dataItem,position)

                if(position % 2 == 0){
                    holder.back.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }else if(position % 2 == 1){
                    holder.back.setBackgroundColor(Color.parseColor("#F7F7F7"))
                }
                holder.detail.setOnClickListener {
                    if (mClickListener != null){
                        mClickListener!!.onItemDetailClick(getItem(position)?.id!!,getItem(position)?.reportId!!,"${userViewModel.baseName.value}  ${timestamp2Date(getItem(position)?.createTime.toString(),"yyyy-MM-dd  HH:mm")}.pdf")
                    }
                }


            }
        }
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataReportListX>() {
        override fun areItemsTheSame(oldItem: DataReportListX, newItem: DataReportListX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataReportListX, newItem: DataReportListX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }




}



class UserReportListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val time = itemView.findViewById<TextView>(R.id.user_report_list_item_time)
    val content = itemView.findViewById<TextView>(R.id.user_report_list_item_content)
    val detail = itemView.findViewById<LinearLayout>(R.id.user_report_list_item_detail)

    val back = itemView.findViewById<ConstraintLayout>(R.id.user_report_list_item_back)
    companion object{
        fun newInstance(parent: ViewGroup):UserReportListViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_report_list,parent,false)
            return UserReportListViewHolder(view)
        }
    }
    fun bindWithItem(dataReportListX: DataReportListX, position: Int){
        with(itemView){



            time.text = "${timestamp2Date(dataReportListX.createTime.toString(),"yyyy/MM/dd")}"


            var osas = ""

            when(dataReportListX.osas){
                1 -> osas = "正常"
                2 -> osas = "轻度"
                3 -> osas = "中度"
                4 -> osas = "重度"
                else -> osas = ""
            }
            var opinion = ""
            if (dataReportListX.opinion.equals("")){
                opinion = ""
            }else {
                opinion = "${dataReportListX.opinion}。"
            }

            var oahi = ""
            if(dataReportListX.oahi.equals("") && osas.equals("")){
                oahi = ""
            }else if (!dataReportListX.oahi.equals("") && osas.equals("")) {
                oahi = "OAHI：${dataReportListX.oahi}。"
            }else {
                oahi = "OAHI：${dataReportListX.oahi}，${osas}。"
            }

            if (!opinion.equals("")){
                opinion = "内容：${dataReportListX.opinion}。"
            }
            content.text ="${oahi}${opinion}"


        }
    }
}

class UserReportListFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_user_report_list)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_user_report_list)
    companion object{
        fun newInstance(parent: ViewGroup): UserReportListFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_report_list,parent,false)
            return UserReportListFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: UserReportListNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                UserReportListNetWorkStatus.USER_REPORT_LIST_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                UserReportListNetWorkStatus.USER_REPORT_LIST_COMPLETED -> {
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


