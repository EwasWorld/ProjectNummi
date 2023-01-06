package com.eywa.projectnummi.database.category

class CategoryRepo(private val categoryDao: CategoryDao) {
    fun getAll() = categoryDao.getAll()
    suspend fun insert(vararg categories: DatabaseCategory) = categoryDao.insert(*categories)
    suspend fun delete(category: DatabaseCategory) = categoryDao.delete(category)
    suspend fun update(vararg categories: DatabaseCategory) = categoryDao.update(*categories)
}
