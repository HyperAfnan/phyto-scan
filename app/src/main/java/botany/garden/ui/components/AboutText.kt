package botany.garden.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Charcoal

@Composable
fun AboutText(text: String, modifier: Modifier = Modifier) {
    Text(text = text, modifier = modifier, color = Charcoal, fontSize = 14.5.sp, lineHeight = 24.sp)
}
