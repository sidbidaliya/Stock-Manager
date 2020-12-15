package com.stock.manager.asyncTask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.entities.PEntity

class PAsyncTask(private val context: Context, private val pEntity: PEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {

        /*
    mode 1 --> Save the product in db
    mode 2 --> delete the product from db
    mode 3 --> update the product in db
    mode 4 --> check if product exist or not in db
    * */

        val db = Room.databaseBuilder(context, Database::class.java,"products-db").build()

        when(mode){

            1 -> {
                //Save the product in db
                db.dao().insertProduct(pEntity)
                db.close()
                return true         //because insert function is taken care by room library so no chance of getting false
            }

            2 -> {
                //delete the transaction from db
                db.dao().deleteProduct(pEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

            3 -> {
                //update the transaction in db
                db.dao().updateProduct(pEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

            4 -> {
                // check if product exist or not in db
                val res = db.dao().checkProduct(pEntity.productName)
                val ret: Boolean
                ret = res >= 1
                db.close()
                return ret
            }

            5 -> {
                val res = db.dao().setProductQtNPrice(pEntity.productName,pEntity.productQuantity,pEntity.productPrice)
                val ret : Boolean
                ret = res == 1
                db.close()
                return ret
            }
        }
        return false
    }
}