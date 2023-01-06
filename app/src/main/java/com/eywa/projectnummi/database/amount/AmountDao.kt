package com.eywa.projectnummi.database.amount

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AmountDao {
    @Query("SELECT * FROM ${DatabaseAmount.TABLE_NAME}")
    fun getAll(): Flow<List<DatabaseAmount>>

    @Insert
    suspend fun insert(vararg courts: DatabaseAmount)

    @Delete
    suspend fun delete(court: DatabaseAmount)

    @Update
    suspend fun update(vararg courts: DatabaseAmount)
}
