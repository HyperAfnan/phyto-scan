package botany.garden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.FernPale
import botany.garden.ui.theme.Moss
import coil.compose.AsyncImage

data class InfoCardItem(
    val label: String,
    val imageUrl: String = "",
    val icon: ImageVector? = null,
)

@Composable
fun InfoCardGrid(
    items: List<InfoCardItem>,
    columns: Int = 2,
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit = {},
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val chunkedItems = items.chunked(columns)
        chunkedItems.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { item ->
                    InfoCard(
                        item = item,
                        modifier = Modifier.weight(1f),
                        onImageClick = onImageClick,
                    )
                }

                // Fill empty slots in incomplete rows
                val emptySlots = columns - rowItems.size
                repeat(emptySlots) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    item: InfoCardItem,
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit = {},
) {
    val isClickable = item.imageUrl.isNotBlank()
    val cardModifier = if (isClickable) {
        modifier.clickable { onImageClick(item.imageUrl) }
    } else {
        modifier
    }

    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        modifier = cardModifier,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(FernPale),
                contentAlignment = Alignment.Center,
            ) {
                if (item.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = "file:///android_asset/${item.imageUrl}",
                        contentDescription = item.label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Moss,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall,
                color = Charcoal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            )
        }
    }
}
