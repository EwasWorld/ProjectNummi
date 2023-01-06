package com.eywa.projectnummi.database.person

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabasePerson.TABLE_NAME)
data class DatabasePerson(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
) {
    companion object {
        const val TABLE_NAME = "people"
    }
}
