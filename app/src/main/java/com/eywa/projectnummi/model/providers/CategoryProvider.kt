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
    ).mapIndexed { index, (name, hue) -> Category(index + 1, name, ColorUtils.asCategoryColor(hue)) }

    val recursive = mutableListOf<Category>().apply {
        val top1 = Category(1, "Top 1", Color.Red, null, false)
        add(top1)
        val top1Sub1 = Category(2, "Top 1 - Sub 1", Color.Yellow, top1, true)
        add(top1Sub1)
        val top1SubSub1 = Category(3, "Top 1 - Sub Sub 1", Color.Blue, top1Sub1, true)
        add(top1SubSub1)
        val top1Sub2 = Category(4, "Top 1 - Sub 2", Color.Cyan, top1, false)
        add(top1Sub2)

        val top2 = Category(5, "Top 2", Color.Magenta, null, false)
        add(top2)
        val top2Sub1 = Category(6, "Top 2 - Sub 1", Color.Green, top2, false)
        add(top2Sub1)

        val top3 = Category(7, "Top 3", Color.White, null, true)
        add(top3)
        val top3Sub1 = Category(8, "Top 3 - Sub 1", Color.Black, top3, true)
        add(top3Sub1)
    }.toList()
}
