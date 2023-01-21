package com.eywa.projectnummi.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

object DatabaseTestUtils {
    fun createDatabase(): NummiDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room
                .inMemoryDatabaseBuilder(context, NummiDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}
