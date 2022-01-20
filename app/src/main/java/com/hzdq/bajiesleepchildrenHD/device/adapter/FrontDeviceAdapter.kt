package com.hzdq.bajiesleepchildrenHD.device.adapter

import android.graphics.Color
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
import com.hzdq.bajiesleepchildrenHD.device.paging.FrontDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date

class FrontDeviceAdapter(val deviceViewModel: DeviceViewModel, val lifecycleOwner: LifecycleOwner) :
    PagedListAdapter<DataFrontDeviceXX, RecyclerView.ViewHolder>(FrontDeviceAdapter.DIFFCALLBACK) {

    init {
        deviceViewModel.retryFrontDevice()
    }

    //创建一个成员变量来保存网络状态
    private var netWorkStatus: FrontDeviceNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(dateItem: DataFrontDeviceXX)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }



    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: FrontDeviceNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == FrontDeviceNetWorkStatus.FRONT_DEVICE_INITIAL_LOADING){
            hideFooter()
        }else{
            showFooter()
        }
    }





    override fun getItemCount(): Int {
        return super.getItemCount() +if (hasFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        //根据position位置 如果等于最后一个item 返回FOOTER_VIEW_TYPE 否则返回NORMAL_VIEW_TYPE
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_front_device else R.layout.item_front_device_list
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.item_front_device_list ->{

                FrontDeviceViewHolder.newInstance(parent).also { holder->
                    //holder.adapterPosition 是当前item的数据

                }
            }



            else -> FrontDeviceFooterViewHolder.newInstance(parent).also {
                it.itemView.setOnClickListener {
                    deviceViewModel.retryFrontDevice()
                }
            }


        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_front_device -> {
                (holder as FrontDeviceFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as FrontDeviceViewHolder).bindWithFrontDeviceItem(dataItem,position)

                holder.itemView.setOnClickListener {
                    deviceViewModel.frontDevicePosition.value = holder.absoluteAdapterPosition
                    if (mClickListener != null){
                        mClickListener!!.onItemClick(dataItem)
                    }
                }

                //根据position来决定颜色
                deviceViewModel.frontDevicePosition.observe(lifecycleOwner, Observer {
                    if (holder.absoluteAdapterPosition == it){
                        //设备sn
                        deviceViewModel.deviceSn.value = getItem(it)?.sn

                        holder.itemView.setBackgroundColor(Color.parseColor("#EEF2F8"))
                    }else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                })
            }
        }
    }

    object DIFFCALLBACK: DiffUtil.ItemCallback<DataFrontDeviceXX>() {
        override fun areItemsTheSame(oldItem: DataFrontDeviceXX, newItem: DataFrontDeviceXX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataFrontDeviceXX, newItem: DataFrontDeviceXX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }


}

class FrontDeviceViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val frontname = itemView.findViewById<TextView>(R.id.front_device_list_item_name)
    val sn = itemView.findViewById<TextView>(R.id.front_device_list_item_sn)
    val sex = itemView.findViewById<ImageView>(R.id.front_device_list_item_sex)
    val date = itemView.findViewById<TextView>(R.id.front_device_list_item_date)
    val status = itemView.findViewById<TextView>(R.id.front_device_list_item_status)

    companion object{
        fun newInstance(parent: ViewGroup):FrontDeviceViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_front_device_list,parent,false)
            return FrontDeviceViewHolder(view)
        }
    }
    fun bindWithFrontDeviceItem(dataFrontDeviceXX: DataFrontDeviceXX, position: Int){
        with(itemView){
            frontname.text = dataFrontDeviceXX.truename
            sn.text = "${dataFrontDeviceXX.sn}"
            when(dataFrontDeviceXX.status){
                1 -> status.text = "闲置"
                5 -> status.text = "维修中"
                10 -> status.text = "维保中"
                15 -> {
                    if (dataFrontDeviceXX.outTime == 0){
                        status.text = "待回收"
                    }else {
                        status.text = "逾期${dataFrontDeviceXX.outTime}天"
                    }
                }
                20 -> status.text = "已借出"
            }

            if (dataFrontDeviceXX.lastUpdateTime == 0){
                date.visibility = View.INVISIBLE
            }else {
                date.visibility = View.VISIBLE
                date.text = "${timestamp2Date(dataFrontDeviceXX.lastUpdateTime.toString(),"MM/dd HH:mm")}"
            }

        }
    }
}

class FrontDeviceFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.footer_front_device)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_front_device)
    companion object{
        fun newInstance(parent: ViewGroup): FrontDeviceFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_front_device,parent,false)
            return FrontDeviceFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: FrontDeviceNetWorkStatus?){


        with(itemView){
            when(netWorkStatus){
                FrontDeviceNetWorkStatus.FRONT_DEVICE_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                FrontDeviceNetWorkStatus.FRONT_DEVICE_COMPLETED -> {
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
