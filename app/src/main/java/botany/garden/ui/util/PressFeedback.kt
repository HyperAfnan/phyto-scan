package botany.garden.ui.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

class PressFeedbackState(
    val interactionSource: MutableInteractionSource,
    val scale: Float,
    val alpha: Float,
)

@Composable
fun rememberPressFeedbackState(
    scaleOnPress: Float = 0.97f,
    alphaOnPress: Float = 0.96f,
): PressFeedbackState {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (pressed) scaleOnPress else 1f,
        animationSpec = spring(stiffness = 520f, dampingRatio = 0.9f),
        label = "pressScale",
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (pressed) alphaOnPress else 1f,
        animationSpec = spring(stiffness = 520f, dampingRatio = 0.9f),
        label = "pressAlpha",
    )

    return PressFeedbackState(
        interactionSource = interactionSource,
        scale = animatedScale,
        alpha = animatedAlpha,
    )
}

fun Modifier.applyPressFeedback(state: PressFeedbackState): Modifier = this.graphicsLayer {
    scaleX = state.scale
    scaleY = state.scale
    alpha = state.alpha
}

fun resolveIntroPage(currentPage: Int, dragDistance: Float, pageCount: Int, threshold: Float = 72f): Int {
    return when {
        dragDistance < -threshold && currentPage < pageCount - 1 -> currentPage + 1
        dragDistance > threshold && currentPage > 0 -> currentPage - 1
        else -> currentPage
    }
}
