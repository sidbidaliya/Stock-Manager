package com.stock.manager.fragment

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stock.manager.adapter.ProductRecyclerAdapter
import com.stock.manager.database.Database
import com.stock.manager.R
import com.stock.manager.activity.PurchaseActivity
import com.stock.manager.entities.PEntity
import com.stock.manager.model.Transaction
import kotlin.properties.Delegates


class ProductsFragment : Fragment() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var productsAdapter: ProductRecyclerAdapter
    private lateinit var fab : FloatingActionButton

    private var productsList: ArrayList<Transaction> = arrayListOf()

    var layoutSelect by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val list = RetrieveAllProducts(context as Context).execute().get()

        layoutSelect = if (list.isEmpty()){
            R.layout.empty_product
        }else{
            R.layout.fragment_products
        }
        val view = inflater.inflate(layoutSelect, container, false)

        if(layoutSelect == R.layout.fragment_products){

            rvProducts = view.findViewById(R.id.rvProducts)
            fab = view.findViewById(R.id.addProFAB)

            fab.setOnClickListener {
                startActivity(Intent(context, PurchaseActivity::class.java))
            }

            for (i in list.indices){
                val onePro = Transaction(
                    list[i].productName,
                    list[i].productQuantity,
                    list[i].productPrice
                )
                productsList.add(onePro)
            }

            if (activity != null) {
                productsAdapter = ProductRecyclerAdapter(context as Context, productsList)
                rvProducts.adapter = productsAdapter
                rvProducts.layoutManager = LinearLayoutManager(activity)
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