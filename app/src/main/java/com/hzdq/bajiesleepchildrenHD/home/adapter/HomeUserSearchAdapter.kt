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
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeBindDeviceUserX
import com.hzdq.bajiesleepchildrenHD.dataclass.DataHomeUserSearchX
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceDeviceNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeBindDeviceUserNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.paging.HomeUserSearchNetWorkStatus
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeBindDeviceViewModel
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel
import com.hzdq.bajiesleepchildrenHD.utils.timestamp2Date


class HomeUserSearchAdapter(val homeViewModel: HomeViewModel, val lifecycleOwner: LifecycleOwner) :
    PagedListAdapter<DataHomeUserSearchX, RecyclerView.ViewHolder>(DIFFCALLBACK) {


    init {
        homeViewModel.retryHomeUserSearch()
    }
    //创建一个成员变量来保存网络状态
    private var netWorkStatus: HomeUserSearchNetWorkStatus?=  null

    //加载第一组数据之前不显示footer 因为图片都会插入到footer前面导致一开始加载出的列表在最底部
    private var hasFooter = false
    private var mClickListener: OnItemClickListener? = null
    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(name:String,itemView: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }

    //根据网络状态更新底部页脚是否显示
    fun updateNetWorkStatus(netWorkStatus: HomeUserSearchNetWorkStatus?){
        this.netWorkStatus = netWorkStatus
        if(netWorkStatus == HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_INITIAL_LOADING){
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
        return if (hasFooter && position == itemCount-1) R.layout.list_footer_home_user_search else R.layout.item_home_user_search
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
            R.layout.item_home_user_search ->{

                HomeUserSearchViewHolder.newInstance(parent).also { holder->
                    //holder.adapterPosition 是当前item的数据

                }
            }



            else -> HomeUserSearchFooterViewHolder.newInstance(parent).also {
                it.itemView.setOnClickListener {
                    homeViewModel.retryHomeUserSearch()
                }
            }


        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.list_footer_home_user_search -> {
                (holder as HomeUserSearchFooterViewHolder).bindWithNetWorkStatus(netWorkStatus)
            }
            else -> {
                val dataItem = getItem(position) ?: return
                (holder as HomeUserSearchViewHolder).bindWithItem(dataItem)

                holder.itemView.setOnClickListener {
                    if (mClickListener != null){
                        mClickListener!!.onItemClick(getItem(position)!!.truename,holder.itemView)
                    }
                }


            }
        }
    }


    object DIFFCALLBACK: DiffUtil.ItemCallback<DataHomeUserSearchX>() {
        override fun areItemsTheSame(oldItem: DataHomeUserSearchX, newItem: DataHomeUserSearchX): Boolean {
            //判断两个item是否相同这里是比较对象是否为同一个对象  ===表示判断是否是同一个对象 ==比较的是内容
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: DataHomeUserSearchX, newItem: DataHomeUserSearchX): Boolean {
            //判断两个item内容是否相同
            return oldItem == newItem
        }

    }



}


class HomeUserSearchViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


    val name = itemView.findViewById<TextView>(R.id.home_user_search_item_name)
    val sex = itemView.findViewById<TextView>(R.id.home_user_search_item_sex)
    val time = itemView.findViewById<TextView>(R.id.home_user_search_item_add_time)



    companion object{
        fun newInstance(parent: ViewGroup):HomeUserSearchViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_user_search,parent,false)
            return HomeUserSearchViewHolder(view)
        }
    }
    fun bindWithItem(dataHomeUserSearchX: DataHomeUserSearchX){
        with(itemView){
            name.text = dataHomeUserSearchX.truename
            when(dataHomeUserSearchX.sex){
                "1" -> sex.text = "男"
                "2" -> sex.text = "女"
                else -> sex.text = "未知"
            }

            time.text = "添加时间：${timestamp2Date(dataHomeUserSearchX.createTime.toString(),"yyyy-MM-dd")}"
        }
    }
}

class HomeUserSearchFooterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.textView_home_user_search)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_home_user_search)
    companion object{
        fun newInstance(parent: ViewGroup): HomeUserSearchFooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_home_user_search,parent,false)
            return HomeUserSearchFooterViewHolder(view)
        }
    }
    fun bindWithNetWorkStatus(netWorkStatus: HomeUserSearchNetWorkStatus?){


        with(itemView){
            when(netWorkStatus){
                HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_FAILED -> {
                    textView.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                HomeUserSearchNetWorkStatus.HOME_USER_SEARCH_USER_COMPLETED -> {
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