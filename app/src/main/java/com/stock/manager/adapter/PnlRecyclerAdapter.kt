package com.stock.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stock.manager.R
import com.stock.manager.entities.PnLEntity

class PnlRecyclerAdapter(val context: Context, private val transactionList : List<PnLEntity>) : RecyclerView.Adapter<PnlRecyclerAdapter.PnlViewHolder>() {
    class PnlViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cName : TextView = view.findViewById(R.id.cName)
        val date : TextView = view.findViewById(R.id.dt)
        val bPrice : TextView = view.findViewById(R.id.bPrice)
        val sPrice : TextView = view.findViewById(R.id.sPrice)
        val profit : TextView = view.findViewById(R.id.profit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PnlViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pnl_single_row,parent,false)
        return PnlViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: PnlViewHolder, position: Int) {
        val stat = transactionList[position]
        holder.cName.text = stat.cName
        holder.date.text = stat.dateTime
        holder.bPrice.text = stat.bPrice
        holder.sPrice.text = stat.sPrice
        holder.profit.text = stat.total
    }
}