package botany.garden.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import botany.garden.data.model.Plant
import botany.garden.ui.components.HeroSection
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.components.TopBar
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Paper

@Composable
fun PlantDetailScreen(plant: Plant?) {
    if (plant == null) {
        Text("Scan a plant name to open its page", modifier = Modifier.padding(24.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .verticalScroll(rememberScrollState()),
    ) {
        HeroSection(
            commonName = plant.commonNames.firstOrNull() ?: plant.id,
            scientificName = plant.botanicalName,
            familyTag = "Fam. ${plant.family} · ${plant.id}",
        )
        Spacer(Modifier.height(22.dp))

        plant.morphology?.let { morphology ->
            SectionEyebrow("MORPHOLOGY", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(8.dp))
            Text("Height: ${morphology.height}", modifier = Modifier.padding(horizontal = 20.dp))
            BulletList(morphology.description)
        }
        DetailSection("MICROSCOPY", plant.microscopy)
        plant.transverseSection?.let { DetailSection("TRANSVERSE SECTION · ${it.organ.uppercase()}", it.description) }
        DetailSection("CHEMICAL CONSTITUENTS", plant.chemicalConstituents)
        DetailSection("PHARMACOLOGICAL ACTIVITIES", plant.pharmacologicalActivities)
        DetailSection("TRADITIONAL USES", plant.traditionalUses)
        plant.quickFacts?.let { facts ->
            DetailSection("MEDICINAL PARTS", facts.medicinalParts)
            DetailSection("ACTIVE COMPOUNDS", facts.majorActiveCompounds)
            if (facts.specialFeature.isNotBlank()) DetailSection("SPECIAL FEATURE", listOf(facts.specialFeature))
        }
        Spacer(Modifier.height(120.dp))
    }
    TopBar(Modifier.padding(top = 40.dp))
}

@Composable
private fun DetailSection(title: String, items: List<String>) {
    if (items.isEmpty()) return
    Spacer(Modifier.height(22.dp))
    SectionEyebrow(title, Modifier.padding(horizontal = 20.dp))
    BulletList(items)
}

@Composable
private fun BulletList(items: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        items.forEach { item ->
            Text("• $item", style = MaterialTheme.typography.bodyMedium, color = Charcoal, modifier = Modifier.padding(vertical = 3.dp))
        }
    }
}
