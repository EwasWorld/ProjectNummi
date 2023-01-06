package com.eywa.projectnummi.database.transaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM ${DatabaseTransaction.TABLE_NAME}")
    fun getAll(): Flow<List<DatabaseTransaction>>

    @Insert
    suspend fun insert(vararg courts: DatabaseTransaction)

    @Delete
    suspend fun delete(court: DatabaseTransaction)

    @Update
    suspend fun update(vararg courts: DatabaseTransaction)
}
