package com.eywa.projectnummi.database.transaction

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM ${DatabaseTransaction.TABLE_NAME}")
    fun getFull(): Flow<List<FullDatabaseTransaction>>

    /**
     * @return max [DatabaseTransaction.order] where [DatabaseTransaction.date] == [date]
     */
    @Query("SELECT MAX(`order`) FROM ${DatabaseTransaction.TABLE_NAME} WHERE date = :date")
    fun getMaxOrder(date: Calendar): Flow<Int?>

    /**
     * @return [DatabaseTransaction] with the highest [DatabaseTransaction.order] that is lower than [order]
     *   where [DatabaseTransaction.date] == [date]
     */
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE date = :date AND `order` < :order
                ORDER BY `order` DESC
                LIMIT 1
            """
    )
    fun getOrderBelow(date: Calendar, order: Int): Flow<DatabaseTransaction?>

    /**
     * @return [DatabaseTransaction] with the lowest [DatabaseTransaction.order] that is higher than [order]
     *   where [DatabaseTransaction.date] == [date]
     */
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE date = :date AND `order` > :order
                ORDER BY `order` ASC
                LIMIT 1
            """
    )
    fun getOrderAbove(date: Calendar, order: Int): Flow<DatabaseTransaction?>

    @Insert
    suspend fun insert(transaction: DatabaseTransaction): Long

    @Delete
    suspend fun delete(transaction: DatabaseTransaction)

    @Update
    suspend fun update(vararg transactions: DatabaseTransaction)
}
