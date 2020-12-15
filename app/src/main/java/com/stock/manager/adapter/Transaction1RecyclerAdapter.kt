package com.stock.manager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stock.manager.R
import com.stock.manager.entities.LEntity

class Transaction1RecyclerAdapter(
    val context: Context,
    private val transaction1List: List<LEntity>
) : RecyclerView.Adapter<Transaction1RecyclerAdapter.Transaction1ViewHolder>() {

    class Transaction1ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pName: TextView = view.findViewById(R.id.proName)
        val pQuantity: TextView = view.findViewById(R.id.proQuantity)
        val pPrice: TextView = view.findViewById(R.id.proPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Transaction1ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_transaction1_single_row, parent, false)
        return Transaction1ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transaction1List.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Transaction1ViewHolder, position: Int) {
        val transaction = transaction1List[position]
        holder.pName.text = transaction.PName
        holder.pQuantity.text = transaction.PQt
        holder.pPrice.text = transaction.PPrice
    }
}
