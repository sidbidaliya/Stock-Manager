package com.stock.manager.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.stock.manager.adapter.SRecyclerAdapter
import com.stock.manager.R
import com.stock.manager.asyncTask.*
import com.stock.manager.database.Database
import com.stock.manager.entities.*
import com.stock.manager.model.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SellActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var sDate: TextView
    private lateinit var sName: AutoCompleteTextView
    private lateinit var sQuantity: TextInputEditText
    private lateinit var sPrice: TextInputEditText
    lateinit var cName: AutoCompleteTextView
    lateinit var sFAB: FloatingActionButton
    lateinit var btnAdds: Button

    lateinit var sRecyclerView: RecyclerView
    lateinit var sRecyclerAdapter: SRecyclerAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private var transactionList: ArrayList<Transaction> = arrayListOf()
    private var customerList: List<String> = arrayListOf()
    private var productsList: List<String> = arrayListOf()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_sell)

        sName = findViewById(R.id.sName)
        sQuantity = findViewById(R.id.sQt)
        sPrice = findViewById(R.id.sPrice)
        cName = findViewById(R.id.etCustomerName)
        sFAB = findViewById(R.id.sFAB)
        sRecyclerView = findViewById(R.id.sRecyclerView)
        sRecyclerView.isNestedScrollingEnabled = false
        btnAdds = findViewById(R.id.btnAdds)

        transactionList.clear()

        toolbar = findViewById(R.id.salesToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Sales Entry"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            if (transactionList.isEmpty()){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                val dialog = AlertDialog.Builder(this@SellActivity)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Added Items will be removed. Do you want to continue?")
                dialog.setPositiveButton("Yes") { text, listener ->
                    transactionList.clear()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                dialog.setNegativeButton("No") { text, listener -> // Do Nothing
                }
                dialog.create()
                dialog.show()
            }
        }

        customerList = RetrieveCustomers(this).execute().get()
        val vendorAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, customerList)
        cName.threshold = 1
        cName.setAdapter(vendorAdapter)

        productsList = RetrieveProducts(this).execute().get()
        val productsAdapter =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, productsList)
        sName.threshold = 1
        sName.setAdapter(productsAdapter)

        //DATE PICKER
        sDate = findViewById(R.id.etSalesDate)
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select Date")
        val materialDatePicker = builder.build()
        sDate.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "DATE_PICKER")
        }
        materialDatePicker.addOnPositiveButtonClickListener {
            sDate.text = materialDatePicker.headerText
        }
        //DATE PICKER

        btnAdds.setOnClickListener {

            if (sName.text.toString().isEmpty()) {
                sName.error = "Enter Product Name"
                sName.requestFocus()
                return@setOnClickListener
            }
            if (sQuantity.text.toString().isEmpty()) {
                sQuantity.error = "Enter Quantity"
                sQuantity.requestFocus()
                return@setOnClickListener
            }
            if (sPrice.text.toString().isEmpty()) {
                sPrice.error = "Enter Price"
                sPrice.requestFocus()
                return@setOnClickListener
            }

            val pEntity = PEntity(
                sName.text.toString(),
                sQuantity.text.toString(),
                sPrice.text.toString()
            )

            if (!PAsyncTask(this, pEntity, 4).execute().get()) {
                Toast.makeText(this, "You Don't have this Product", Toast.LENGTH_SHORT).show()
                sName.requestFocus()
                return@setOnClickListener
            }

            val transactionEntity = Transaction(
                sName.text.toString(),
                sQuantity.text.toString(),
                sPrice.text.toString(),
            )

            transactionList.add(transactionEntity)

            sRecyclerAdapter = SRecyclerAdapter(this, transactionList)
            sRecyclerView.adapter = sRecyclerAdapter
            sRecyclerView.layoutManager = LinearLayoutManager(this)

            sName.text?.clear()
            sQuantity.text?.clear()
            sPrice.text?.clear()
            sName.requestFocus()

        }

        sFAB.setOnClickListener {

            if (cName.text.toString().isEmpty()) {
                cName.error = "Enter Name"
                cName.requestFocus()
                return@setOnClickListener
            }
            if (sDate.text.toString().isEmpty()) {
                sDate.error = "Enter Date"
                sDate.requestFocus()
                return@setOnClickListener
            }
            if (transactionList.isEmpty()) {
                Toast.makeText(this, "No item is Added", Toast.LENGTH_SHORT).show()
            } else {
                val pk = SimpleDateFormat("dd-MM-yyyy|HH:mm:ss", Locale.getDefault())
                val primaryKey = pk.format(Date())
                val dt = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val time = dt.format(Date())

                println("Response is --> primary key - $primaryKey")
                println("Response is --> time - $time")

                var totalSell = 0.00
                var totalB = 0.00

                for (i in 0 until transactionList.size) {                       //update the product quantity

                    totalSell += transactionList[i].tPPrice.toDouble() * transactionList[i].tPQt.toInt()

                    val lEntity = LEntity(
                        primaryKey,
                        transactionList[i].tPName,
                        transactionList[i].tPQt,
                        transactionList[i].tPPrice
                    )

                    LAsyncTask(
                        this,
                        lEntity,
                        1
                    ).execute()   // to save this list item in the database

                    val pDetail = GetProductByName(this, transactionList[i].tPName).execute().get()
                    val proName = pDetail.productName
                    val price = pDetail.productPrice.toDouble()
                    var qt = pDetail.productQuantity.toInt()
                    val newQt = transactionList[i].tPQt.toInt()
                    qt -= newQt
                    val newPEntity = PEntity(proName, qt.toString(), price.toString())
                    PAsyncTask(this, newPEntity, 5).execute().get()

                    totalB += price * transactionList[i].tPQt.toInt()

                }

                val dEntity = DEntity(
                    cName.text.toString(),
                    "",
                    "",
                    "",
                    "Customer"
                )

                if (!DAsyncTask(this, dEntity, 4).execute().get()) {       //If dealer didn't exist
                    DAsyncTask(this, dEntity, 1).execute()      // Add new dealer
                    sharedPreferences.edit().putBoolean("newCustomer",true).apply()
                }

                val profit = totalSell-totalB

                val pnlEntity = PnLEntity(
                    cName.text.toString(),
                    "${sDate.text}|$time",
                    totalSell.toString(),
                    totalB.toString(),
                    profit.toString()
                )

                PnLAsyncTask(this, pnlEntity,1).execute()

                val tEntity = TEntity(
                    primaryKey,
                    sDate.text.toString(),
                    time,
                    "Sell",
                    cName.text.toString(),
                    totalSell.toString()
                )

                val checkSaved = TAsyncTask(this, tEntity, 1).execute()
                val isSaved = checkSaved.get()
                if (isSaved) {
                    startActivity(Intent(this@SellActivity, MainActivity::class.java))
                    Toast.makeText(this, "Transaction Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (transactionList.isEmpty()){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else{
            val dialog = AlertDialog.Builder(this@SellActivity)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Added Items will be removed. Do you want to continue?")
            dialog.setPositiveButton("Yes") { text, listener ->
                transactionList.clear()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            dialog.setNegativeButton("No") { text, listener -> // Do Nothing
            }
            dialog.create()
            dialog.show()
        }
    }

    class RetrieveCustomers(val context: Context) : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String> {
            val db = Room.databaseBuilder(context, Database::class.java, "transactions-db")
                .build()        //Initialise the database
            return db.dao().getCustomers()         //Return the list of customers
        }
    }

    class RetrieveProducts(val context: Context) : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String> {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()        //Initialise the database
            return db.dao().productsNames()         //Return the list of all Products
        }
    }

    class GetProductByName(val context: Context, private val productName: String) :
        AsyncTask<Void, Void, PEntity>() {
        override fun doInBackground(vararg params: Void?): PEntity {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()
            return db.dao().getProductByName(productName)
        }
    }
}
