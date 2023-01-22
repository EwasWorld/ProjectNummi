package com.eywa.projectnummi.database.amount

import androidx.room.*
import com.eywa.projectnummi.database.DbId
import com.eywa.projectnummi.database.category.CategoryIdWithParentIds
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.database.category.FullDatabaseCategory
import com.eywa.projectnummi.database.person.DatabasePerson
import com.eywa.projectnummi.database.transaction.DatabaseTransaction

@Entity(
        tableName = DatabaseAmount.TABLE_NAME,
        foreignKeys = [
            ForeignKey(
                    entity = DatabaseTransaction::class,
                    parentColumns = ["id"],
                    childColumns = ["transactionId"],
                    onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                    entity = DatabaseCategory::class,
                    parentColumns = ["id"],
                    childColumns = ["categoryId"],
                    onDelete = ForeignKey.SET_NULL,
            ),
            ForeignKey(
                    entity = DatabasePerson::class,
                    parentColumns = ["id"],
                    childColumns = ["personId"],
                    onDelete = ForeignKey.SET_NULL,
            ),
        ],
        indices = [
            Index(
                    value = ["transactionId", "categoryId", "personId"],
                    unique = true,
            ),
        ]
)
data class DatabaseAmount(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(index = true) val transactionId: Int,
        @ColumnInfo(index = true) val categoryId: Int?,

        /**
         * Null for default person
         */
        @ColumnInfo(index = true) val personId: Int?,

        /**
         * In pennies
         */
        val amount: Int,
) {
    companion object {
        const val TABLE_NAME = "amounts"
    }
}

data class FullDatabaseAmount(
        @Embedded val amount: DatabaseAmount,

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
) {
    fun addCategoryInfoDbId(info: Map<DbId, CategoryIdWithParentIds>) = addCategoryInfo(info.mapKeys { it.key.id })

    fun addCategoryInfo(info: Map<Int, CategoryIdWithParentIds>) =
            FullDatabaseAmountWithFullCategory(
                    amount = amount,
                    category = category?.let { cat -> FullDatabaseCategory(cat, cat.id.let { info[it] }) },
                    person = person,
            )
}

data class FullDatabaseAmountWithFullCategory(
        val amount: DatabaseAmount,
        val category: FullDatabaseCategory?,
        val person: DatabasePerson?,
)
