package com.eywa.projectnummi.sharedUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.theme.colors.NummiColorTheme
import com.eywa.projectnummi.theme.currentColorTheme

@Composable
fun NummiScreenPreviewWrapper(
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        theme: NummiColorTheme = currentColorTheme,
        content: @Composable () -> Unit
) = NummiPreviewWrapper(
        theme = theme,
        contentPadding = contentPadding,
        content = content,
        modifier = modifier.fillMaxSize()
)

@Composable
fun NummiPreviewWrapper(
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        theme: NummiColorTheme = currentColorTheme,
        content: @Composable () -> Unit
) {
    NummiTheme(theme) {
        Box(
                modifier = modifier
                        .background(NummiTheme.colors.appBackground.main)
                        .padding(contentPadding)
        ) {
            content()
        }
    }
}
