package com.eywa.projectnummi.database.category

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${DatabaseCategory.TABLE_NAME}")
    fun get(): Flow<List<DatabaseCategory>>

    @Insert
    suspend fun insert(category: DatabaseCategory): Long

    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Update
    suspend fun update(vararg categories: DatabaseCategory)
}
