package com.hzdq.bajiesleepchildrenHD.screen.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontDeviceXX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataFrontScreenX
import com.hzdq.bajiesleepchildrenHD.device.adapter.FrontDeviceAdapter
import com.hzdq.bajiesleepchildrenHD.device.adapter.FrontDeviceFooterViewHolder
import com.hzdq.bajiesleepchildrenHD.device.adapter.FrontDeviceViewHolder
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.screen.paging.FrontScreenNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date

class FrontScreenAdapter(val screenViewModel: ScreenViewModel, val lifecycleOwner: LifecycleOwner)  :
    PagedListAdapter<DataFrontScreenX, RecyclerView.ViewHolder>(DIFFCALLBACK){

    init {
        screenViewModel.retryFrontScreen()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FrontScreenNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: FrontScreenNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FrontScreenNetWorkStatus.FRONT_SCREEN_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_front_screen else R.layout.item_screen_task
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.item_screen_task -> {

                FrontScreenViewHolder.newInstance(parent).also { holder->
                    //holder.adapterPosition 是当前item的数据

                }
            }

            else -> {
                FrontScreenFooterViewHolder.newInstance(parent).also {
                    it.itemView.setOnClickListener {
                        screenViewModel.retryFrontScreen()
                    }
                }
            }



        }

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_front_screen -> {
                (holder as FrontScreenFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as FrontScreenViewHolder).bindWithFrontScreenItem(dataItem,position)
                holder.itemView.setOnClickListener {
                    screenViewModel.screenFrontPosition.value = holder.absoluteAdapterPosition
                    if (mClickListener != null){
                        getItem(position)?.id?.let { it1 -> mClickListener!!.onItemClick(it1) }

                    }
                }

                screenViewModel.screenFrontPosition.observe(lifecycleOwner, Observer {

                    if (holder.absoluteAdapterPosition == it){
                        screenViewModel.taskId.value = getItem(it)?.id
                        holder.itemView.setBackgroundColor(Color.parseColor("#EEF2F8"))
                    }else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                })
            }
        }
    }



    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFrontScreenX>() {
        override fun areItemsTheSame(oldItem: DataFrontScreenX, newItem: DataFrontScreenX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFrontScreenX, newItem: DataFrontScreenX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }


}

class FrontScreenViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val title = itemView.findViewById<TextView>(R.id.screen_task_quesion)
    val number = itemView.findViewById<TextView>(R.id.screen_task_test_person_count)
    val endTime = itemView.findViewById<TextView>(R.id.screen_task_end_time)
    val status = itemView.findViewById<TextView>(R.id.screen_task_status)

    companion object{
        fun newInstance(parent: ViewGroup):FrontScreenViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screen_task,parent,false)
            return FrontScreenViewHolder(view)
        }
    }
    fun bindWithFrontScreenItem(dataFrontScreenX: DataFrontScreenX, position: Int){
        with(itemView){

            title.text = dataFrontScreenX.name
            number.text = "${dataFrontScreenX.num}人已测"
            endTime.text = "结束：${timestamp2Date(dataFrontScreenX.end,"yyy/MM/dd")}"

            when(dataFrontScreenX.status){
                1 -> status.text = "未开始"
                2 -> status.text = "进行中"
                3 -> status.text = "已结束"
            }

        }
    }
}

class FrontScreenFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.footer_front_screen)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_front_screen)
    companion object{
        fun newInstance(parent: ViewGroup): FrontScreenFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_front_screen,parent,false)
            return FrontScreenFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FrontScreenNetWorkStatus?){


        with(itemView){
            when(netWorkStatus){
                FrontScreenNetWorkStatus.FRONT_SCREEN_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FrontScreenNetWorkStatus.FRONT_SCREEN_COMPLETED -> {
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