package com.stock.manager.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.stock.manager.R
import com.stock.manager.fragment.*

class FragmentActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_fragment)

        val isNightModeOn = sharedPreferences.getBoolean("NightMode", false)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        toolbar = findViewById(R.id.FragmentToolbar)
        frameLayout = findViewById(R.id.frameLayout)

        setUpToolbar()

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        when (intent.getStringExtra("fragment")) {
            "products" -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        ProductsFragment()
                    )
                    .commit()
                supportActionBar?.title = "Products"
            }

            "transactions" -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        TransactionsFragment()
                    )
                    .commit()
                supportActionBar?.title = "Transactions"
            }

            "pnl" -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        PnLFragment()
                    )
                    .commit()
                supportActionBar?.title = "Profit & Loss"
            }

            "limited_stocks" -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        MinFragment()
                    )
                    .commit()
                supportActionBar?.title = "Limited Stocks"
            }

            "customers" -> {
                val bundle = Bundle()
                bundle.putString("params", "Customers")
                val fragment = CnVFragment()
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        fragment
                    )
                    .commit()
                supportActionBar?.title = "Customers"
                sharedPreferences.edit().putBoolean("newCustomer", false).apply()
            }

            "vendors" -> {
                val bundle = Bundle()
                bundle.putString("params", "Vendors")
                val fragment = CnVFragment()
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        fragment
                    )
                    .commit()
                supportActionBar?.title = "Vendors"
                sharedPreferences.edit().putBoolean("newVendor", false).apply()

            }
        }

    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

}