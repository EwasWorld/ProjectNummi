package com.eywa.projectnummi.database.category

class CategoryRepo(private val categoryDao: CategoryDao) {
    fun get() = categoryDao.get()
    suspend fun insert(category: DatabaseCategory) = categoryDao.insert(category)
    suspend fun delete(category: DatabaseCategory) = categoryDao.delete(category)
    suspend fun update(vararg categories: DatabaseCategory) = categoryDao.update(*categories)
}
