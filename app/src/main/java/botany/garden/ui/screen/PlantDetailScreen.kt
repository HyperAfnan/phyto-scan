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
import botany.garden.ui.components.AboutText
import botany.garden.ui.components.BadgeData
import botany.garden.ui.components.BadgeRow
import botany.garden.ui.components.CareGrid
import botany.garden.ui.components.FactsAccordion
import botany.garden.ui.components.HeroSection
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.components.TopBar
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Paper

@Composable
fun PlantDetailScreen(plant: Plant) {
    val badges = plant.badges

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            HeroSection(
                commonName = plant.commonNames.first(),
                scientificName = plant.botanicalName,
                familyTag = "Fam. ${plant.family} · ${plant.id}",
                pronunciation = plant.pronunciation,
            )

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("AT A GLANCE", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            BadgeRow(
                badges = listOf(
                    BadgeData(Icons.Outlined.FavoriteBorder, "Conservation", badges.conservation),
                    BadgeData(Icons.Outlined.LocationOn, "Native region", badges.nativeRegion),
                    BadgeData(Icons.Outlined.WbSunny, "Flowering", badges.flowering),
                    BadgeData(Icons.Outlined.Schedule, "Lifespan", badges.lifespan),
                    BadgeData(Icons.Outlined.Grass, "Plant type", badges.plantType),
                    BadgeData(Icons.Outlined.Height, "Mature height", badges.matureHeight),
                    BadgeData(Icons.Outlined.TrendingUp, "Growth rate", badges.growthRate),
                ),
            )

            plant.warnings?.let { warning ->
                Spacer(Modifier.height(22.dp))
                SectionEyebrow("SAFETY & WARNINGS", Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(10.dp))
                WarningCard(warning.headline, warning.chips, Modifier.padding(horizontal = 20.dp))
            }

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("CARE & HABITAT", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            CareGrid(plant.care)

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("ABOUT THE PLANT", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(12.dp))
            AboutText(plant.about, Modifier.padding(horizontal = 20.dp))

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("EVERYDAY USES", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            UseList(plant.uses)

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("FUN FACTS", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            FactsAccordion(plant.funFacts)
            Spacer(Modifier.height(120.dp))
        }
        TopBar(Modifier.padding(top = 40.dp))
    }
}
