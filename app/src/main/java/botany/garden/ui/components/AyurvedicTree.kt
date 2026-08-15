package botany.garden.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import botany.garden.data.model.AyurvedicPropertiesData
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.SubText

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
        Column(modifier = Modifier.padding(16.dp)) {
            if (properties.rasa.isNotEmpty()) {
                TreeSection(label = "Rasa (Taste)", items = properties.rasa)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (properties.guna.isNotEmpty()) {
                TreeSection(label = "Guna (Properties)", items = properties.guna)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (properties.virya.isNotEmpty()) {
                TreeSection(label = "Virya (Potency)", items = properties.virya)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (properties.doshaAction.isNotBlank()) {
                Text(
                    text = "Dosha Action",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Ink
                )
                Row(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                    Text(text = "└─ ", color = SubText)
                    Text(text = properties.doshaAction, color = Charcoal)
                }
            }
        }
    }
}

@Composable
private fun TreeSection(label: String, items: List<String>) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Ink
    )
    items.forEachIndexed { index, item ->
        val prefix = if (index == items.size - 1) "└─ " else "├─ "
        Row(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
            Text(text = prefix, color = SubText)
            Text(text = item, color = Charcoal)
        }
    }
}
