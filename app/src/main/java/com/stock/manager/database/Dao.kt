package com.stock.manager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.stock.manager.entities.*

@Dao
interface Dao {

    //--------------------DEALERS DAO--------------------

    @Insert
    fun insertDealer(dealerEntity: DEntity)

    @Delete
    fun deleteDealer(dealerEntity: DEntity)

    @Update
    fun updateDealer(dealerEntity: DEntity)

    @Query("SELECT * FROM dealers WHERE dealer_type = 'Customer'")
    fun getAllCustomers(): List<DEntity>

    @Query("SELECT * FROM dealers WHERE dealer_type = 'Vendor'")
    fun getAllVendors(): List<DEntity>

    @Query("SELECT COUNT(*) from dealers where dealer_name = :dName")
    fun checkDealer(dName: String): Int

    //--------------------PRODUCTS DAO--------------------

    @Insert
    fun insertProduct(productEntity: PEntity)

    @Delete
    fun deleteProduct(productEntity: PEntity)

    @Update
    fun updateProduct(productEntity: PEntity)

    @Query("SELECT * FROM Products")
    fun getAllProducts(): List<PEntity>

    @Query("SELECT DISTINCT product_name from Products")
    fun productsNames(): List<String>

    @Query("SELECT COUNT(*) from Products where product_name = :pName")
    fun checkProduct(pName: String): Int

    @Query("SELECT * from Products where product_name = :pName")
    fun getProductByName(pName: String): PEntity

    @Query("UPDATE products SET product_quantity = :pQt, product_price = :pPrice where product_name = :pName")
    fun setProductQtNPrice(pName: String, pQt: String, pPrice: String): Int

    //--------------------TRANSACTIONS DAO--------------------

    @Insert
    fun insertTransaction(tEntity : TEntity)

    @Delete
    fun deleteTransaction(tEntity: TEntity)

    @Update
    fun updateTransaction(tEntity: TEntity)

    @Query("SELECT * FROM Transactions")
    fun getAllTransactions(): List<TEntity>

    @Query("SELECT DISTINCT dealer_name from Transactions WHERE buy_or_sell = 'Buy'")
    fun getVendors(): List<String>

    @Query("SELECT DISTINCT dealer_name from Transactions WHERE buy_or_sell = 'Sell'")
    fun getCustomers(): List<String>


    //--------------------PnL DAO--------------------

    @Insert
    fun insertPnL(pnlEntity: PnLEntity)

    @Query("SELECT * FROM PnL")
    fun getAllPnL(): List<PnLEntity>

    //--------------------LIST DAO--------------------

    @Insert
    fun insertList(listEntity: LEntity)

    @Query("SELECT * FROM Lists WHERE transaction_id = :listId")
    fun getTransactionHistory(listId: String): List<LEntity>

}