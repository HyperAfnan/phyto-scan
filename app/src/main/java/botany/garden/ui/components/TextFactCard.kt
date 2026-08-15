package botany.garden.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.SubText

data class TextFactItem(
    val title: String,
    val description: String = "",
    val icon: ImageVector? = null,
)

@Composable
fun TextFactList(
    items: List<TextFactItem>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { item ->
            TextFactCard(item = item)
        }
    }
}

@Composable
fun TextFactCard(
    item: TextFactItem,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Moss,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(top = 2.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = Charcoal,
                )
                if (item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = SubText,
                        lineHeight = 18.sp,
                    )
                }
            }
        }
    }
}
