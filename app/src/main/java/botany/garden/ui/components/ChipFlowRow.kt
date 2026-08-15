package botany.garden.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Line

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipFlowRow(items: List<String>, modifier: Modifier = Modifier) {
    if (items.isEmpty()) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEach { item ->
            AssistChip(
                onClick = { /* No-op */ },
                label = { Text(item, color = Charcoal) },
                shape = RoundedCornerShape(20.dp),
                colors = AssistChipDefaults.assistChipColors(containerColor = CardBg),
                border = BorderStroke(1.dp, Line),
            )
        }
    }
}
