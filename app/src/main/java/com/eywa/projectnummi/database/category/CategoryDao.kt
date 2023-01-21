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
                WITH RECURSIVE sub_cat(parentsString,catId,color) AS (
                    SELECT NULL, id, IIF(matchParentColor = 0 OR parentCategoryId IS NULL, color, NULL) 
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE parentCategoryId IS NULL
                        
                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.parentCategoryId IS NULL THEN sub_cat.parentsString
                                WHEN sub_cat.parentsString IS NULL THEN cat.parentCategoryId
                                ELSE cat.parentCategoryId || ',' || sub_cat.parentsString
                            END,
                            cat.id,
                            IIF(cat.matchParentColor = 0 OR cat.parentCategoryId IS NULL, cat.color, sub_cat.color)
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
                WITH RECURSIVE sub_cat(parentsString,catId,color) AS (
                    SELECT NULL, parentCategoryId, IIF(matchParentColor = 0 OR parentCategoryId IS NULL, color, NULL)
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE id = :id
                        
                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.id IS NULL THEN sub_cat.parentsString
                                WHEN sub_cat.parentsString IS NULL THEN cat.id
                                ELSE sub_cat.parentsString || ',' || cat.id
                            END,
                            cat.parentCategoryId,
                            CASE
                                WHEN NOT sub_cat.color IS NULL THEN sub_cat.color
                                WHEN cat.matchParentColor = 0 OR cat.parentCategoryId IS NULL THEN cat.color
                                ELSE NULL
                            END
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.id = sub_cat.catId
                )
                SELECT parentsString, :id as catId, color
                FROM sub_cat
                WHERE catId IS NULL
            """
    )
    fun getParentIds(id: Int): Flow<CategoryIdWithParentIds>

    @Insert
    suspend fun insert(category: DatabaseCategory): Long

    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Update
    suspend fun update(vararg categories: DatabaseCategory)
}
