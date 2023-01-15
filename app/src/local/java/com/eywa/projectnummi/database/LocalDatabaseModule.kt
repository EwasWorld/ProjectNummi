package com.eywa.projectnummi.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eywa.projectnummi.model.providers.AccountProvider
import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.model.providers.PeopleProvider
import com.eywa.projectnummi.model.providers.TransactionProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {
    private lateinit var db: NummiDatabase

    @Singleton
    @Provides
    fun provideDatabase(
            @ApplicationContext context: Context,
    ): NummiDatabase {
        db = Room
                .inMemoryDatabaseBuilder(
                        context,
                        NummiDatabase::class.java,
                )
                .addCallback(dbCallback)
                .build()
        return db
    }

    private val dbCallback = object : RoomDatabase.Callback() {
        override fun onCreate(sqlDb: SupportSQLiteDatabase) {
            super.onCreate(sqlDb)

            CoroutineScope(Dispatchers.IO).launch {
                PeopleProvider.basic.forEach {
                    db.personDao().insert(it.asDbPerson())
                }
                AccountProvider.basic.forEach {
                    db.accountDao().insert(it.asDbAccount())
                }
                CategoryProvider.basic.forEach {
                    db.categoryDao().insert(it.asDbCategory())
                }
                TransactionProvider.basic.forEach {
                    db.transactionRepo().insert(it.asDbTransaction(), it.getDbAmounts(null))
                }
            }
        }
    }
}
