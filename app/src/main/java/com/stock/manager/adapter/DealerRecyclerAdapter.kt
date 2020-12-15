package com.stock.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stock.manager.R
import com.stock.manager.entities.DEntity

class DealerRecyclerAdapter(val context: Context, private val dealerList : List<DEntity>) : RecyclerView.Adapter<DealerRecyclerAdapter.DealerViewHolder>() {
    class DealerViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dName : TextView = view.findViewById(R.id.txtDName)
        val dAddress : TextView = view.findViewById(R.id.txtDAddress)
        val dNum : TextView = view.findViewById(R.id.txtDNum)
        val dMail : TextView = view.findViewById(R.id.txtDMail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dealer_single_row,parent,false)
        return DealerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dealerList.size
    }

    override fun onBindViewHolder(holder: DealerViewHolder, position: Int) {
        val tran = dealerList[position]
        holder.dName.text = tran.dName
        if (tran.dAddress.isNotEmpty() || tran.dAddress != ""){
            holder.dAddress.text = tran.dAddress
        }
        if (tran.dContact.isNotEmpty() || tran.dContact != ""){
            holder.dNum.text = tran.dContact
        }
        if (tran.dMail.isNotEmpty() || tran.dMail != ""){
            holder.dMail.text = tran.dMail
        }
    }
}