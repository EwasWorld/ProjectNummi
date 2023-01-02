package com.eywa.projectnummi.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * true if in a compose preview
 */
@Composable
fun isInEditMode() = LocalInspectionMode.current