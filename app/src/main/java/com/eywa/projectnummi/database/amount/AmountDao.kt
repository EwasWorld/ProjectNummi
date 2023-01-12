package com.eywa.projectnummi.database.amount

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AmountDao {
    @Transaction
    @Query("SELECT * FROM ${DatabaseAmount.TABLE_NAME}")
    fun getFull(): Flow<List<FullDatabaseAmount>>

    @Insert
    suspend fun insert(vararg amounts: DatabaseAmount)

    @Delete
    suspend fun delete(vararg amount: DatabaseAmount)

    @Update
    suspend fun update(vararg amounts: DatabaseAmount)
}
