package com.eywa.projectnummi.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eywa.projectnummi.database.account.AccountDao
import com.eywa.projectnummi.database.account.AccountRepo
import com.eywa.projectnummi.database.account.DatabaseAccount
import com.eywa.projectnummi.database.amount.AmountDao
import com.eywa.projectnummi.database.amount.DatabaseAmount
import com.eywa.projectnummi.database.category.CategoryDao
import com.eywa.projectnummi.database.category.CategoryRepo
import com.eywa.projectnummi.database.category.DatabaseCategory
import com.eywa.projectnummi.database.person.DatabasePerson
import com.eywa.projectnummi.database.person.PersonDao
import com.eywa.projectnummi.database.person.PersonRepo
import com.eywa.projectnummi.database.transaction.DatabaseTransaction
import com.eywa.projectnummi.database.transaction.TransactionDao
import com.eywa.projectnummi.database.transaction.TransactionRepo

@Database(
        entities = [
            DatabaseAccount::class,
            DatabaseAmount::class,
            DatabaseCategory::class,
            DatabasePerson::class,
            DatabaseTransaction::class,
        ],
        version = 2,
        exportSchema = true, // Needs a schema location in the build.gradle too to export!
        autoMigrations = [
            AutoMigration(from = 1, to = 2),
        ]
)
@TypeConverters(DatabaseConverters::class)
abstract class NummiDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun amountDao(): AmountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun personDao(): PersonDao
    abstract fun transactionDao(): TransactionDao

    fun accountRepo() = AccountRepo(accountDao())
    fun categoryRepo() = CategoryRepo(categoryDao())
    fun personRepo() = PersonRepo(personDao())
    fun transactionRepo() = TransactionRepo(transactionDao(), amountDao())

    companion object {
        const val DATABASE_NAME = "nummi_database"
    }
}
