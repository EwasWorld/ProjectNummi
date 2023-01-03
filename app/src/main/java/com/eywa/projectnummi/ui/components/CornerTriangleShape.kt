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
import com.eywa.projectnummi.common.ColorHelper
import com.eywa.projectnummi.common.Polar
import com.eywa.projectnummi.ui.theme.NummiTheme
import com.eywa.projectnummi.ui.theme.colors.BaseColor

data class CornerTriangleShapeState(
        val isTop: Boolean = true,
        val isLeft: Boolean = true,
        val xScale: Float = 1f,
        val yScale: Float = 1f,
        val totalSegments: Int = 1,
        /**
         * True: Each segment is given the same percentage distance along the tangent
         * False: Each segment is given the same arc angle
         */
        val usePercentage: Boolean = true,
) {

    fun getShape(segmentIndex: Int = 0): GenericShape =
            if (usePercentage) {
                val percentage = 1f / totalSegments
                CornerTriangleShapePercentage(this, segmentIndex * percentage, percentage)
            }
            else {
                CornerTriangleShapeArc(this, segmentIndex)
            }
}

/**
 * Forces [CornerTriangleShapeState.totalSegments] to be [colors].size
 */
@Composable
fun BoxScope.CornerTriangleBox(
        colors: List<Color>,
        modifier: Modifier = Modifier,
        segmentModifier: Modifier = Modifier,
        state: CornerTriangleShapeState = CornerTriangleShapeState(),
) {
    require(colors.isNotEmpty()) { "Must provide at least one colour" }
    Box(
            modifier = modifier
                    .matchParentSize()
                    .clip(state
                            .copy(totalSegments = 1)
                            .getShape())
    ) {
        val adjustedState = state.copy(totalSegments = colors.size)
        colors.forEachIndexed { index, color ->
            CornerTriangleBox(
                    color = color,
                    state = adjustedState,
                    segmentIndex = index,
                    modifier = modifier.then(segmentModifier)
            )
        }
    }
}

@Composable
fun BoxScope.CornerTriangleBox(
        color: Color,
        modifier: Modifier = Modifier,
        state: CornerTriangleShapeState = CornerTriangleShapeState(),
        segmentIndex: Int = 0,
) {
    Box(
            modifier = modifier
                    .matchParentSize()
                    .clip(state.getShape(segmentIndex))
                    .background(color)
    )
}

/**
 * Each segment is given the same percentage distance along the tangent
 */
@Suppress("FunctionName") // because I'm not allowed to extend GenericShape -.-
fun CornerTriangleShapePercentage(
        state: CornerTriangleShapeState,
        startPercentage: Float = 0f,
        percentage: Float = 0f,
) = GenericShape { size, _ ->
    fun xModifier(x: Float) = if (state.isLeft) state.xScale * x else (size.width - state.xScale * x)
    fun yModifier(y: Float) = if (state.isTop) state.yScale * y else (size.height - state.yScale * y)
    val xRelativeModifier = if (state.isLeft) 1f else -1f
    val yRelativeModifier = if (state.isTop) 1f else -1f

    // Start [height] along the width towards the centre
    moveTo(xModifier(size.height), yModifier(0f))

    // Move the start percentage in
    relativeMoveTo(
            xRelativeModifier * (-size.height * state.xScale) * startPercentage,
            yRelativeModifier * (size.height * state.yScale) * startPercentage,
    )

    // Draw up to the end percentage
    relativeLineTo(
            xRelativeModifier * (-size.height * state.xScale) * percentage,
            yRelativeModifier * (size.height * state.yScale) * percentage,
    )

    // End in the corner
    lineTo(xModifier(0f), yModifier(0f))
}

/**
 * Each segment is given the same arc angle
 */
@Suppress("FunctionName") // because I'm not allowed to extend GenericShape -.-
fun CornerTriangleShapeArc(
        state: CornerTriangleShapeState,
        segmentIndex: Int = 0,
) = GenericShape { size, _ ->
    fun xModifier(x: Float) = if (state.isLeft) state.xScale * x else (size.width - state.xScale * x)
    fun yModifier(y: Float) = if (state.isTop) state.yScale * y else (size.height - state.yScale * y)

    val segmentArc = (Math.PI.toFloat() / 2f) / state.totalSegments
    val pointOne = Polar(size.height, segmentIndex * segmentArc).toCartesian()
    val pointTwo = Polar(size.height, (segmentIndex + 1) * segmentArc).toCartesian()

    // Start in the corner
    moveTo(xModifier(0f), yModifier(0f))
    // Move along the width towards the centre
    lineTo(xModifier(pointOne.x), yModifier(pointOne.y))
    // Move back to the edge vertically connected to the starting corner
    lineTo(xModifier(pointTwo.x), yModifier(pointTwo.y))
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
                modifier = Modifier
                        .size(300.dp, 100.dp)
                        .background(BaseColor.BASE_BLACK)
        ) {
            CornerTriangleBox(
                    colors = params.colors,
                    state = params.state,
                    segmentModifier = Modifier.alpha(if (params.colors.size == 1) 0.3f else 1f)
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
        val state: CornerTriangleShapeState = CornerTriangleShapeState(),
        val colors: List<Color> = listOf(BaseColor.GREEN),
)

@Suppress("BooleanLiteralArgument")
class CornerTriangleShapePreviewProvider : CollectionPreviewParameterProvider<CornerTriangleShapePreviewParams>(
        listOf(
                CornerTriangleShapePreviewParams("percentage",
                        CornerTriangleShapeState(usePercentage = true),
                        colors = listOf(0f, 0.3f, 0.6f, 0.8f).map { ColorHelper.asCategoryColor(it) }),
                CornerTriangleShapePreviewParams("arc",
                        CornerTriangleShapeState(usePercentage = false),
                        listOf(0f, 0.3f, 0.6f, 0.8f).map { ColorHelper.asCategoryColor(it) }),
                CornerTriangleShapePreviewParams("topLeft", CornerTriangleShapeState(true, true, 1f, 1f)),
                CornerTriangleShapePreviewParams("topRight", CornerTriangleShapeState(true, false, 1f, 1f)),
                CornerTriangleShapePreviewParams("bottomLeft", CornerTriangleShapeState(false, true, 1f, 1f)),
                CornerTriangleShapePreviewParams("bottomRight", CornerTriangleShapeState(false, false, 1f, 1f)),
                CornerTriangleShapePreviewParams("doubleX", CornerTriangleShapeState(true, true, 2f, 1f)),
                CornerTriangleShapePreviewParams("doubleY", CornerTriangleShapeState(true, true, 1f, 2f)),
                CornerTriangleShapePreviewParams("halfSize", CornerTriangleShapeState(true, true, 0.5f, 0.5f)),
                CornerTriangleShapePreviewParams("negative", CornerTriangleShapeState(false, false, 3.5f, 3f)),
        )
)
