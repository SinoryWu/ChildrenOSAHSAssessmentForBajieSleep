package com.hzdq.bajiesleepchildrenHD.user.adapter

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
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class FrontUserAdapter(val userViewModel: UserViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataFrontUserX,RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        Log.d("adpterinit", "frontuseradapter")
        userViewModel.retryFrontUser()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FrontUserNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null

    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(dateItem: DataFrontUserX)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: FrontUserNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FrontUserNetWorkStatus.FRONT_USER_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer else R.layout.item_user_user_list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

         return when(viewType){
             R.layout.item_user_user_list ->{

                 FrontUserViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> FrontUserFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     userViewModel.retryFrontUser()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer -> {
                (holder as FrontUserFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as FrontUserViewHolder).bindWithFrontUserItem(dataItem,position)

                holder.itemView.setOnClickListener {

                    userViewModel.frontUserPosition.value = holder.absoluteAdapterPosition
                    if (mClickListener != null){
                        mClickListener!!.onItemClick(dataItem)
                    }
                }

                userViewModel.frontUserPosition.observe(lifecycleOwner, Observer {
                    if (holder.absoluteAdapterPosition == it){
                        userViewModel.dateItem.value = getItem(it)
                        userViewModel.uid.value = getItem(it)?.uid
                        userViewModel.name.value = getItem(it)?.truename
                        userViewModel.userTreatRecordRefresh.value = 1
                        userViewModel.userFollowUpRefresh.value = 1
                        userViewModel.userQuestionListRefresh.value = 1
                        userViewModel.userReportListRefresh.value = 1
                        holder.itemView.setBackgroundColor(Color.parseColor("#EEF2F8"))
                    }else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                })
            }
        }
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFrontUserX>() {
        override fun areItemsTheSame(oldItem: DataFrontUserX, newItem: DataFrontUserX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFrontUserX, newItem: DataFrontUserX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }




}



class FrontUserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val frontname = itemView.findViewById<TextView>(R.id.user_user_list_item_name)
    val age = itemView.findViewById<TextView>(R.id.user_user_list_item_age)
    val sex = itemView.findViewById<ImageView>(R.id.user_user_list_item_sex)
    val date = itemView.findViewById<TextView>(R.id.user_user_item_date)
    val status = itemView.findViewById<TextView>(R.id.user_list_item_status)
    val back1 = itemView.findViewById<ConstraintLayout>(R.id.user_user_list_item_layout)
    companion object{
        fun newInstance(parent: ViewGroup):FrontUserViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_user_list,parent,false)
            return FrontUserViewHolder(view)
        }
    }
    fun bindWithFrontUserItem(dataFrontUserX: DataFrontUserX, position: Int){
        with(itemView){

            if (dataFrontUserX.truename != null){
                frontname.text = dataFrontUserX.truename
            }

            if (dataFrontUserX.age != null){
                age.text = "${dataFrontUserX.age}岁"
            }

            if (dataFrontUserX.sex != null){
                if (dataFrontUserX.sex.equals("1") || dataFrontUserX.sex.equals("0")){
                    sex.setImageResource(R.mipmap.sex_men_icon)
                }else if (dataFrontUserX.sex.equals("2")){
                    sex.setImageResource(R.mipmap.sex_women_icon)
                }
            }


            if (dataFrontUserX.createTime != null){
                date.text = "创建于${timestamp2Date(dataFrontUserX.createTime.toString(),"MM月dd日")}"
            }

            if (dataFrontUserX.oahiRes != null){
                status.text = dataFrontUserX.oahiRes
                when(dataFrontUserX.oahiRes){
                    "未评估" -> status.setTextColor(Color.parseColor("#96ADDF"))
                    "正常" -> status.setTextColor(Color.parseColor("#6CC291"))
                    "轻度" -> status.setTextColor(Color.parseColor("#596AFD"))
                    "中度" -> status.setTextColor(Color.parseColor("#F39920"))
                    "重度" -> status.setTextColor(Color.parseColor("#F45C50"))
                }
            }


        }
    }
}

class FrontUserFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textViewFooter)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBarFooter)
    companion object{
        fun newInstance(parent: ViewGroup): FrontUserFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer,parent,false)
            return FrontUserFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FrontUserNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                FrontUserNetWorkStatus.FRONT_USER_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FrontUserNetWorkStatus.FRONT_USER_COMPLETED -> {
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


