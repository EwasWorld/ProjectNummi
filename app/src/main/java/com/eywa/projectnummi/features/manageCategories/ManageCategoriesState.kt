package com.eywa.projectnummi.features.manageCategories

import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.navigation.NummiNavRoute
import com.eywa.projectnummi.sharedUi.category.createCategoryDialog.CreateCategoryDialogState
import com.eywa.projectnummi.sharedUi.deleteConfirmationDialog.DeleteConfirmationDialogState
import com.eywa.projectnummi.sharedUi.manageItemDialog.ManageItemDialogState
import com.eywa.projectnummi.utils.CategorySortNode

data class ManageCategoriesState(
        val categories: List<Category>? = null,
        val createDialogState: CreateCategoryDialogState? = null,
        val manageItemDialogState: ManageItemDialogState<Category>? = null,
        val deleteDialogState: DeleteConfirmationDialogState<Category>? = null,
        val navigateInitiatedFor: NummiNavRoute? = null,
) {
    val deleteWithSubCategoriesState =
            deleteDialogState?.item?.takeIf { item -> categories?.any { it.hasParentWithId(item.id) } == true }

    val sortedCategories = categories?.let { CategorySortNode.generate(it).getOrdered() }
}

