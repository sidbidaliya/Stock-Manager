package com.stock.manager.asyncTask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.stock.manager.database.Database
import com.stock.manager.entities.LEntity

class LAsyncTask(private val context: Context, private val lEntity: LEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {

        /*
    mode 1 --> Save the list item in db
    mode 2 --> delete the list item from db
    mode 3 --> update the list item in db
    mode 4 --> check if list item exist or not in db
    * */

        val db = Room.databaseBuilder(context, Database::class.java,"lists-db").build()

        when(mode){

            1 -> {
                //Save the list item in db
                db.dao().insertList(lEntity)
                db.close()
                return true         //because insert function is taken care by room library so no chance of getting false
            }

            2 -> {
                //delete the list item from db
                db.dao().insertList(lEntity)
                db.close()
                return true         //because delete function is taken care by room library so no chance of getting false
            }

        }
        return false
    }
}