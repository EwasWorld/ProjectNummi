package com.eywa.projectnummi.database.transaction

import androidx.room.*
import com.eywa.projectnummi.database.amount.DatabaseAmount
import com.eywa.projectnummi.database.amount.FullDatabaseAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM ${DatabaseTransaction.TABLE_NAME}")
    fun getFull(): Flow<List<FullDatabaseTransaction>>

    @Transaction
    @Query("SELECT * FROM ${DatabaseTransaction.TABLE_NAME} WHERE id = :id")
    fun getFull(id: Int): Flow<FullDatabaseTransaction>

    /**
     * @param overrideShowAllAccounts ignore [accountIds], show all accounts
     * @param overrideShowAllCategories ignore [categoryIds], show all categories
     * @param overrideShowAllPeople ignore [personIds], show all people
     * @param overrideShowInAndOut ignore [isOutgoing], show all transactions
     */
    @Transaction
    @Query(
            """
                SELECT *
                FROM ${DatabaseTransaction.TABLE_NAME} as tran
                JOIN ${DatabaseAmount.TABLE_NAME} as amount ON tran.id = amount.transactionId
                WHERE 
                    tran.date >= :from AND tran.date <= :to 
                    AND (tran.isOutgoing = :isOutgoing OR :overrideShowInAndOut)
                    AND (tran.accountId IN (:accountIds) OR :overrideShowAllAccounts)
                    AND (amount.categoryId IN (:categoryIds) OR :overrideShowAllCategories)
                    AND (amount.personId IN (:personIds) OR :overrideShowAllPeople)
                GROUP BY tran.id
            """
    )
    fun get(
            from: Calendar,
            to: Calendar,
            accountIds: List<Int?>,
            overrideShowAllAccounts: Boolean,
            categoryIds: List<Int?>,
            overrideShowAllCategories: Boolean,
            personIds: List<Int?>,
            overrideShowAllPeople: Boolean,
            isOutgoing: Boolean,
            overrideShowInAndOut: Boolean,
    ): Flow<Map<DatabaseTransactionWithFullAccount, List<FullDatabaseAmount>>>

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
