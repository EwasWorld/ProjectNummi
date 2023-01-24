package com.eywa.projectnummi.model.providers

import androidx.compose.ui.graphics.Color
import com.eywa.projectnummi.model.objects.Category
import com.eywa.projectnummi.utils.ColorUtils

object CategoryProvider {
    val basic = listOf(
            "Entertainment" to 0f,
            "Groceries" to 0.33f,
            "Income" to 0.66f,
            "Work" to 0.8f,
            "Utilities" to 0.2f,
    )
            .mapIndexed { index, (name, hue) ->
                Category(
                        id = index + 1,
                        name = name,
                        dbColor = ColorUtils.asCategoryColor(hue),
                        allNames = listOf(name),
                )
            }
            .plus(
                    listOf(
                            Category(
                                    id = 6,
                                    name = "Sport",
                                    dbColor = ColorUtils.asCategoryColor(0.5f),
                                    matchParentColor = false,
                                    parentIds = null,
                                    allNames = listOf("Sport"),
                            ),
                            Category(
                                    id = 7,
                                    name = "Equipment",
                                    dbColor = Color.Red,
                                    matchParentColor = true,
                                    displayColor = ColorUtils.asCategoryColor(0.5f),
                                    parentIds = listOf(6),
                                    allNames = listOf("Equipment", "Sport"),
                            ),
                            Category(
                                    id = 8,
                                    name = "Membership",
                                    dbColor = Color.Gray,
                                    matchParentColor = false,
                                    displayColor = Color.Gray,
                                    parentIds = listOf(6),
                                    allNames = listOf("Membership", "Sport"),
                            ),
                    )
            )

    val recursive = mutableListOf<Category>().apply {
        add(
                Category(
                        id = 1,
                        name = "Top 1",
                        dbColor = Color.Red,
                        matchParentColor = false,
                        displayColor = Color.Red,
                        parentIds = null,
                        allNames = listOf("Top 1"),
                )
        )
        add(
                Category(
                        id = 2,
                        name = "Top 1 - Sub 1",
                        dbColor = Color.Yellow,
                        matchParentColor = true,
                        displayColor = Color.Red,
                        parentIds = listOf(1),
                        allNames = listOf("Top 1 - Sub 1", "Top 1"),
                )
        )
        add(
                Category(
                        id = 3,
                        name = "Top 1 - Sub Sub 1",
                        dbColor = Color.Blue,
                        matchParentColor = true,
                        displayColor = Color.Red,
                        parentIds = listOf(2, 1),
                        allNames = listOf("Top 1 - Sub Sub 1", "Top 1 - Sub 1", "Top 1"),
                )
        )
        add(
                Category(
                        id = 4,
                        name = "Top 1 - Sub 2",
                        dbColor = Color.Cyan,
                        matchParentColor = false,
                        displayColor = Color.Cyan,
                        parentIds = listOf(1),
                        allNames = listOf("Top 1 - Sub 2", "Top 1"),
                )
        )

        add(
                Category(
                        id = 5,
                        name = "Top 2",
                        dbColor = Color.Magenta,
                        matchParentColor = false,
                        displayColor = Color.Magenta,
                        parentIds = null,
                        allNames = listOf("Top 2"),
                )
        )
        add(
                Category(
                        id = 6,
                        name = "Top 2 - Sub 1",
                        dbColor = Color.Green,
                        matchParentColor = false,
                        displayColor = Color.Green,
                        parentIds = listOf(5),
                        allNames = listOf("Top 2 - Sub 1", "Top 2"),
                )
        )

        add(
                Category(
                        id = 7,
                        name = "Top 3",
                        dbColor = Color.White,
                        matchParentColor = true,
                        displayColor = Color.White,
                        parentIds = null,
                        allNames = listOf("Top 3"),
                )
        )
        add(
                Category(
                        id = 8,
                        name = "Top 3 - Sub 1",
                        dbColor = Color.Black,
                        matchParentColor = true,
                        displayColor = Color.White,
                        parentIds = listOf(7),
                        allNames = listOf("Top 3 - Sub 1", "Top 3"),
                )
        )
    }.toList()
}
