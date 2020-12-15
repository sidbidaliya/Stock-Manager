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
import com.stock.manager.R
import com.stock.manager.adapter.MinRecyclerAdapter
import com.stock.manager.adapter.ProductRecyclerAdapter
import com.stock.manager.database.Database
import com.stock.manager.entities.PEntity
import com.stock.manager.model.Min
import kotlin.properties.Delegates

class MinFragment : Fragment() {

    lateinit var rvMin: RecyclerView
    lateinit var minAdapter: MinRecyclerAdapter

    private var allProductsList: List<PEntity> = arrayListOf()
    private var minProductsList: ArrayList<Min> = arrayListOf()

    var layoutSelect by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        allProductsList = RetrieveAllProducts(context as Context).execute().get()

        for (i in allProductsList.indices){
            if (allProductsList[i].productQuantity.toInt() <= 5){
                val min = Min(
                    allProductsList[i].productName,
                    allProductsList[i].productQuantity,
                    allProductsList[i].productPrice
                )
                minProductsList.add(min)
            }
        }

        layoutSelect = if (minProductsList.isEmpty()){
            R.layout.empty_limited_stocks
        }else{
            R.layout.fragment_min
        }
        val view = inflater.inflate(layoutSelect, container, false)

        if (layoutSelect == R.layout.fragment_min){

            rvMin = view.findViewById(R.id.rvMin)

            if (activity != null) {
                minAdapter = MinRecyclerAdapter(context as Context, minProductsList)
                rvMin.adapter = minAdapter
                rvMin.layoutManager = LinearLayoutManager(activity)
            }

        }

        return view
    }

    class RetrieveAllProducts(val context: Context) : AsyncTask<Void, Void, List<PEntity>>() {
        override fun doInBackground(vararg params: Void?): List<PEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()        //Initialise the database
            return db.dao().getAllProducts()         //Return the list of all Products
        }
    }

}