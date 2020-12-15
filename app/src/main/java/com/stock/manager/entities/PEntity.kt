package com.stock.manager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class PEntity(
    @ColumnInfo(name = "product_name") var productName: String,
    @ColumnInfo(name = "product_quantity") var productQuantity: String,
    @ColumnInfo(name = "product_price") var productPrice: String
){
    @PrimaryKey(autoGenerate = true)
    var productId : Int = 0
}
