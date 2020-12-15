package com.stock.manager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Lists")
data class LEntity(
    @ColumnInfo(name = "transaction_id") val tId : String,
    @ColumnInfo(name = "pro_name") val PName: String,
    @ColumnInfo(name = "pro_quantity") var PQt: String,
    @ColumnInfo(name = "pro_price") val PPrice: String
){
    @PrimaryKey(autoGenerate = true)
    var lId : Int = 0
}