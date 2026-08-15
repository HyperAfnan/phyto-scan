package botany.garden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import botany.garden.data.model.CommonNameEntry
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.FernPale
import botany.garden.ui.theme.Line
import botany.garden.ui.theme.Paper

@Composable
fun CommonNamesTable(entries: List<CommonNameEntry>, modifier: Modifier = Modifier) {
    if (entries.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Line, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FernPale)
                .padding(12.dp)
        ) {
            Text(
                text = "Language",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Name",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(2f)
            )
        }

        entries.forEachIndexed { index, entry ->
            val bgColor = if (index % 2 == 0) CardBg else Paper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .padding(12.dp)
            ) {
                Text(
                    text = entry.language,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}
