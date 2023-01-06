package com.eywa.projectnummi.database.person

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM ${DatabasePerson.TABLE_NAME}")
    fun getAll(): Flow<List<DatabasePerson>>

    @Insert
    suspend fun insert(vararg courts: DatabasePerson)

    @Delete
    suspend fun delete(court: DatabasePerson)

    @Update
    suspend fun update(vararg courts: DatabasePerson)
}
