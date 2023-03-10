package com.eywa.projectnummi.database.transaction

import androidx.room.*
import com.eywa.projectnummi.database.DbId
import com.eywa.projectnummi.database.account.DatabaseAccount
import com.eywa.projectnummi.database.amount.DatabaseAmount
import com.eywa.projectnummi.database.amount.FullDatabaseAmount
import com.eywa.projectnummi.database.amount.FullDatabaseAmountWithFullCategory
import com.eywa.projectnummi.database.category.CategoryIdWithParentIds
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.database.person.DatabasePerson
import java.util.*

@Entity(
        tableName = DatabaseTransaction.TABLE_NAME,
        foreignKeys = [
            ForeignKey(
                    entity = DatabaseAccount::class,
                    parentColumns = ["id"],
                    childColumns = ["accountId"],
                    onDelete = ForeignKey.SET_NULL,
            ),
        ],
        indices = [
            Index(
                    value = ["date", "order"],
                    unique = true,
            )
        ],
)
data class DatabaseTransaction(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: Calendar,
        val name: String,
        /**
         * Null for default account
         */
        @ColumnInfo(index = true) val accountId: Int? = null,
        /**
         * True if money is leaving the account, false if it's entering the account
         */
        val isOutgoing: Boolean = true,
        /**
         * The order that transactions with the same [date] should be shown
         */
        val order: Int = 1,
        val note: String? = null,
        /**
         * Marks this transaction as a recurring item. Will allow the user to quickly create a duplicate of it.
         *
         * Should not count towards actual user data.
         */
        @ColumnInfo(defaultValue = "0")
        val isRecurring: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "transactions"
    }
}

data class FullDatabaseTransaction(
        @Embedded val transaction: DatabaseTransaction,

        @Relation(
                parentColumn = "id",
                entityColumn = "transactionId",
                entity = DatabaseAmount::class,
        )
        val amounts: List<FullDatabaseAmount>,

        @Relation(
                parentColumn = "accountId",
                entityColumn = "id",
        )
        val account: DatabaseAccount?,
) {
    fun addCategoryInfoDbId(info: Map<DbId, CategoryIdWithParentIds>) = addCategoryInfo(info.mapKeys { it.key.id })

    fun addCategoryInfo(info: Map<Int, CategoryIdWithParentIds>) =
            FullDatabaseTransactionWithFullCategory(
                    transaction = transaction,
                    amounts = amounts.map { it.addCategoryInfo(info) },
                    account = account
            )
}

// TODO This feels dumb... Why isn't map working?
data class DatabaseTransactionAndAmount(
        val transactionId: Int,
        val amountId: Int,
        val accountId: Int,
        val categoryId: Int?,
        val personId: Int?,

        @Relation(
                parentColumn = "transactionId",
                entityColumn = "id",
        )
        val transaction: DatabaseTransaction,

        @Relation(
                parentColumn = "amountId",
                entityColumn = "id",
        )
        val amount: DatabaseAmount,

        @Relation(
                parentColumn = "accountId",
                entityColumn = "id",
        )
        val account: DatabaseAccount?,

        @Relation(
                parentColumn = "categoryId",
                entityColumn = "id",
        )
        val category: DatabaseCategory?,

        @Relation(
                parentColumn = "personId",
                entityColumn = "id",
        )
        val person: DatabasePerson?,
)

data class FullDatabaseTransactionWithFullCategory(
        val transaction: DatabaseTransaction,
        val amounts: List<FullDatabaseAmountWithFullCategory>,
        val account: DatabaseAccount?,
)

data class DatabaseTransactionWithFullAccount(
        @Embedded val transaction: DatabaseTransaction,
        @Relation(
                parentColumn = "accountId",
                entityColumn = "id",
        )
        val account: DatabaseAccount?,
)
