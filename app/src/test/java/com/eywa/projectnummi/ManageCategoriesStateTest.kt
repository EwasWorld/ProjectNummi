package com.eywa.projectnummi

import com.eywa.projectnummi.model.providers.CategoryProvider
import com.eywa.projectnummi.utils.CategorySortNode
import org.junit.Assert.assertEquals
import org.junit.Test

class ManageCategoriesStateTest {
    @Test
    fun testSort_basic() {
        val categories = CategoryProvider.basic
        val idOrder = listOf(1, 2, 3, 6, 7, 8, 5, 4)
        assertEquals(
                idOrder,
                CategorySortNode.generate(categories).getOrdered().map { it.id },
        )
    }

    @Test
    fun testSort_recursive() {
        val categories = CategoryProvider.recursive
        assertEquals(
                (1..8).toList(),
                CategorySortNode.generate(categories).getOrdered().map { it.id },
        )
    }
}
