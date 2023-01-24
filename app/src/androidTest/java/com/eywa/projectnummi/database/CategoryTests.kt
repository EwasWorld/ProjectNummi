package com.eywa.projectnummi.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eywa.projectnummi.database.category.CategoryDao
import com.eywa.projectnummi.database.category.CategoryIdWithParentIds
import com.eywa.projectnummi.model.objects.Category
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

@OptIn(ExperimentalCoroutinesApi::class)
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

    @Test
    fun testGetParentIds() = runTest {
        val categories = CategoryProvider.recursive
        categories.forEach { categoryDao.insert(it.asDbCategory()) }

        val expectedItems = (1..8).toOutput(categories)
        val actual = categoryDao.getParentIds().first()

        expectedItems.forEach { (id, expected) ->
            assertEquals(expected, actual[id])
        }
        assertEquals(emptyMap<Int, CategoryIdWithParentIds>(),
                actual.filterNot { (key, _) -> expectedItems.keys.contains(key) })
    }

    @Test
    fun testGetParentIdsFromId() = runTest {
        val categories = CategoryProvider.recursive
        categories.forEach { categoryDao.insert(it.asDbCategory()) }

        (1..8).toOutput(categories)
                .forEach { (id, expected) ->
                    assertEquals(expected, categoryDao.getParentIds(id.id).first())
                }
    }

    private fun IntRange.toOutput(categories: List<Category>) = associate { key ->
        val category = categories.find { it.id == key }!!
        DbId(key) to CategoryIdWithParentIds(
                category.parentIds?.joinToString(","),
                category.allNames.joinToString(CategoryIdWithParentIds.NAME_SEPARATOR),
                DbColor(category.displayColor),
        )
    }
}
