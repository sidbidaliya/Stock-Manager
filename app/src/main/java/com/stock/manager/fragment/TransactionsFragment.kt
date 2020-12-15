package com.stock.manager.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.stock.manager.adapter.TransactionRecyclerAdapter
import com.stock.manager.database.Database
import com.stock.manager.R
import com.stock.manager.entities.TEntity
import kotlin.properties.Delegates

class TransactionsFragment : Fragment() {

    private lateinit var rvTransaction : RecyclerView
    private lateinit var recyclerAdapter: TransactionRecyclerAdapter

    private var dbTransactionList = listOf<TEntity>()       //To store the transaction list

    var layoutSelect by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dbTransactionList = RetrieveTransactions(activity as Context).execute().get()

        layoutSelect = if (dbTransactionList.isEmpty()){
            R.layout.empty_transaction
        }else{
            R.layout.fragment_transactions
        }

        val view = inflater.inflate(layoutSelect, container, false)

        if (layoutSelect == R.layout.fragment_transactions){

            rvTransaction = view.findViewById(R.id.rvTransaction)

            if (activity != null) {
                recyclerAdapter = TransactionRecyclerAdapter(activity as Context, dbTransactionList.reversed())
                rvTransaction.adapter = recyclerAdapter
                rvTransaction.layoutManager = LinearLayoutManager(activity)
            }

        }

        return view
    }

    class RetrieveTransactions(val context: Context) : AsyncTask<Void, Void, List<TEntity>>() {
        override fun doInBackground(vararg params: Void?): List<TEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "transactions-db")
                .build()        //Initialise the database
            return db.dao().getAllTransactions()   //Return the list of transactions
        }
    }

}