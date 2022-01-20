package com.hzdq.bajiesleepchildrenHD.user.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android.liuzhuang.rcimageview.RoundCornerImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hzdq.bajiesleepchildrenHD.R
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel
import io.supercharge.shimmerlayout.ShimmerLayout

class TreatmentDetailPicAdapter(): RecyclerView.Adapter<TreatmentDetailPicAdapter.MyViewHolder>() {

    private  var list: List<String>? = null
    private var mClickListener: OnItemClickListener? = null



    fun setList(list:List<String>?){

        this.list = list


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_treat_detail_pic,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.shimmer.apply {
            setShimmerColor(0x55FFFFFF) //设置闪烁颜色
            setShimmerAngle(0) //设置闪烁角度
            setShimmerAnimationDuration(600)
            startShimmerAnimation() //开始闪烁
        }

        Glide.with(holder.itemView)
            .load(list?.get(position)) //加载的地址
            .placeholder(R.drawable.treat_detail_initial_background) //加载成功前的占位图片
            .listener(object : RequestListener<Drawable> {
                //图片加载失败
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
                //图片加载成功
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false.also {
                        holder.shimmer?.stopShimmerAnimation()
                    }
                }

            })
            .into(holder.picture)

        holder.itemView.setOnClickListener {
            if (mClickListener!= null){
                mClickListener!!.onItemClick(list?.get(position)!!)
            }
        }

    }

    override fun getItemCount(): Int {
        if (list != null){

            return list?.size!!

        }else {

            return 0
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shimmer = itemView.findViewById<ShimmerLayout>(R.id.treat_detail_shimmer)
        val picture = itemView.findViewById<RoundCornerImageView>(R.id.treat_detail_pic)
    }


    //设置回调接口
    interface OnItemClickListener {
        fun onItemClick(url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mClickListener = listener
    }


}

