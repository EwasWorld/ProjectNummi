package com.eywa.projectnummi.database.category

import kotlinx.coroutines.flow.map

class CategoryRepo(private val categoryDao: CategoryDao) {
    fun get() = categoryDao.get()

    fun getFull() = categoryDao.get().map { categories ->
        val (noParent, hasParent) = categories.partition { it.parentCategoryId == null }
        val processed = noParent.map { FullDatabaseCategory(category = it, parent = null) }.toMutableList()
        val toProcess = hasParent.toMutableList()
        var toProcessSize = toProcess.size
        while (toProcess.isNotEmpty()) {
            for (i in toProcess.indices.reversed()) {
                val category = toProcess[i]
                val parent = processed.find { category.parentCategoryId == it.category.id } ?: continue
                processed.add(FullDatabaseCategory(category, parent))
                toProcess.removeAt(i)
            }

            if (toProcessSize == toProcess.size) {
                throw IllegalStateException("Cannot resolve category parents")
            }
            toProcessSize = toProcess.size
        }
        processed.toList()
    }

    suspend fun insert(category: DatabaseCategory) = categoryDao.insert(category)
    suspend fun delete(category: DatabaseCategory) = categoryDao.delete(category)
    suspend fun update(vararg categories: DatabaseCategory) = categoryDao.update(*categories)
}
