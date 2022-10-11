package com.example.coroutines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recyclerview.view.*



class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v){
    val itemIcon = v.item_icon
    val itemData = v.item_data
}

class Data(val profile: Int, val todo: String)

class MyAdapter(val list: ArrayList<Data>) : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val cellForRow = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview,parent,false)
        return CustomViewHolder(cellForRow)
    }

    //클릭 리스너 연결
    interface OnItemClickListener{
        fun onClick(v:View, position: Int)
    }

    var onItemClickListener : OnItemClickListener? = null


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemIcon.setImageResource(list[position].profile)
        holder.itemData.text = list[position].todo
        if(onItemClickListener !=null){
            holder.itemView.setOnClickListener{
                v->onItemClickListener?.onClick(v, position)
            }
        }
    }



    //item count
    override fun getItemCount(): Int {
        return list.size
    }

}