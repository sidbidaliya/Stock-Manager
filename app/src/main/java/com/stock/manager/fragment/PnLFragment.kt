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
import com.stock.manager.adapter.PnlRecyclerAdapter
import com.stock.manager.database.Database
import com.stock.manager.R
import com.stock.manager.entities.PnLEntity
import kotlin.properties.Delegates

class PnLFragment : Fragment() {

    private lateinit var rvPnL : RecyclerView
    private lateinit var recyclerAdapter: PnlRecyclerAdapter
    private var dbPnlList = listOf<PnLEntity>()

    var layoutSelect by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dbPnlList = RetrievePnL(activity as Context).execute().get()

        layoutSelect = if (dbPnlList.isEmpty()){
            R.layout.empty_p_n_l
        }else{
            R.layout.fragment_pnl
        }

        val view = inflater.inflate(layoutSelect, container, false)

        if (layoutSelect == R.layout.fragment_pnl){

            rvPnL = view.findViewById(R.id.rvPnL)

            if (activity != null) {
                recyclerAdapter = PnlRecyclerAdapter(activity as Context, dbPnlList.reversed())
                rvPnL.adapter = recyclerAdapter
                rvPnL.layoutManager = LinearLayoutManager(activity)
            }

        }

        return view
    }

    class RetrievePnL(val context: Context) : AsyncTask<Void, Void, List<PnLEntity>>() {
        override fun doInBackground(vararg params: Void?): List<PnLEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "pnl-db")
                .build()        //Initialise the database
            return db.dao().getAllPnL()   //Return the list of transactions
        }
    }
}