package com.hzdq.bajiesleepchildrenHD.evaluate.adapter

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
import com.hzdq.bajiesleepchildrenHD.dataclass.DataEvaluateRecordX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontUserX
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.EvaluateRecordNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.evaluate.paging.FrontEvaluateNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.user.paging.FrontUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date
import kotlinx.android.synthetic.main.activity_add_treatment_record.view.*

class EvaluateRecordAdapter(val evaluateViewModel: EvaluateViewModel,val lifecycleOwner: LifecycleOwner) :PagedListAdapter<DataEvaluateRecordX,RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        evaluateViewModel.retryEvaluateRecord()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: EvaluateRecordNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemDetailClickListener? = null

    //设置回调接口
    interface OnItemDetailClickListener {
        fun onItemDetailClick(id: Int)
    }

    fun setOnItemDetailClickListener(listener: OnItemDetailClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: EvaluateRecordNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == EvaluateRecordNetWorkStatus.EVALUATE_RECORD_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_evaluate_record else R.layout.item_evaluate_list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

         return when(viewType){
             R.layout.item_evaluate_list ->{

                 EvaluateRecordViewHolder.newInstance(parent).also { holder->
                     //holder.adapterPosition 是当前item的数据

                 }
             }



             else -> EvaluateRecordFooterViewHolder.newInstance(parent).also {
                 it.itemView.setOnClickListener {
                     evaluateViewModel.retryEvaluateRecord()
                 }
             }


         }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_evaluate_record -> {
                (holder as EvaluateRecordFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as EvaluateRecordViewHolder).bindWithItem(dataItem,position)

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

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataEvaluateRecordX>() {
        override fun areItemsTheSame(oldItem: DataEvaluateRecordX, newItem: DataEvaluateRecordX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataEvaluateRecordX, newItem: DataEvaluateRecordX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }




}



class EvaluateRecordViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    val time = itemView.findViewById<TextView>(R.id.evaluate_list_time)
    val classification = itemView.findViewById<TextView>(R.id.evaluate_list_classification)
    val assessment = itemView.findViewById<TextView>(R.id.evaluate_list_assessment)
    val detail = itemView.findViewById<LinearLayout>(R.id.evaluate_list_detail)
    val back = itemView.findViewById<ConstraintLayout>(R.id.evaluate_list_item_back_layout)

    companion object{
        fun newInstance(parent: ViewGroup):EvaluateRecordViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evaluate_list,parent,false)
            return EvaluateRecordViewHolder(view)
        }
    }
    fun bindWithItem(dataEvaluateRecordX: DataEvaluateRecordX, position: Int){
        with(itemView){
//            back.setBackgroundColor(Color.parseColor("#FFFFFF"))



            time.text = "${timestamp2Date("${dataEvaluateRecordX.createTime}","yyyy/MM/dd")}"
            when(dataEvaluateRecordX.osas){
                1 -> classification.text = "正常"
                2 -> classification.text = "轻度"
                3 -> classification.text = "中度"
                4 -> classification.text = "重度"
                else -> classification.text = "--"
            }

            val list:List<String> = dataEvaluateRecordX.assessment

            if (list.size >= 2){
                assessment.text = "${list[0]}\n${list[1]}"
            }else if (list.size == 1){
                assessment.text = "${list[0]}"
            }else if (list.isEmpty()){
                assessment.text = "--"
            }


//            for(i in 0..dataEvaluateRecordX.assessment.size-1){
//                assessment.text = dataEvaluateRecordX.assessment[i]
//                Log.d("assessment", "bindWithItem:${dataEvaluateRecordX.assessment[i]} ")
//            }

        }
    }
}

class EvaluateRecordFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_evaluate_record)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_evaluate_record)
    companion object{
        fun newInstance(parent: ViewGroup): EvaluateRecordFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_evaluate_record,parent,false)
            return EvaluateRecordFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: EvaluateRecordNetWorkStatus?){

        with(itemView){
            when(netWorkStatus){
                EvaluateRecordNetWorkStatus.EVALUATE_RECORD_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                EvaluateRecordNetWorkStatus.EVALUATE_RECORD_COMPLETED -> {
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


