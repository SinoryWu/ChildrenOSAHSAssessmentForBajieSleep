package com.hzdq.bajiesleepchildrenHD.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceDeviceXX
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel


class HomeBindDeviceDeviceAdapter(val homeBindDeviceViewModel: HomeBindDeviceViewModel, val lifecycleOwner: LifecycleOwner) :
    PagedListAdapter<DataHomeBindDeviceDeviceXX, RecyclerView.ViewHolder>(DIFFCALLBACK) {


    init {
        homeBindDeviceViewModel.retryHomeBindDeviceDevice()
    }
    //创建一个成员变量来保存网络状态
    private var netWorkStatus: HomeBindDeviceDeviceNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(sn: String,status:Int,itemView: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: HomeBindDeviceDeviceNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.layout_footer_home_bind_device_device else R.layout.item_home_bind_device_device
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
            R.layout.item_home_bind_device_device ->{

                HomeBindDeviceDeviceViewHolder.newInstance(parent).also { holder->
                    //holder.adapterPosition 是当前item的数据

                }
            }



            else -> HomeBindDeviceDeviceFooterViewHolder.newInstance(parent).also {
                it.itemView.setOnClickListener {
                    homeBindDeviceViewModel.retryHomeBindDeviceDevice()
                }
            }


        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.layout_footer_home_bind_device_device -> {
                (holder as HomeBindDeviceDeviceFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as HomeBindDeviceDeviceViewHolder).bindWithItem(dataItem)

                holder.itemView.setOnClickListener {
                    if (mClickListener != null){
                        mClickListener!!.onItemClick(getItem(position)?.sn!!,getItem(position)?.status!!,holder.itemView)
                    }
                }


            }
        }
    }


    object DIFFCALLBACK: DiffUtil.ItemCallback<DataHomeBindDeviceDeviceXX>() {
        override fun areItemsTheSame(oldItem: DataHomeBindDeviceDeviceXX, newItem: DataHomeBindDeviceDeviceXX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataHomeBindDeviceDeviceXX, newItem: DataHomeBindDeviceDeviceXX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }



}


class HomeBindDeviceDeviceViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    val sn = itemView.findViewById<TextView>(R.id.home_bind_device_device_sn)
    var name = itemView.findViewById<TextView>(R.id.home_bind_device_device_name)


    companion object{
        fun newInstance(parent: ViewGroup):HomeBindDeviceDeviceViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_bind_device_device,parent,false)
            return HomeBindDeviceDeviceViewHolder(view)
        }
    }
    fun bindWithItem(homeBindDeviceDeviceXX: DataHomeBindDeviceDeviceXX){
        with(itemView){
            sn.text = "${homeBindDeviceDeviceXX.sn}"
            when(homeBindDeviceDeviceXX.status){
                1 ->  name.text = " 闲置"
                5 -> name.text = "维修中"
                10 -> name.text = "维保中"
                15 ->{
                    if (homeBindDeviceDeviceXX.outTime == 0){
                        name.text = "${homeBindDeviceDeviceXX.truename}    待回收"
                    }else {
                        name.text = "${homeBindDeviceDeviceXX.truename}    逾期${homeBindDeviceDeviceXX.outTime}天"
                    }
                }
                20 -> name.text = "${homeBindDeviceDeviceXX.truename}    已借出"
            }


        }
    }
}

class HomeBindDeviceDeviceFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.footer_home_bind_device_device)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_home_bind_device_device)
    companion object{
        fun newInstance(parent: ViewGroup): HomeBindDeviceDeviceFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_footer_home_bind_device_device,parent,false)
            return HomeBindDeviceDeviceFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: HomeBindDeviceDeviceNetWorkStatus?){


        with(itemView){
            when(netWorkStatus){
                HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                HomeBindDeviceDeviceNetWorkStatus.HOME_BIND_DEVICE_DEVICE_COMPLETED -> {
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