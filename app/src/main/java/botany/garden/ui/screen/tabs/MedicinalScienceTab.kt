package botany.garden.ui.screen.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.components.ChipFlowRow
import botany.garden.ui.components.MedicinalUseCard
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.theme.Charcoal

@Composable
fun MedicinalScienceTabContent(plant: Plant) {
    Column(
        modifier = Modifier.padding(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (plant.medicinalUses.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "MEDICINAL USES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    plant.medicinalUses.forEachIndexed { index, use ->
                        val icon = when (index) {
                            0 -> Icons.Outlined.FavoriteBorder
                            1 -> Icons.Outlined.Spa
                            2 -> Icons.Outlined.Shield
                            else -> Icons.Outlined.LocalHospital
                        }
                        MedicinalUseCard(
                            title = use.title,
                            points = use.points,
                            icon = icon
                        )
                    }
                }
            }
        }
        
        if (plant.pharmacologicalActivities.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "PHARMACOLOGICAL ACTIVITIES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                ChipFlowRow(items = plant.pharmacologicalActivities, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
        
        if (plant.phytochemicals.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "MAJOR PHYTOCHEMICALS", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                ChipFlowRow(items = plant.phytochemicals, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
        
        if (plant.scientificEvidence.isNotBlank()) {
            Column {
                SectionEyebrow(label = "MODERN SCIENTIFIC EVIDENCE", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = plant.scientificEvidence,
                        style = MaterialTheme.typography.bodySmall,
                        color = Charcoal,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
