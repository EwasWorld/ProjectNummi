package com.eywa.projectnummi.database.account

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM ${DatabaseAccount.TABLE_NAME}")
    fun getAll(): Flow<List<DatabaseAccount>>

    @Insert
    suspend fun insert(vararg courts: DatabaseAccount)

    @Delete
    suspend fun delete(court: DatabaseAccount)

    @Update
    suspend fun update(vararg courts: DatabaseAccount)
}
