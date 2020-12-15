package com.stock.manager.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.R
import com.stock.manager.entities.LEntity
import com.stock.manager.entities.TEntity

class TransactionRecyclerAdapter(val context: Context, private val transactionList : List<TEntity>) : RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View): RecyclerView.ViewHolder(view){
        var dName : TextView = view.findViewById(R.id.dealerName)
        val tDate : TextView = view.findViewById(R.id.dateTime)
        val rvTransaction1 : RecyclerView = view.findViewById(R.id.rvTransaction1)
        val amount : TextView = view.findViewById(R.id.amt)
        lateinit var recyclerAdapter: Transaction1RecyclerAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_transaction_single_row,parent,false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.tDate.text = transaction.TDate + "|" + transaction.TTime
        holder.amount.text = transaction.TSum

        if(transaction.TMode == "Buy"){
            holder.dName.text = "Vendor: ${transaction.DName}"
        }else{
            holder.dName.text = "Customer: ${transaction.DName}"
        }


        val list = RetrieveTransactionsList(context, transaction.ListId).execute().get()

        holder.recyclerAdapter = Transaction1RecyclerAdapter(context, list)
        holder.rvTransaction1.adapter = holder.recyclerAdapter
        holder.rvTransaction1.layoutManager = LinearLayoutManager(context)
    }

    class RetrieveTransactionsList(val context: Context, private val key: String) : AsyncTask<Void, Void, List<LEntity>>() {
        override fun doInBackground(vararg params: Void?): List<LEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "lists-db")
                .build()        //Initialise the database
            return db.dao().getTransactionHistory(key)   //Return the list of transactions
        }
    }

}
