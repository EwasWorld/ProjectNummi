package com.eywa.projectnummi.database.transaction

import androidx.room.*
import com.eywa.projectnummi.database.amount.DatabaseAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

private const val DATE_ORDER = "date DESC, `order` DESC"

@Dao
interface TransactionDao {
    /**
     * Items are ordered by [DatabaseTransaction.date] and [DatabaseTransaction.order]
     */
    @Transaction
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE isRecurring = :isRecurring 
                ORDER BY $DATE_ORDER
            """
    )
    fun getFull(isRecurring: Boolean): Flow<List<FullDatabaseTransaction>>

    /**
     * Items are ordered by [DatabaseTransaction.date] and [DatabaseTransaction.order]
     */
    @Transaction
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE id = :id 
                ORDER BY $DATE_ORDER
            """
    )
    fun getFull(id: Int): Flow<FullDatabaseTransaction>

    /**
     * Note:
     * - Does not return any [DatabaseTransaction.isRecurring] items.
     * - Items are ordered by [DatabaseTransaction.date] and [DatabaseTransaction.order]
     *
     * @param overrideShowAllAccounts ignore [accountIds], show all accounts
     * @param allowAccountIsNull include null values with [accountIds]
     * @param overrideShowAllCategories ignore [categoryIds], show all categories
     * @param allowCategoryIsNull include null values with [categoryIds]
     * @param overrideShowAllPeople ignore [personIds], show all people
     * @param allowPersonIsNull include null values with [personIds]
     * @param overrideShowInAndOut ignore [isOutgoing], show all transactions
     */
    @Transaction
    @Query(
            """
                SELECT tran.id as transactionId, amount.id as amountId, accountId, categoryId, personId
                FROM ${DatabaseTransaction.TABLE_NAME} as tran
                JOIN ${DatabaseAmount.TABLE_NAME} as amount ON tran.id = amount.transactionId
                WHERE 
                    isRecurring = 0
                    AND tran.date >= :from AND tran.date <= :to 
                    AND (tran.isOutgoing = :isOutgoing OR :overrideShowInAndOut)
                    AND (:overrideShowAllAccounts OR tran.accountId IN (:accountIds) OR (tran.accountId IS null AND :allowAccountIsNull))
                    AND (:overrideShowAllCategories OR amount.categoryId IN (:categoryIds) OR (amount.categoryId IS null AND :allowCategoryIsNull))
                    AND (:overrideShowAllPeople OR amount.personId IN (:personIds) OR (amount.personId IS null AND :allowPersonIsNull))
                ORDER BY $DATE_ORDER
            """
    )
    fun get(
            from: Calendar,
            to: Calendar,
            accountIds: List<Int>,
            allowAccountIsNull: Boolean,
            overrideShowAllAccounts: Boolean,
            categoryIds: List<Int>,
            allowCategoryIsNull: Boolean,
            overrideShowAllCategories: Boolean,
            personIds: List<Int>,
            allowPersonIsNull: Boolean,
            overrideShowAllPeople: Boolean,
            isOutgoing: Boolean,
            overrideShowInAndOut: Boolean,
    ): Flow<List<DatabaseTransactionAndAmount>>

    /**
     * @return max [DatabaseTransaction.order] where [DatabaseTransaction.date] == [date]
     */
    @Query("SELECT MAX(`order`) FROM ${DatabaseTransaction.TABLE_NAME} WHERE date = :date")
    fun getMaxOrder(date: Calendar): Flow<Int?>

    /**
     * Note: does not return any [DatabaseTransaction.isRecurring] items
     *
     * @return [DatabaseTransaction] with the highest [DatabaseTransaction.order] that is lower than [order]
     *   where [DatabaseTransaction.date] == [date]
     */
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE date = :date AND `order` < :order AND isRecurring = 0
                ORDER BY `order` DESC
                LIMIT 1
            """
    )
    fun getOrderBelow(date: Calendar, order: Int): Flow<DatabaseTransaction?>

    /**
     * Note: does not return any [DatabaseTransaction.isRecurring] items
     *
     * @return [DatabaseTransaction] with the lowest [DatabaseTransaction.order] that is higher than [order]
     *   where [DatabaseTransaction.date] == [date]
     */
    @Query(
            """
                SELECT * 
                FROM ${DatabaseTransaction.TABLE_NAME} 
                WHERE date = :date AND `order` > :order AND isRecurring = 0
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
