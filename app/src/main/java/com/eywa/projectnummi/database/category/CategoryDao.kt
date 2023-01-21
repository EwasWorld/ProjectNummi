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
                WITH RECURSIVE sub_cat(parentsString,catId) AS (
                    SELECT NULL, id
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE parentCategoryId IS NULL
                        
                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.parentCategoryId IS NULL THEN sub_cat.parentsString
                                WHEN sub_cat.parentsString IS NULL THEN cat.parentCategoryId
                                ELSE cat.parentCategoryId || ',' || sub_cat.parentsString
                            END,
                            cat.id
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.parentCategoryId = sub_cat.catId
                )
                SELECT *
                FROM sub_cat
            """
    )
    fun getParentIds(): Flow<List<CategoryIdWithParentIds>>

    /**
     * Direct parent first, root last
     */
    @Query(
            """
                WITH RECURSIVE sub_cat(parentsString,current) AS (
                    SELECT NULL, parentCategoryId 
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE id = :id
                        
                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.id IS NULL THEN sub_cat.parentsString
                                WHEN sub_cat.parentsString IS NULL THEN cat.id
                                ELSE sub_cat.parentsString || ',' || cat.id
                            END,
                            cat.parentCategoryId
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.id = sub_cat.current
                )
                SELECT parentsString
                FROM sub_cat
                WHERE current IS NULL
            """
    )
    fun getParentIds(id: Int): Flow<String>

    @Insert
    suspend fun insert(category: DatabaseCategory): Long

    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Update
    suspend fun update(vararg categories: DatabaseCategory)
}
