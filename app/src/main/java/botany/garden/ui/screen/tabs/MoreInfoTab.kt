package botany.garden.ui.screen.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.components.TextFactItem
import botany.garden.ui.components.TextFactList
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.SubText

@Composable
fun MoreInfoTabContent(
    plant: Plant,
    onImageClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (plant.interestingFacts.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "INTERESTING FACTS", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                TextFactList(
                    items = plant.interestingFacts.map { TextFactItem(it.title, it.description, Icons.Outlined.Lightbulb) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        if (plant.doYouKnow.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "DO YOU KNOW?", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                TextFactList(
                    items = plant.doYouKnow.map { TextFactItem(it.title, it.description, Icons.Outlined.QuestionMark) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        if (plant.references.isNotEmpty()) {
            Column {
                SectionEyebrow(label = "REFERENCES", modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        plant.references.forEach { reference ->
                            Row {
                                Text(text = "–", color = SubText)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = reference,
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
    }
}
