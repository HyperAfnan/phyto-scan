package botany.garden.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import botany.garden.ui.components.UseList
import botany.garden.ui.components.WarningCard
import botany.garden.ui.theme.Paper

@Composable
fun PlantDetailScreen(plant: Plant) {
    val badges = plant.badges
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(plant.id) {
        visible = true
    }

    val heroAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(stiffness = 260f, dampingRatio = 0.92f),
        label = "heroAlpha",
    )
    val heroOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 24f,
        animationSpec = spring(stiffness = 260f, dampingRatio = 0.92f),
        label = "heroOffset",
    )

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = heroOffset.dp)
                    .alpha(heroAlpha),
            ) {
                HeroSection(
                    commonName = plant.commonNames.first(),
                    scientificName = plant.botanicalName,
                    familyTag = "Fam. ${plant.family} · ${plant.id}",
                    pronunciation = plant.pronunciation,
                )
            }

            Spacer(Modifier.height(22.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = heroOffset.dp * 0.7f)
                    .alpha(heroAlpha),
            ) {
                Column {
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
            }
        }
        TopBar(Modifier.padding(top = 40.dp))
    }
}
