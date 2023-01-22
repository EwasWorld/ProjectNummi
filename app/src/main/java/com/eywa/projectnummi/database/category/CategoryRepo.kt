package com.eywa.projectnummi.database.category

import com.eywa.projectnummi.database.DbId
import kotlinx.coroutines.flow.combine

class CategoryRepo(private val categoryDao: CategoryDao) {
    fun getFull() = categoryDao.get()
            .combine(categoryDao.getParentIds()) { categories, parentIds ->
                categories.map { category ->
                    FullDatabaseCategory(category = category, info = parentIds[DbId(category.id)])
                }
            }

    suspend fun insert(category: DatabaseCategory) = categoryDao.insert(category)
    suspend fun delete(category: DatabaseCategory) = categoryDao.delete(category)
    suspend fun update(vararg categories: DatabaseCategory) = categoryDao.update(*categories)
}
