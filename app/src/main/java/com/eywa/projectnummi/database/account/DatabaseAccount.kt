package com.eywa.projectnummi.database.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabaseAccount.TABLE_NAME)
data class DatabaseAccount(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        /**
         * Credit, debit, cash, etc.
         */
        val type: String? = null,
) {
    companion object {
        const val TABLE_NAME = "accounts"
    }
}
