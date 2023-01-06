package com.eywa.projectnummi.database.person

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM ${DatabasePerson.TABLE_NAME}")
    fun get(): Flow<List<DatabasePerson>>

    @Insert
    suspend fun insert(person: DatabasePerson): Long

    @Delete
    suspend fun delete(person: DatabasePerson)

    @Update
    suspend fun update(vararg people: DatabasePerson)
}
