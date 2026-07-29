package botany.garden.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BadgeData(
    val icon: ImageVector,
    val label: String,
    val value: String,
)

@Composable
fun BadgeRow(badges: List<BadgeData>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
    ) {
        items(badges.size) { index ->
            val badge = badges[index]
            BadgeCard(icon = badge.icon, label = badge.label, value = badge.value)
        }
    }
}
