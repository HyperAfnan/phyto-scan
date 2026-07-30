package botany.garden.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import botany.garden.ui.components.AboutText
import botany.garden.ui.components.BadgeData
import botany.garden.ui.components.BadgeRow
import botany.garden.ui.components.CareGrid
import botany.garden.ui.components.FactsAccordion
import botany.garden.ui.components.HeroSection
import botany.garden.ui.components.SectionEyebrow
import botany.garden.ui.components.TopBar
import botany.garden.ui.components.UseList
import botany.garden.ui.components.WarningCard
import botany.garden.ui.theme.Paper

@Composable
fun PlantDetailScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            HeroSection(
                commonName = "Oleander",
                scientificName = "Nerium oleander",
                familyTag = "Fam. Apocynaceae \u00b7 Specimen 014",
            )

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("AT A GLANCE", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))

            BadgeRow(
                badges = listOf(
                    BadgeData(Icons.Outlined.FavoriteBorder, "Conservation", "Least Concern"),
                    BadgeData(Icons.Outlined.LocationOn, "Native region", "Mediterranean & N. Africa"),
                    BadgeData(Icons.Outlined.WbSunny, "Flowering", "May \u2013 Sept"),
                    BadgeData(Icons.Outlined.Schedule, "Lifespan", "50+ years"),
                    BadgeData(Icons.Outlined.Grass, "Plant type", "Evergreen shrub"),
                    BadgeData(Icons.Outlined.Height, "Mature height", "2 \u2013 6 m"),
                    BadgeData(Icons.Outlined.TrendingUp, "Growth rate", "Fast"),
                ),
            )

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("SAFETY & WARNINGS", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            WarningCard(Modifier.padding(horizontal = 20.dp))

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("CARE & HABITAT", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            CareGrid()

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("ABOUT THE PLANT", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(12.dp))
            AboutText(Modifier.padding(horizontal = 20.dp))

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("EVERYDAY USES", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            UseList()

            Spacer(Modifier.height(22.dp))
            SectionEyebrow("FUN FACTS", Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(10.dp))
            FactsAccordion()

            Spacer(Modifier.height(120.dp))
        }

        TopBar(Modifier.padding(top = 40.dp))
    }
}
