package com.stock.manager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dealers")
data class DEntity(
    @ColumnInfo(name = "dealer_name") val dName: String,
    @ColumnInfo(name = "dealer_address") val dAddress: String,
    @ColumnInfo(name = "dealer_contact") val dContact: String,
    @ColumnInfo(name = "dealer_mail") val dMail: String,
    @ColumnInfo(name = "dealer_type") val dType: String
){
    @PrimaryKey(autoGenerate = true)
    var dealerId : Int = 0
}