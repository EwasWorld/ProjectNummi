package com.eywa.projectnummi.database.transaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM ${DatabaseTransaction.TABLE_NAME}")
    fun getFull(): Flow<List<FullDatabaseTransaction>>

    @Insert
    suspend fun insert(transaction: DatabaseTransaction): Long

    @Delete
    suspend fun delete(transaction: DatabaseTransaction)

    @Update
    suspend fun update(vararg transactions: DatabaseTransaction)
}
