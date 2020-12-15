package com.stock.manager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stock.manager.entities.*

@Database(
    entities = [PEntity::class, TEntity::class, DEntity::class, LEntity::class, PnLEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}