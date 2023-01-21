package com.eywa.projectnummi.database.category

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${DatabaseCategory.TABLE_NAME}")
    fun get(): Flow<List<DatabaseCategory>>

    /**
     * Source: https://stackoverflow.com/a/48675161
     */
    @Query(
            """
                WITH RECURSIVE
                sub_cat(parent,catName,catId,level) AS (
                    SELECT 
                            NULL, name, id, 0 
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE parentCategoryId IS NULL
                    UNION ALL
                    SELECT 
                            cat.parentCategoryId, cat.name, cat.id, sub_cat.level+1 
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.parentCategoryId = sub_cat.catId
                )
                SELECT * FROM sub_cat
            """
    )
    fun getRecursive(): Flow<List<RecursiveTest>>

    @Insert
    suspend fun insert(category: DatabaseCategory): Long

    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Update
    suspend fun update(vararg categories: DatabaseCategory)
}

data class RecursiveTest(
        val parent: Int?,
        val catName: String,
        val catId: String,
        val level: Int,
)
