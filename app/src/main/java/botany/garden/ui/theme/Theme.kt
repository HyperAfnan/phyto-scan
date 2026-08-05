package botany.garden.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = lightColorScheme(
    primary = Oleander,
    onPrimary = CardBg,
    surface = CardBg,
    onSurface = Ink,
    surfaceVariant = Paper,
    onSurfaceVariant = Charcoal,
    outline = Line,
    background = Paper,
    onBackground = Ink,
)

@Composable
fun BotanyGardenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content,
    )
}
