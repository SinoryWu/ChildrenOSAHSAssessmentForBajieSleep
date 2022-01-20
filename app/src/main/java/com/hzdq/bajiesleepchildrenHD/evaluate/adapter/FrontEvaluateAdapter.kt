package com.hzdq.bajiesleepchildrenHD.evaluate.adapter

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
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.FrontEvaluateNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class FrontEvaluateAdapter(val evaluateViewModel: EvaluateViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataFrontUserX,RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        evaluateViewModel.retryFrontEvaluate()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FrontEvaluateNetWorkStatus?=  null

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
    fun updateNetWorkStatus(netWorkStatus: FrontEvaluateNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FrontEvaluateNetWorkStatus.FRONT_EVALUATE_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_front_evaluate else R.layout.item_front_evaluate_list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

         return when(viewType){
             R.layout.item_front_evaluate_list ->{

                 FrontEvaluateViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> FrontEvaluateFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     evaluateViewModel.retryFrontEvaluate()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_front_evaluate -> {
                (holder as FrontEvaluateFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as FrontEvaluateViewHolder).bindWithFrontEvaluateItem(dataItem,position)

                holder.itemView.setOnClickListener {

                    evaluateViewModel.frontEvaluatePosition.value = holder.absoluteAdapterPosition
                    if (mClickListener != null){
                        mClickListener!!.onItemClick(dataItem)
                    }
                }

                evaluateViewModel.frontEvaluatePosition.observe(lifecycleOwner, Observer {
                    if (holder.absoluteAdapterPosition == it){
                        evaluateViewModel.dateItem.value = getItem(it)
                        evaluateViewModel.patient_id.value = getItem(it)?.uid
                        evaluateViewModel.name.value = getItem(it)?.truename

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



class FrontEvaluateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val frontname = itemView.findViewById<TextView>(R.id.front_evaluate_list_item_name)
    val age = itemView.findViewById<TextView>(R.id.front_evaluate_list_item_age)
    val sex = itemView.findViewById<ImageView>(R.id.front_evaluate_list_item_sex)
    val date = itemView.findViewById<TextView>(R.id.front_evaluate_item_date)
    val status = itemView.findViewById<TextView>(R.id.front_evaluate_list_item_status)
    val back = itemView.findViewById<ConstraintLayout>(R.id.front_evaluate_list_item_layout)
    companion object{
        fun newInstance(parent: ViewGroup):FrontEvaluateViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_front_evaluate_list,parent,false)
            return FrontEvaluateViewHolder(view)
        }
    }
    fun bindWithFrontEvaluateItem(dataFrontUserX: DataFrontUserX, position: Int){
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

class FrontEvaluateFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_front_evaluate)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_front_evaluate)
    companion object{
        fun newInstance(parent: ViewGroup): FrontEvaluateFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_front_evaluate,parent,false)
            return FrontEvaluateFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FrontEvaluateNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                FrontEvaluateNetWorkStatus.FRONT_EVALUATE_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FrontEvaluateNetWorkStatus.FRONT_EVALUATE_COMPLETED -> {
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


