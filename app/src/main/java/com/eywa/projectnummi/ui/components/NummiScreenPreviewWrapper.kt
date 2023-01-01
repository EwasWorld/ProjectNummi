package com.eywa.projectnummi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.NummiColorTheme
import com.eywa.projectnummi.ui.theme.currentAppTheme

@Composable
fun NummiScreenPreviewWrapper(theme: NummiColorTheme = currentAppTheme, content: @Composable () -> Unit) {
    NummiTheme(theme) {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(NummiTheme.colors.appBackground.main)
        ) {
            content()
        }
    }
}
