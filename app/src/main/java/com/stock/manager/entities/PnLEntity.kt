package com.stock.manager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PnL")
class PnLEntity(
    @ColumnInfo(name = "customer_name") val cName: String,
    @ColumnInfo(name = "Date_time") val dateTime: String,
    @ColumnInfo(name = "total_sold_price") val sPrice: String,
    @ColumnInfo(name = "total_buy_price") val bPrice: String,
    @ColumnInfo(name = "total_profit") val total: String
) {
    @PrimaryKey(autoGenerate = true)
    var pnlId: Int = 0
}