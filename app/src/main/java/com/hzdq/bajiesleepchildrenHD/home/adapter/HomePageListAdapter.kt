package com.hzdq.bajiesleepchildrenHD.home.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel

class HomePageListAdapter(val mContext: Context, val homeViewModel : HomeViewModel, val lifecycleOwner: LifecycleOwner, val footerCount :Int): RecyclerView.Adapter<HomePageListAdapter.MyViewHolder>() {

    private  var pagedList: MutableList<MutableList<Int>>? = null
    private  var numbers: MutableList<Int>? = null
    private var mClickListener: OnItemClickListener? = null

    companion object{
       const val TYPE_FOOTER = 1//添加Footer
        const val TYPE_NORMAL = 0//两者都没有添加

    }

    fun setNumbers(numbers:MutableList<Int>?){
        this.numbers = numbers
    }

    override fun getItemViewType(position: Int): Int {


        return if(position == itemCount-1) TYPE_FOOTER else TYPE_NORMAL
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var holder : MyViewHolder
        if (viewType == TYPE_FOOTER){

            holder= MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_list_pages_footer_9,parent,false))





        }else {
            holder  = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_list_pages,parent,false))

        }

        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == itemCount-1) {
            return
        } else{

            holder.number.text = (position+1).toString()
            homeViewModel.currentPage.observe(lifecycleOwner, Observer {
                if (holder.number.text.toString() == it.toString()){
                    holder.number.isSelected = true
                    holder.number.setTextColor(Color.parseColor("#ffffff"))
                }else {
                    holder.number.isSelected = false
                    holder.number.setTextColor(Color.parseColor("#262626"))
                }
            })

            val page =  holder.number.text.toString().toInt()
            holder.number.setOnClickListener {
                homeViewModel.currentPage.value = page
                if (mClickListener != null){
                    mClickListener!!.onItemClick(position+1)
                }
                homeViewModel.position = position+1
            }
//            holder.number.text = numbers?.get(position).toString()
        }


//        homeViewModel.currentPage.observe(lifecycleOwner, Observer {
//
//            numberAdapter.setNumbers(pagedList?.get(position ))
//
//        })

//        numberAdapter.setNumbers(pagedList?.get(position ))
    }

    override fun getItemCount(): Int {

        if (numbers != null){
            return numbers?.size!! + 1
        }else {
            return 0
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listRecyclerView = itemView.findViewById<RecyclerView>(R.id.home_page_list_recyclerview)
        val number = itemView.findViewById<Button>(R.id.home_page_list_number_footer)

    }


    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }



}

