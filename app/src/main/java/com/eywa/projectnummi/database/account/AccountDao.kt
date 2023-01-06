package com.eywa.projectnummi.database.account

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM ${DatabaseAccount.TABLE_NAME}")
    fun get(): Flow<List<DatabaseAccount>>

    @Insert
    suspend fun insert(vararg accounts: DatabaseAccount)

    @Delete
    suspend fun delete(account: DatabaseAccount)

    @Update
    suspend fun update(vararg accounts: DatabaseAccount)
}
