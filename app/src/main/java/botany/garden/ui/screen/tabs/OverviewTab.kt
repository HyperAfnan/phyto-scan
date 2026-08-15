package botany.garden.ui.screen.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import botany.garden.data.model.Plant
import botany.garden.ui.components.AboutText
import botany.garden.ui.components.ChipFlowRow
import botany.garden.ui.components.CommonNamesTable
import botany.garden.ui.components.InfoCardGrid
import botany.garden.ui.components.InfoCardItem
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.FernPale
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.SubText

@Composable
fun OverviewTabContent(
    plant: Plant,
    onImageClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (plant.about.isNotBlank()) {
            AboutText(
                text = plant.about,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        if (plant.synonyms.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "SYNONYMES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                ChipFlowRow(items = plant.synonyms, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }

        if (plant.commonNameTable.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "COMMON NAMES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                CommonNamesTable(entries = plant.commonNameTable, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }

        if (plant.spotImages.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "HOW TO SPOT?", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                InfoCardGrid(
                    items = plant.spotImages.map { InfoCardItem(it.caption, it.imageUrl, Icons.Outlined.Eco) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onImageClick = onImageClick,
                )
            }
        }

        if (plant.habitat.isNotBlank()) {
            Column {
                SectionEyebrow(label = "HABITAT", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(FernPale),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.Map,
                                contentDescription = "Map",
                                modifier = Modifier.size(48.dp),
                                tint = Moss,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Map", color = SubText)
                            Text(
                                text = plant.habitat,
                                color = Charcoal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
