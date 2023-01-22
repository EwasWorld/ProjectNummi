package com.eywa.projectnummi.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
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
    fun testGetParentIds() = runTest {
        CategoryProvider.recursive.forEach { categoryDao.insert(it.asDbCategory()) }

        val expectedItems = mapOf(
                DbId(1) to CategoryIdWithParentIds(null, "Top 1", DbColor(Color.Red)),
                DbId(5) to CategoryIdWithParentIds(null, "Top 2", DbColor(Color.Magenta)),
                DbId(7) to CategoryIdWithParentIds(null, "Top 3", DbColor(Color.White)),
                DbId(2) to CategoryIdWithParentIds("1", "Top 1 - Sub 1<!NM_SEP!>Top 1", DbColor(Color.Red)),
                DbId(4) to CategoryIdWithParentIds("1", "Top 1 - Sub 2<!NM_SEP!>Top 1", DbColor(Color.Cyan)),
                DbId(6) to CategoryIdWithParentIds("5", "Top 2 - Sub 1<!NM_SEP!>Top 2", DbColor(Color.Green)),
                DbId(8) to CategoryIdWithParentIds("7", "Top 3 - Sub 1<!NM_SEP!>Top 3", DbColor(Color.White)),
                DbId(3) to CategoryIdWithParentIds("2,1",
                        "Top 1 - Sub Sub 1<!NM_SEP!>Top 1 - Sub 1<!NM_SEP!>Top 1",
                        DbColor(Color.Red)),
        )
        val actual = categoryDao.getParentIds().first()

        expectedItems.forEach { (id, expected) ->
            assertEquals(expected, actual[id])
        }
        assertEquals(emptyMap<Int, CategoryIdWithParentIds>(),
                actual.filterNot { (key, _) -> expectedItems.keys.contains(key) })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetParentIdsFromId() = runTest {
        CategoryProvider.recursive.forEach { categoryDao.insert(it.asDbCategory()) }

        listOf(
                3 to CategoryIdWithParentIds("2,1",
                        "Top 1 - Sub Sub 1<!NM_SEP!>Top 1 - Sub 1<!NM_SEP!>Top 1",
                        DbColor(Color.Red)),
                2 to CategoryIdWithParentIds("1", "Top 1 - Sub 1<!NM_SEP!>Top 1", DbColor(Color.Red)),
                4 to CategoryIdWithParentIds("1", "Top 1 - Sub 2<!NM_SEP!>Top 1", DbColor(Color.Cyan)),
                7 to CategoryIdWithParentIds(null, "Top 3", DbColor(Color.White)),
                8 to CategoryIdWithParentIds("7", "Top 3 - Sub 1<!NM_SEP!>Top 3", DbColor(Color.White)),
        ).forEach { (id, expected) ->
            assertEquals(expected, categoryDao.getParentIds(id).first())
        }
    }
}
