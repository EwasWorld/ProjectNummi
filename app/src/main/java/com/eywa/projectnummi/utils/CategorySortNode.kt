package com.eywa.projectnummi.utils

import com.eywa.projectnummi.model.objects.Category

/**
 * Helper class for sorting categories.
 */
class CategorySortNode private constructor(
        private val category: Category?,
        private val children: MutableMap<Int, CategorySortNode> = mutableMapOf(),
) {
    fun add(newCategory: Category, parentIds: List<Int>?) {
        if (parentIds.isNullOrEmpty()) {
            check(category == null || category.id == newCategory.parentIds?.first()) { "Invalid add" }
            children[newCategory.id] = CategorySortNode(newCategory)
            return
        }

        children[parentIds.last()]!!.add(newCategory, parentIds.dropLast(1))
    }

    /**
     * @return a list of categories.
     * Categories with the same parent are sorted by name.
     * Sub-categories come immediately after their direct parent.
     * e.g. 1, 1.1, 1.1.1, 1.2, 2, 2.1, etc.
     */
    fun getOrdered(): List<Category> = listOfNotNull(category).plus(
            children
                    .map { (_, value) -> value.category to value.getOrdered() }
                    .sortedBy { it.first!!.name }
                    .flatMap { it.second }
    )

    companion object {
        fun generate(categories: List<Category>): CategorySortNode {
            val nodes = CategorySortNode(null)
            categories
                    .groupBy { it.parentIds?.size }
                    .entries
                    .sortedBy { (key, _) -> key ?: -1 }
                    .flatMap { it.value }
                    .forEach { nodes.add(it, it.parentIds) }
            return nodes
        }
    }
}
