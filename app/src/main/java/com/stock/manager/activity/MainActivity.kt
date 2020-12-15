package com.stock.manager.activity

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.room.Room
import com.stock.manager.R
import com.stock.manager.database.Database
import com.stock.manager.entities.PEntity
import com.stock.manager.fragment.MinFragment
import com.stock.manager.model.Min

class MainActivity : AppCompatActivity() {

    lateinit var products: CardView
    lateinit var transactions: CardView
    private lateinit var pnl: CardView
    private lateinit var limitedStock: CardView
    private lateinit var customers: CardView
    private lateinit var vendors: CardView
    private lateinit var purchaseEntry: CardView
    private lateinit var salesEntry: CardView
    private lateinit var warnImg: ImageView
    private lateinit var newCustomer: ImageView
    lateinit var newVendor: ImageView

    lateinit var toolbar: Toolbar
    private lateinit var sharedPreferences: SharedPreferences

    private var allProductsList: List<PEntity> = arrayListOf()

    private val themeOptions =
        arrayOf<CharSequence>("Light", "Dark")
    private var checkedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_main)

        val isNightModeOn = sharedPreferences.getBoolean("NightMode", false)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        products = findViewById(R.id.products)
        transactions = findViewById(R.id.transactions)
        pnl = findViewById(R.id.profit_loss)
        limitedStock = findViewById(R.id.limited_stock)
        customers = findViewById(R.id.customer)
        vendors = findViewById(R.id.vendors)
        purchaseEntry = findViewById(R.id.purchase_entry)
        salesEntry = findViewById(R.id.sales_entry)
        warnImg = findViewById(R.id.warn)
        newCustomer = findViewById(R.id.new_customer)
        newVendor = findViewById(R.id.new_vendor)

        toolbar = findViewById(R.id.MainToolbar)

        setUpToolbar()

        val newV = sharedPreferences.getBoolean("newVendor",false)
        val newC = sharedPreferences.getBoolean("newCustomer",false)

        if (newV){
            newVendor.visibility = View.VISIBLE
        }else{
            newVendor.visibility = View.GONE
        }

        if (newC){
            newCustomer.visibility = View.VISIBLE
        }else{
            newCustomer.visibility = View.GONE
        }

        allProductsList = MinFragment.RetrieveAllProducts(this).execute().get()
        var warn = 0

        for (i in allProductsList.indices){
            if (allProductsList[i].productQuantity.toInt() <= 5){
                warn++
            }
        }

        if (warn >= 1){
            warnImg.visibility = View.VISIBLE
        }else{
            warnImg.visibility = View.GONE
        }

        products.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "products")
            startActivity(intent)
            finish()
        }

        transactions.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "transactions")
            startActivity(intent)
            finish()
        }

        pnl.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "pnl")
            startActivity(intent)
            finish()
        }

        limitedStock.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "limited_stocks")
            startActivity(intent)
            finish()
        }

        customers.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "customers")
            startActivity(intent)
            finish()
        }

        vendors.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("fragment", "vendors")
            startActivity(intent)
            finish()
        }

        purchaseEntry.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)
            startActivity(intent)
            finish()
        }

        salesEntry.setOnClickListener {
            val intent = Intent(this, SellActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Stock Manager"
    }

    class RetrieveAllProducts(val context: Context) : AsyncTask<Void, Void, List<PEntity>>() {
        override fun doInBackground(vararg params: Void?): List<PEntity> {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()        //Initialise the database
            return db.dao().getAllProducts()         //Return the list of all Products
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme -> {
                val isNightModeOn = sharedPreferences.getBoolean("NightMode", false)

                checkedItem = if (isNightModeOn){
                    1
                }else{
                    0
                }
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Set Theme")
                builder.setSingleChoiceItems(
                    themeOptions, checkedItem
                ) { dialog, which ->
                    // user checked an item
                    when (which) {
                        0 -> {
                            checkedItem = 0
                        }
                        1 -> {
                            checkedItem = 1
                        }
                    }
                }

                builder.setNegativeButton("Cancel") { dialogInterface, i ->
                    //do nothing
                }
                    .setPositiveButton("Ok") { dialogInterface, i ->
                        if (checkedItem == 0) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            sharedPreferences.edit().putBoolean("NightMode", false).apply()
                        } else if (checkedItem == 1) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            sharedPreferences.edit().putBoolean("NightMode", true).apply()
                        }
                        checkedItem = -1
                    }
                builder.create().show()
            }

            R.id.about -> {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.custom_layout_about)
                val window = dialog.window
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                window?.setGravity(Gravity.CENTER_HORIZONTAL)
                dialog.show()

                val closeButton = dialog.findViewById<ImageButton>(R.id.about_close)
                closeButton.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}