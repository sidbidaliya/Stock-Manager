package com.stock.manager.asyncTask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.entities.PnLEntity

class PnLAsyncTask(private val context: Context, private val pnlEntity: PnLEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {

    /*
    mode 1 --> Save the PnL in db
    mode 2 --> delete the PnL from db
    mode 3 --> update the PnL in db
    * */

    private val db = Room.databaseBuilder(context, Database::class.java,"pnl-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {

        when(mode){

            1 -> {
                //Save the PnL in db
                db.dao().insertPnL(pnlEntity)
                db.close()
                return true         //because insert function is taken care by room library so no chance of getting false
            }

        }
        return false
    }
}