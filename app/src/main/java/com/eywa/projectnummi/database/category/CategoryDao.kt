package com.eywa.projectnummi.database.category

import androidx.room.*
import com.eywa.projectnummi.database.DbId
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
                WITH RECURSIVE sub_cat(parentsIdsString, allNamesString, id, color) AS (
                    SELECT 
                            NULL, 
                            name, 
                            id,
                            CASE
                                WHEN matchParentColor = 0 OR parentCategoryId IS NULL THEN color
                                ELSE NULL
                            END
                        FROM ${DatabaseCategory.TABLE_NAME} 
                        WHERE parentCategoryId IS NULL
                        
                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.parentCategoryId IS NULL THEN sub_cat.parentsIdsString
                                WHEN sub_cat.parentsIdsString IS NULL THEN cat.parentCategoryId
                                ELSE cat.parentCategoryId || ',' || sub_cat.parentsIdsString
                            END,
                            cat.name || '${CategoryIdWithParentIds.NAME_SEPARATOR}' || sub_cat.allNamesString,
                            cat.id,
                            CASE
                                WHEN cat.matchParentColor = 0 OR cat.parentCategoryId IS NULL THEN cat.color
                                ELSE sub_cat.color
                            END
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.parentCategoryId = sub_cat.id
                )
                SELECT *
                FROM sub_cat
                GROUP BY sub_cat.id
            """
    )
    fun getParentIds(): Flow<Map<DbId, CategoryIdWithParentIds>>

    /**
     * Direct parent first, root last
     */
    @Query(
            """
                WITH RECURSIVE sub_cat(parentsIdsString, allNamesString, current, color) AS (
                    SELECT 
                            NULL,
                            name, 
                            parentCategoryId, 
                            CASE
                                WHEN matchParentColor = 0 OR parentCategoryId IS NULL THEN color
                                ELSE NULL
                            END
                        FROM ${DatabaseCategory.TABLE_NAME}
                        WHERE id = :id

                    UNION ALL
                    SELECT
                            CASE
                                WHEN cat.id IS NULL THEN sub_cat.parentsIdsString
                                WHEN sub_cat.parentsIdsString IS NULL THEN cat.id
                                ELSE sub_cat.parentsIdsString || ',' || cat.id
                            END,
                            sub_cat.allNamesString || '${CategoryIdWithParentIds.NAME_SEPARATOR}' || cat.name,
                            cat.parentCategoryId,
                            CASE
                                WHEN NOT sub_cat.color IS NULL THEN sub_cat.color
                                WHEN cat.matchParentColor = 0 OR cat.parentCategoryId IS NULL THEN cat.color
                                ELSE NULL
                            END
                        FROM ${DatabaseCategory.TABLE_NAME} as cat, sub_cat
                        WHERE cat.id = sub_cat.current
                )
                SELECT parentsIdsString, allNamesString, color
                FROM sub_cat
                WHERE current IS NULL
            """
    )
    fun getParentIds(id: Int): Flow<CategoryIdWithParentIds>

    @Insert
    suspend fun insert(category: DatabaseCategory): Long

    @Delete
    suspend fun delete(category: DatabaseCategory)

    @Query(
            """
                DELETE FROM ${DatabaseCategory.TABLE_NAME}
                WHERE (id IN (:ids))
            """
    )
    suspend fun delete(ids: List<Int>)

    @Update
    suspend fun update(vararg categories: DatabaseCategory)
}
