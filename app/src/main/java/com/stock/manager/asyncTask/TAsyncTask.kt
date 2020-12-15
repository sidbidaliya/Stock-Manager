package com.stock.manager.asyncTask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.entities.TEntity

class TAsyncTask(
    val context: Context,
    private val tEntity: TEntity,
    private val mode: Int
) : AsyncTask<Void, Void, Boolean>() {

    /*
    mode 1 --> Save the transaction in db
    mode 2 --> delete the transaction from db
    mode 3 --> update the transaction in db
    * */

    private val db = Room.databaseBuilder(context, Database::class.java, "transactions-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {

        when (mode) {

            1 -> {
                //Save the transaction in db
                db.dao().insertTransaction(tEntity)
                db.close()
                return true         //because insert function is taken care by room library so no chance of getting false
            }

            2 -> {
                //delete the transaction from db
                db.dao().deleteTransaction(tEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

            3 -> {
                //update the transaction in db
                db.dao().updateTransaction(tEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

        }
        return false
    }
}
