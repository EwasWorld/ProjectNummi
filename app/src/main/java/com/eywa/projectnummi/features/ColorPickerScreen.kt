package com.eywa.projectnummi.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.theme.NummiTheme
import com.eywa.projectnummi.utils.ColorUtils

// TODO Move to a debug variant folder
@Composable
fun ColorPickerScreen() {
    var color by remember { mutableStateOf(0.5f) }
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                    .fillMaxSize()
                    .padding(NummiTheme.dimens.screenPadding)
    ) {
        Text(
                text = "%.4f".format(color),
                color = NummiTheme.colors.appBackground.content,
                style = NummiTheme.typography.h3,
        )
        Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(ColorUtils.asCategoryColor(color))
        )
        Slider(
                value = color,
                onValueChange = { color = it },
                colors = NummiTheme.colors.generalSlider(),
        )
    }
}
