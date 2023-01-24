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
        @ColumnInfo(defaultValue = "0") val matchParentColor: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}

data class FullDatabaseCategory(
        val category: DatabaseCategory,
        val info: CategoryIdWithParentIds?,
)

class CategoryIdWithParentIds(
        private val parentsIdsString: String?,
        private val allNamesString: String?,
        val color: DbColor,
) {
    /**
     * Direct parent first, root last
     */
    fun getParentsIds() = parentsIdsString?.split(",")?.map { it.toInt() }

    /**
     * This category's name first, then all parents up to the root
     */
    fun getAllNames() = allNamesString?.split(NAME_SEPARATOR)

    override fun equals(other: Any?): Boolean {
        if (other !is CategoryIdWithParentIds) return false
        return parentsIdsString == other.parentsIdsString
                && allNamesString == other.allNamesString
                && color.value == other.color.value
    }

    override fun hashCode(): Int {
        var result = parentsIdsString?.hashCode() ?: 0
        result = 31 * result + (allNamesString?.hashCode() ?: 0)
        result = 31 * result + color.value.hashCode()
        return result
    }

    companion object {
        internal const val NAME_SEPARATOR = "<!NM_SEP!>"
    }
}

