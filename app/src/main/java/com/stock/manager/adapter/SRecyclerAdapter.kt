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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.stock.manager.R
import com.stock.manager.model.Transaction

class SRecyclerAdapter(val context: Context, private val transactionList: ArrayList<Transaction>) :
    RecyclerView.Adapter<SRecyclerAdapter.SViewHolder>() {
    class SViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pName: TextView = view.findViewById(R.id.rPName)
        val pQt: TextView = view.findViewById(R.id.rPQt)
        val pPrice: TextView = view.findViewById(R.id.rPPrice)
        val delete: ImageView = view.findViewById(R.id.item_delete)
        val edit: ImageView = view.findViewById(R.id.item_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_product_single_row, parent, false)
        return SViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SViewHolder, position: Int) {
        val tran = transactionList[position]
        holder.pName.text = tran.tPName
        holder.pQt.text = tran.tPQt
        holder.pPrice.text = tran.tPPrice
        holder.delete.setOnClickListener {
            transactionList.remove(tran)
            notifyDataSetChanged()
        }

        holder.edit.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.edit_item_layout)
            val window = dialog.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            window?.setGravity(Gravity.CENTER_HORIZONTAL)
            dialog.show()

            val name = dialog.findViewById<TextInputEditText>(R.id.item_Name)
            val qt = dialog.findViewById<TextInputEditText>(R.id.item_Qt)
            val price = dialog.findViewById<TextInputEditText>(R.id.item_Price)
            val update = dialog.findViewById<Button>(R.id.item_update)

            name.setText(tran.tPName)
            qt.setText(tran.tPQt)
            price.setText(tran.tPPrice)

            update.setOnClickListener {
                if (name.editableText.isEmpty()) {
                    name.error = "Enter Name"
                    name.requestFocus()
                    return@setOnClickListener
                }

                if (qt.editableText.isEmpty()) {
                    qt.error = "Enter Quantity"
                    qt.requestFocus()
                    return@setOnClickListener
                }

                if (price.editableText.isEmpty()) {
                    price.error = "Enter Price"
                    price.requestFocus()
                    return@setOnClickListener
                }

                tran.tPName = name.editableText.toString()
                tran.tPQt = qt.editableText.toString()
                tran.tPPrice = price.editableText.toString()

                notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }
}
