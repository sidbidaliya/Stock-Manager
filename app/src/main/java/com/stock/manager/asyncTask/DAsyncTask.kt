package com.stock.manager.asyncTask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.entities.DEntity

class DAsyncTask(private val context: Context, private val dEntity: DEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {

        /*
    mode 1 --> Save the dealer in db
    mode 2 --> delete the dealer from db
    mode 3 --> update the dealer in db
    mode 4 --> check if dealer exist or not in db
    * */

        val db = Room.databaseBuilder(context, Database::class.java,"dealers-db").build()

        when(mode){

            1 -> {
                //Save the dealer in db
                db.dao().insertDealer(dEntity)
                db.close()
                return true         //because insert function is taken care by room library so no chance of getting false
            }

            2 -> {
                //delete the dealer from db
                db.dao().deleteDealer(dEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

            3 -> {
                //update the dealer in db
                db.dao().updateDealer(dEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

            4 -> {
                // check if dealer exist or not in db
                val res = db.dao().checkDealer(dEntity.dName)
                val ret: Boolean
                ret = res >= 1
                db.close()
                return ret
            }
        }
        return false
    }
}