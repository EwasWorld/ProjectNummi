package com.eywa.projectnummi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.BaseColor

@Composable
fun BoxScope.CornerTriangleBox(
        color: Color,
        modifier: Modifier = Modifier,
        isTop: Boolean = true,
        isLeft: Boolean = true,
        xScale: Float = 1f,
        yScale: Float = 1f,
) {
    Box(
            modifier = modifier
                    .matchParentSize()
                    .clip(CornerTriangleShape(isTop, isLeft, xScale, yScale))
                    .background(color)
    )
}

@Suppress("FunctionName") // because I'm not allowed to extend GenericShape -.-
fun CornerTriangleShape(
        isTop: Boolean = true,
        isLeft: Boolean = true,
        xScale: Float = 1f,
        yScale: Float = 1f,
) = GenericShape { size, _ ->
    fun xModifier(x: Float) = if (isLeft) xScale * x else (size.width - xScale * x)
    fun yModifier(y: Float) = if (isTop) yScale * y else (size.height - yScale * y)

    // Start in the corner
    moveTo(xModifier(0f), yModifier(0f))
    // Move along the width towards the centre
    lineTo(xModifier(size.height), yModifier(0f))
    // Move back to the edge vertically connected to the starting corner
    lineTo(xModifier(0f), yModifier(size.height))
}

@Preview
@Composable
fun CornerTriangleShape_Preview(
        @PreviewParameter(provider = CornerTriangleShapePreviewProvider::class)
        params: CornerTriangleShapePreviewParams,
) {
    NummiTheme {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp, 100.dp)
        ) {
            CornerTriangleBox(
                    color = BaseColor.GREEN,
                    isTop = params.isTop,
                    isLeft = params.isLeft,
                    xScale = params.xScale,
                    yScale = params.yScale,
                    modifier = Modifier.alpha(0.3f)
            )
            Text(
                    text = params.description,
                    color = Color.White,
            )
        }
    }
}

data class CornerTriangleShapePreviewParams(
        val description: String,
        val isTop: Boolean = true,
        val isLeft: Boolean = true,
        val xScale: Float = 1f,
        val yScale: Float = 1f,
)

@Suppress("BooleanLiteralArgument")
class CornerTriangleShapePreviewProvider : CollectionPreviewParameterProvider<CornerTriangleShapePreviewParams>(
        listOf(
                CornerTriangleShapePreviewParams("topLeft", true, true, 1f, 1f),
                CornerTriangleShapePreviewParams("topRight", true, false, 1f, 1f),
                CornerTriangleShapePreviewParams("bottomLeft", false, true, 1f, 1f),
                CornerTriangleShapePreviewParams("bottomRight", false, false, 1f, 1f),
                CornerTriangleShapePreviewParams("doubleX", true, true, 2f, 1f),
                CornerTriangleShapePreviewParams("doubleY", true, true, 1f, 2f),
                CornerTriangleShapePreviewParams("halfSize", true, true, 0.5f, 0.5f),
                CornerTriangleShapePreviewParams("negative", false, false, 3.5f, 3f),
        )
)
