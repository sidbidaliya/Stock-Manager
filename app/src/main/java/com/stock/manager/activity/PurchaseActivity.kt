package com.stock.manager.activity

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
import com.stock.manager.adapter.ItemRecyclerAdapter
import com.stock.manager.R
import com.stock.manager.asyncTask.DAsyncTask
import com.stock.manager.asyncTask.LAsyncTask
import com.stock.manager.asyncTask.PAsyncTask
import com.stock.manager.asyncTask.TAsyncTask
import com.stock.manager.database.Database
import com.stock.manager.entities.DEntity
import com.stock.manager.entities.LEntity
import com.stock.manager.entities.PEntity
import com.stock.manager.entities.TEntity
import com.stock.manager.model.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class PurchaseActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var pDate: TextInputEditText
    private lateinit var pName: AutoCompleteTextView
    private lateinit var pQuantity: TextInputEditText
    private lateinit var pPrice: TextInputEditText
    private lateinit var vName: AutoCompleteTextView
    private lateinit var tFAB: FloatingActionButton
    private lateinit var btnAdd: Button

    private lateinit var tRecyclerView: RecyclerView
    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private var transactionList: ArrayList<Transaction> = arrayListOf()
    private var vendorList: List<String> = arrayListOf()
    private var productsList: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_purchase)

        pName = findViewById(R.id.pName)
        pQuantity = findViewById(R.id.pQt)
        pPrice = findViewById(R.id.pPrice)
        vName = findViewById(R.id.etVendorName)
        tFAB = findViewById(R.id.pFAB)
        tRecyclerView = findViewById(R.id.pRecyclerView)
        tRecyclerView.isNestedScrollingEnabled = false
        btnAdd = findViewById(R.id.btnAdd)

        transactionList.clear()

        toolbar = findViewById(R.id.purchaseToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Purchase Entry"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            if (transactionList.isEmpty()){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                val dialog = AlertDialog.Builder(this@PurchaseActivity)
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

        vendorList = RetrieveVendors(this).execute().get()
        val vendorAdapter =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, vendorList)
        vName.threshold = 1
        vName.setAdapter(vendorAdapter)

        productsList = RetrieveProducts(this).execute().get()
        val productsAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, productsList)
        pName.threshold = 1
        pName.setAdapter(productsAdapter)

        //DATE PICKER
        pDate = findViewById(R.id.PurchaseDate)
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select Date")
        val materialDatePicker = builder.build()
        pDate.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "DATE_PICKER")
        }
        materialDatePicker.addOnPositiveButtonClickListener {
//            pDate.text = materialDatePicker.headerText
            pDate.setText(materialDatePicker.headerText, TextView.BufferType.EDITABLE)
        }
        //DATE PICKER

        btnAdd.setOnClickListener {

            if (pName.text.toString().isEmpty()) {
                pName.error = "Enter Product Name"
                pName.requestFocus()
                return@setOnClickListener
            }
            if (pQuantity.text.toString().isEmpty()) {
                pQuantity.error = "Enter Quantity"
                pQuantity.requestFocus()
                return@setOnClickListener
            }
            if (pPrice.text.toString().isEmpty()) {
                pPrice.error = "Enter Price"
                pPrice.requestFocus()
                return@setOnClickListener
            }

            val transactionEntity = Transaction(
                pName.text.toString(),
                pQuantity.text.toString(),
                pPrice.text.toString()
            )

            transactionList.add(transactionEntity)

            itemRecyclerAdapter = ItemRecyclerAdapter(this, transactionList)
            tRecyclerView.adapter = itemRecyclerAdapter
            tRecyclerView.layoutManager = LinearLayoutManager(this)

            pName.text?.clear()
            pQuantity.text?.clear()
            pPrice.text?.clear()
            pName.requestFocus()

        }

        tFAB.setOnClickListener {

            if (vName.text.toString().isEmpty()) {
                vName.error = "Enter Name"
                vName.requestFocus()
                return@setOnClickListener
            }
            if (pDate.text.toString().isEmpty()) {
                pDate.error = "Enter Date"
                vName.requestFocus()
                return@setOnClickListener
            }
            if (transactionList.isEmpty()) {
                Toast.makeText(this, "No Item is Added", Toast.LENGTH_SHORT).show()
            } else {

                val pk = SimpleDateFormat("dd-MM-yyyy|HH:mm:ss", Locale.getDefault())
                val primaryKey = pk.format(Date())
                val dt = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val time = dt.format(Date())

                println("Response is --> primary key - $primaryKey")
                println("Response is --> time - $time")

                var totalPurchase = 0.00

                for (i in 0 until transactionList.size) {

                    totalPurchase += transactionList[i].tPPrice.toDouble()*transactionList[i].tPQt.toInt()

                    val pEntity = PEntity(
                        transactionList[i].tPName,
                        transactionList[i].tPQt,
                        transactionList[i].tPPrice
                    )

                    if (!PAsyncTask(this, pEntity, 4).execute().get()) {       //If product didn't exist
                        PAsyncTask(this, pEntity, 1).execute()      // Add new product
                    } else {
                        //update the product price and quantity
                        val pDetail =
                            GetProductByName(this, transactionList[i].tPName).execute().get()
                        val proName = pDetail.productName
                        var qt = pDetail.productQuantity.toInt()
                        var price = pDetail.productPrice.toDouble()
                        val newQt = transactionList[i].tPQt.toInt()
                        val newPrice = transactionList[i].tPPrice.toDouble()
                        price = ((price * qt) + (newPrice * newQt)) / (qt + newQt)
                        qt += newQt
                        val convertedPrice = BigDecimal(price).setScale(1, RoundingMode.HALF_EVEN)
                        val newPEntity = PEntity(proName, qt.toString(), convertedPrice.toString())
                        PAsyncTask(this, newPEntity, 5).execute().get()
                    }

                    val lEntity = LEntity(
                        primaryKey,
                        transactionList[i].tPName,
                        transactionList[i].tPQt,
                        transactionList[i].tPPrice
                    )

                    LAsyncTask(this, lEntity, 1).execute()   // to save this list item in the database
                }

                val dEntity = DEntity(
                    vName.text.toString(),
                    "",
                    "",
                    "",
                    "Vendor"
                )

                if (!DAsyncTask(this, dEntity, 4).execute().get()) {       //If dealer didn't exist
                    DAsyncTask(this, dEntity, 1).execute()      // Add new dealer
                    sharedPreferences.edit().putBoolean("newVendor",true).apply()
                }

                val tEntity = TEntity(
                    primaryKey,
                    pDate.text.toString(),
                    time,
                    "Buy",
                    vName.text.toString(),
                    totalPurchase.toString()
                )

                val checkSaved = TAsyncTask(this, tEntity, 1).execute()
                val isSaved = checkSaved.get()

                if (isSaved) {
                    startActivity(Intent(this@PurchaseActivity, MainActivity::class.java))
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
            val dialog = AlertDialog.Builder(this@PurchaseActivity)
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

    class GetProductByName(val context: Context, private val productName: String) :
        AsyncTask<Void, Void, PEntity>() {
        override fun doInBackground(vararg params: Void?): PEntity {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()
            return db.dao().getProductByName(productName)
        }
    }

    class RetrieveVendors(val context: Context) : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String> {
            val db = Room.databaseBuilder(context, Database::class.java, "transactions-db")
                .build()        //Initialise the database
            return db.dao().getVendors()         //Return the list of vendors
        }
    }

    class RetrieveProducts(val context: Context) : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg params: Void?): List<String> {
            val db = Room.databaseBuilder(context, Database::class.java, "products-db")
                .build()        //Initialise the database
            return db.dao().productsNames()         //Return the list of all Products
        }
    }
}
