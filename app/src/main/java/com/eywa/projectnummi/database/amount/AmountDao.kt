package com.eywa.projectnummi.database.amount

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface AmountDao {
    @Insert
    suspend fun insert(vararg amounts: DatabaseAmount)

    @Delete
    suspend fun delete(vararg amount: DatabaseAmount)

    @Update
    suspend fun update(vararg amounts: DatabaseAmount)
}
