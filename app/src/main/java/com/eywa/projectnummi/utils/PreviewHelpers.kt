package com.eywa.projectnummi.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * true if in a compose preview
 */
@Composable
fun isInEditMode() = LocalInspectionMode.current

/**
 * @returns the collection rearranged so that the result of sorting the items by index.toString()
 * is the input order
 */
fun <E> Collection<E>.sortPreviewParameters() =
        indices.sortedBy { it.toString() }.zip(this).sortedBy { it.first }.map { it.second }
