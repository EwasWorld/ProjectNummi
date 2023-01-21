package com.eywa.projectnummi.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eywa.projectnummi.database.category.CategoryDao
import com.eywa.projectnummi.database.category.CategoryIdWithParentIds
import com.eywa.projectnummi.model.providers.CategoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: NummiDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        db = DatabaseTestUtils.createDatabase()
        categoryDao = db.categoryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testRecursive() = runTest {
        CategoryProvider.recursive.forEach { categoryDao.insert(it.asDbCategory()) }

        assertEquals(
                listOf(
                        CategoryIdWithParentIds(null, 1),
                        CategoryIdWithParentIds(null, 5),
                        CategoryIdWithParentIds("1", 2),
                        CategoryIdWithParentIds("1", 4),
                        CategoryIdWithParentIds("5", 6),
                        CategoryIdWithParentIds("2,1", 3),
                ),
                categoryDao.getParentIds().first(),
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testRecursiveWithId() = runTest {
        CategoryProvider.recursive.forEach { categoryDao.insert(it.asDbCategory()) }

        assertEquals("2,1", categoryDao.getParentIds(3).first())
    }
}
