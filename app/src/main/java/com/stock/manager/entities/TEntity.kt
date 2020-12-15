package com.stock.manager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transactions")
data class TEntity(
    @ColumnInfo(name = "list_id") val ListId: String,
    @ColumnInfo(name = "transaction_date") val TDate: String,
    @ColumnInfo(name = "transaction_time") val TTime: String,
    @ColumnInfo(name = "buy_or_sell") val TMode: String,
    @ColumnInfo(name = "dealer_name") val DName: String,
    @ColumnInfo(name = "total_purchase") val TSum: String
){
    @PrimaryKey(autoGenerate = true)
    var tranId : Int = 0
}
