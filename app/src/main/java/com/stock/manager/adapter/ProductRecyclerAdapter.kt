package com.stock.manager.adapter

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.stock.manager.R
import com.stock.manager.asyncTask.PAsyncTask
import com.stock.manager.entities.PEntity
import com.stock.manager.model.Transaction

class ProductRecyclerAdapter(
    val context: Context,
    private val transactionList: ArrayList<Transaction>
) : RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pName: TextView = view.findViewById(R.id.product_name)
        val pQt: TextView = view.findViewById(R.id.product_qt)
        val pPrice: TextView = view.findViewById(R.id.product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_product_1_single_row, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val tran = transactionList[position]
        holder.pName.text = "${holder.pName.text} ${tran.tPName}"
        holder.pQt.text = "${holder.pQt.text} ${tran.tPQt}"
        holder.pPrice.text = "${holder.pPrice.text} ${tran.tPPrice}"
    }
}