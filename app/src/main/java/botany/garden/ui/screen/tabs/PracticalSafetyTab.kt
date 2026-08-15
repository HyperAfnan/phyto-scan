package botany.garden.ui.screen.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.components.AyurvedicTree
import botany.garden.ui.components.ChipFlowRow
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.FernPale
import botany.garden.ui.theme.Moss
import coil.compose.AsyncImage

@Composable
fun PracticalSafetyTabContent(
    plant: Plant,
    onImageClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (plant.safetyPrecautions.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "SAFETY & PRECAUTIONS", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        plant.safetyPrecautions.forEach { precaution ->
                            Row {
                                Text(text = "•", color = Moss)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = precaution,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Charcoal,
                                    lineHeight = 20.sp,
                                )
                            }
                        }
                    }
                }
            }
        }

        if (plant.preparationSteps.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "PREPARATION & HOW TO USE", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        plant.preparationSteps.forEach { step ->
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                                    contentDescription = null,
                                    tint = Moss,
                                    modifier = Modifier.size(16.dp).offset(y = 2.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Charcoal,
                                )
                            }
                        }
                    }
                }
            }
        }

        if (plant.cultivationInfo.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "CULTIVATION INFORMATION", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    items(plant.cultivationInfo) { info ->
                        val cardModifier = if (info.imageUrl.isNotBlank()) {
                            Modifier.width(130.dp).clickable { onImageClick(info.imageUrl) }
                        } else {
                            Modifier.width(130.dp)
                        }
                        OutlinedCard(
                            modifier = cardModifier,
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .background(FernPale),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (info.imageUrl.isNotBlank()) {
                                        AsyncImage(
                                            model = "file:///android_asset/${info.imageUrl}",
                                            contentDescription = info.label,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    } else {
                                        Icon(imageVector = Icons.Outlined.Yard, contentDescription = null, tint = Moss)
                                    }
                                }
                                Text(
                                    text = info.label,
                                    style = MaterialTheme.typography.bodySmall,
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

        if (plant.medicinalParts.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "MEDICINAL PARTS USED", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    items(plant.medicinalParts) { part ->
                        val cardModifier = if (part.imageUrl.isNotBlank()) {
                            Modifier.width(100.dp).clickable { onImageClick(part.imageUrl) }
                        } else {
                            Modifier.width(100.dp)
                        }
                        OutlinedCard(
                            modifier = cardModifier,
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .background(FernPale),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (part.imageUrl.isNotBlank()) {
                                        AsyncImage(
                                            model = "file:///android_asset/${part.imageUrl}",
                                            contentDescription = part.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    } else {
                                        Icon(imageVector = Icons.Outlined.Eco, contentDescription = null, tint = Moss)
                                    }
                                }
                                Text(
                                    text = part.name,
                                    style = MaterialTheme.typography.bodySmall,
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

        if (plant.traditionalUses.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "TRADITIONAL USES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                ChipFlowRow(items = plant.traditionalUses, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }

        if (plant.ayurvedicProperties != null) {
            Column {
                SectionEyebrow(label = "AYURVEDIC PROPERTIES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                AyurvedicTree(plant.ayurvedicProperties, modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}
