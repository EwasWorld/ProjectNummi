package com.eywa.projectnummi.database.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.eywa.projectnummi.database.DbColor

@Entity(
        tableName = DatabaseCategory.TABLE_NAME,
        foreignKeys = [
            ForeignKey(
                    entity = DatabaseCategory::class,
                    parentColumns = ["id"],
                    childColumns = ["parentCategoryId"],
                    onDelete = ForeignKey.SET_NULL,
            ),
        ],
)
data class DatabaseCategory(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String,
        val color: DbColor,
        @ColumnInfo(index = true) val parentCategoryId: Int? = null,
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}
