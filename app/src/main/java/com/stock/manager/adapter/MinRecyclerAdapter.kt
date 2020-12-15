package com.stock.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stock.manager.R
import com.stock.manager.model.Min

class MinRecyclerAdapter(val context: Context, private val proList : List<Min>) : RecyclerView.Adapter<MinRecyclerAdapter.MinViewHolder>() {
    class MinViewHolder(view: View): RecyclerView.ViewHolder(view){
        val pName : TextView = view.findViewById(R.id.rPName)
        val pQt : TextView = view.findViewById(R.id.rPQt)
        val pPrice : TextView = view.findViewById(R.id.rPPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_product_single_row,parent,false)
        return MinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return proList.size
    }

    override fun onBindViewHolder(holder: MinViewHolder, position: Int) {
        val pro = proList[position]
        holder.pName.text = pro.proName
        holder.pQt.text = pro.proQt
        holder.pPrice.text = pro.proPrice
    }
}