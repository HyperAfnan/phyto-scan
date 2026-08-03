package botany.garden.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.PlantUse
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Ink

@Composable
private fun UseItem(label: String, description: String) {
    Row(Modifier.padding(end = 20.dp)) {
        androidx.compose.material3.Icon(Icons.Outlined.Park, null, tint = botany.garden.ui.theme.Oleander, modifier = Modifier.height(19.dp))
        Spacer(Modifier.width(11.dp))
        Column {
            Text(label, color = Ink, fontSize = 13.5.sp)
            Text(description, color = Charcoal, fontSize = 13.5.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
fun UseList(uses: List<PlantUse>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 20.dp)) {
        uses.forEachIndexed { index, use ->
            UseItem(use.label, use.description)
            if (index < uses.lastIndex) Spacer(Modifier.height(11.dp))
        }
    }
}
