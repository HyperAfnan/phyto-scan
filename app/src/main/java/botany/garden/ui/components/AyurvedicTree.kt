package botany.garden.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import botany.garden.data.model.AyurvedicPropertiesData
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.FernPale
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Moss

@Composable
fun AyurvedicTree(properties: AyurvedicPropertiesData, modifier: Modifier = Modifier) {
    val isEmpty = properties.rasa.isEmpty() && 
                  properties.guna.isEmpty() && 
                  properties.virya.isEmpty() && 
                  properties.doshaAction.isBlank()
                  
    if (isEmpty) return

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (properties.rasa.isNotEmpty()) {
                PropertyGroup(label = "Rasa · Taste", items = properties.rasa)
            }

            if (properties.guna.isNotEmpty()) {
                PropertyGroup(label = "Guna · Qualities", items = properties.guna)
            }

            if (properties.virya.isNotEmpty()) {
                PropertyGroup(label = "Virya · Potency", items = properties.virya)
            }

            if (properties.doshaAction.isNotBlank()) {
                Surface(
                    color = FernPale,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "DOSHA ACTION",
                            style = MaterialTheme.typography.labelMedium,
                            color = Moss,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = properties.doshaAction,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Charcoal,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyGroup(label: String, items: List<String>) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        color = Ink
    )

    PropertyPills(items = items)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PropertyPills(items: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    color = Charcoal,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
}
