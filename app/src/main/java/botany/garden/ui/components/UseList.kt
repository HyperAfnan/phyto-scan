package botany.garden.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Oleander

@Composable
fun UseItem(
    icon: ImageVector,
    boldLabel: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(end = 20.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Oleander,
            modifier = Modifier.height(19.dp),
        )
        Spacer(Modifier.width(11.dp))
        Column {
            Text(
                text = boldLabel,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = botany.garden.ui.theme.Ink,
                ),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.5.sp,
                    color = Charcoal,
                    lineHeight = 20.sp,
                ),
            )
        }
    }
}

@Composable
fun UseList(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 20.dp)) {
        UseItem(
            icon = Icons.Outlined.Park,
            boldLabel = "Ornamental",
            description = "Widely planted along medians and boundary hedges for its fast growth and long bloom.",
        )
        Spacer(Modifier.height(11.dp))
        UseItem(
            icon = Icons.Outlined.Scale,
            boldLabel = "Historical medicine",
            description = "Extracts were once studied for heart conditions, but the margin between dose and poison is razor-thin \u2014 modern use is strictly pharmaceutical.",
        )
        Spacer(Modifier.height(11.dp))
        UseItem(
            icon = Icons.Outlined.House,
            boldLabel = "Cultural importance",
            description = "A fixture of Mediterranean courtyard gardens for centuries, and a recurring motif in regional art and poetry.",
        )
        Spacer(Modifier.height(11.dp))
        UseItem(
            icon = Icons.Outlined.Grass,
            boldLabel = "Erosion control",
            description = "Deep, spreading roots stabilise dry slopes and highway embankments.",
        )
        Spacer(Modifier.height(11.dp))
        UseItem(
            icon = Icons.Outlined.Architecture,
            boldLabel = "Wildlife interactions",
            description = "Toxicity keeps grazing animals away, while moths and bees still visit the flowers for nectar.",
        )
    }
}
