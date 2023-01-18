package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.theme.colors.NummiColorTheme
import com.eywa.projectnummi.theme.currentColorTheme

@Composable
fun NummiScreenPreviewWrapper(theme: NummiColorTheme = currentColorTheme, content: @Composable () -> Unit) {
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

@Composable
fun NummiPreviewWrapper(theme: NummiColorTheme = currentColorTheme, content: @Composable () -> Unit) {
    NummiTheme(theme) {
        Box(
                modifier = Modifier.background(NummiTheme.colors.appBackground.main)
        ) {
            content()
        }
    }
}
