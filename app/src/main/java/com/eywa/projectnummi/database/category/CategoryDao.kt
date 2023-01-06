package com.eywa.projectnummi.database.category

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${DatabaseCategory.TABLE_NAME}")
    fun getAll(): Flow<List<DatabaseCategory>>

    @Insert
    suspend fun insert(vararg courts: DatabaseCategory)

    @Delete
    suspend fun delete(court: DatabaseCategory)

    @Update
    suspend fun update(vararg courts: DatabaseCategory)
}
