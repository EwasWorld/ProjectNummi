package com.eywa.projectnummi.database.transaction

import androidx.room.*
import com.eywa.projectnummi.database.account.DatabaseAccount
import com.eywa.projectnummi.database.amount.DatabaseAmount
import com.eywa.projectnummi.database.amount.FullDatabaseAmount
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
         * As [date] is only storing the day of the transaction
         */
        val order: Int = 1,
        val note: String? = null,
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
)
