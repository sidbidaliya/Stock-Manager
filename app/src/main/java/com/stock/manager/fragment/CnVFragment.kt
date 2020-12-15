package com.stock.manager.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.stock.manager.R
import com.stock.manager.adapter.DealerRecyclerAdapter
import com.stock.manager.database.Database
import com.stock.manager.entities.DEntity
import kotlin.properties.Delegates

class CnVFragment : Fragment() {

    private lateinit var dRecyclerView : RecyclerView
    private lateinit var recyclerAdapter: DealerRecyclerAdapter
    private lateinit var dealerType: TextView

    private var customerList: List<DEntity> = arrayListOf()
    private var vendorList: List<DEntity> = arrayListOf()
    private var dealerList: List<DEntity> = arrayListOf()

    private lateinit var sharedPreferences: SharedPreferences

    var layoutSelect by Delegates.notNull<Int>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreferences = this.activity!!
            .getSharedPreferences(
                getString(R.string.preference_file_name),
                Context.MODE_PRIVATE
            )

        var dealer: String? = ""

        if (arguments != null) {
            dealer = arguments?.getString("params");
        }

        customerList = RetrieveCustomers(context as Context).execute().get()
        vendorList = RetrieveVendors(context as Context).execute().get()

        layoutSelect = if (customerList.isEmpty() && vendorList.isEmpty()){
            R.layout.emtpy_dealer
        } else if (dealer == "Customers" && customerList.isEmpty()){
            R.layout.emtpy_dealer
        } else if (dealer == "Vendors" && vendorList.isEmpty()){
            R.layout.emtpy_dealer
        } else{
            R.layout.fragment_cnv
        }
        val view = inflater.inflate(layoutSelect, container, false)



        if(layoutSelect == R.layout.fragment_cnv){

            dRecyclerView = view.findViewById(R.id.rvDealer)
            dealerType = view.findViewById(R.id.cButton)

            if (dealer == "Customers"){
                dealerType.text = "All Customers are listed below: "
                dealerList = customerList
            }
            if (dealer == "Vendors"){
                dealerType.text = "All Vendors are listed below: "
                dealerList = vendorList
            }
            callAdapter(dealerList)

        }

        return  view
    }

    private fun callAdapter(list: List<DEntity>){

        if (activity != null) {
            recyclerAdapter = DealerRecyclerAdapter(activity as Context, list)
            dRecyclerView.adapter = recyclerAdapter
            dRecyclerView.layoutManager = LinearLayoutManager(activity)
        }

    }

    class RetrieveCustomers(val context: Context) : AsyncTask<Void, Void, List<DEntity>>() {
        override fun doInBackground(vararg params: Void?): List<DEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "dealers-db")
                .build()        //Initialise the database
            return db.dao().getAllCustomers()         //Return the list of all customers
        }
    }

    class RetrieveVendors(val context: Context) : AsyncTask<Void, Void, List<DEntity>>() {
        override fun doInBackground(vararg params: Void?): List<DEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "dealers-db")
                .build()        //Initialise the database
            return db.dao().getAllVendors()         //Return the list of all vendors
        }
    }
}
