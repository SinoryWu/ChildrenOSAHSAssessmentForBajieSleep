package com.hzdq.bajiesleepchildrenHD.randomvisit.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontFollowUpX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.randomvisit.paging.FrontFollowUpNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class FrontFollowUpAdapter(val randomViewModel: RandomViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataFrontFollowUpX,RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        randomViewModel.retryFrontFollowUp()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FrontFollowUpNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null

    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(dateItem: DataFrontFollowUpX)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: FrontFollowUpNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FrontFollowUpNetWorkStatus.FRONT_FOLLOW_UP_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_front_follow_up else R.layout.item_front_follow_up_list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

         return when(viewType){
             R.layout.item_front_follow_up_list ->{

                 FrontFollowUpViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> FrontFollowUpFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     randomViewModel.retryFrontFollowUp()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_front_follow_up -> {
                (holder as FrontFollowUpFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
           else -> {
                val dataItem = getItem(position) ?: return
                (holder as FrontFollowUpViewHolder).bindWithItem(dataItem,position)

                holder.itemView.setOnClickListener {

                    randomViewModel.frontFollowUpPosition.value = holder.absoluteAdapterPosition
//                    if (mClickListener != null){
//                        mClickListener!!.onItemClick(dataItem)
//                    }
                }

                randomViewModel.frontFollowUpPosition.observe(lifecycleOwner, Observer {
                    if (holder.absoluteAdapterPosition == it){
//                        randomViewModel.dateItem.value = getItem(it)
                        randomViewModel.uid.value = getItem(it)?.uid
                        randomViewModel.name.value = getItem(it)?.truename
//                        randomViewModel.userTreatRecordRefresh.value = 1
                        randomViewModel.followUpRefresh.value = 1
                        holder.back.setBackgroundColor(Color.parseColor("#EEF2F8"))
                    }else {
                        holder.back.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                })
            }
        }
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFrontFollowUpX>() {
        override fun areItemsTheSame(oldItem: DataFrontFollowUpX, newItem: DataFrontFollowUpX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFrontFollowUpX, newItem: DataFrontFollowUpX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }




}



class FrontFollowUpViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val frontname = itemView.findViewById<TextView>(R.id.front_follow_up_list_item_name)
    val next = itemView.findViewById<TextView>(R.id.front_follow_up_list_item_next)
    val sex = itemView.findViewById<ImageView>(R.id.front_follow_up_list_item_sex)

    val back = itemView.findViewById<ConstraintLayout>(R.id.front_follow_up_list_item_layout)
    companion object{
        fun newInstance(parent: ViewGroup):FrontFollowUpViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_front_follow_up_list,parent,false)
            return FrontFollowUpViewHolder(view)
        }
    }
    fun bindWithItem(dataFrontFollowUpX: DataFrontFollowUpX, position: Int){
        with(itemView){


            if (dataFrontFollowUpX.truename != null){
                frontname.text = dataFrontFollowUpX.truename
            }


            if (dataFrontFollowUpX.sex != null){
                if (dataFrontFollowUpX.sex.equals("1") || dataFrontFollowUpX.sex.equals("0")){
                    sex.setImageResource(R.mipmap.sex_men_icon)
                }else if (dataFrontFollowUpX.sex.equals("2")){
                    sex.setImageResource(R.mipmap.sex_women_icon)
                }
            }



            if (dataFrontFollowUpX.next != null){
                if (dataFrontFollowUpX.next == 0){
                    next.text = "下次随访：--"
                }else {
                    next.text = "下次随访：${timestamp2Date(dataFrontFollowUpX.next.toString(),"yyyy/MM/dd")}"
                }
            }


        }
    }
}

class FrontFollowUpFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_front_follow_up)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_front_follow_up)
    companion object{
        fun newInstance(parent: ViewGroup): FrontFollowUpFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_front_follow_up,parent,false)
            return FrontFollowUpFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FrontFollowUpNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                FrontFollowUpNetWorkStatus.FRONT_FOLLOW_UP_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FrontFollowUpNetWorkStatus.FRONT_FOLLOW_UP_COMPLETED -> {
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


