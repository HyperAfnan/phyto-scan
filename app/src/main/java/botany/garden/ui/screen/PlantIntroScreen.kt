package botany.garden.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Fern
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Line
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.Paper
import botany.garden.ui.theme.SubText

@Composable
fun PlantIntroScreen(plant: Plant, onComplete: () -> Unit) {
    var page by remember(plant.id) { mutableIntStateOf(0) }
    var dragDistance by remember { mutableFloatStateOf(0f) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .pointerInput(plant.id) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, amount -> dragDistance += amount },
                    onDragEnd = {
                        if (dragDistance < -70f && page < 3) page++
                        if (dragDistance > 70f && page > 0) page--
                        dragDistance = 0f
                    },
                )
            }
            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Box(Modifier.height(48.dp).clickable(onClick = onComplete).padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                Text("SKIP", color = Moss, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("❧", color = Fern, fontSize = 28.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            listOf("Meet the Plant", "Plant Snapshot", "Why It Matters", "Care Snapshot")[page],
            color = Ink,
            fontFamily = FontFamily.Serif,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(14.dp))
        Box(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            when (page) {
                0 -> MeetPlant(plant)
                1 -> Origin(plant)
                2 -> Matters(plant)
                else -> Care(plant, onComplete)
            }
        }
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                repeat(4) { index ->
                    Box(Modifier.size(if (index == page) 12.dp else 10.dp).clip(CircleShape).background(if (index == page) Moss else Line))
                }
            }
            Text("${page + 1} of 4", color = SubText, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            Box(Modifier.size(46.dp).clip(CircleShape).background(Moss).clickable { if (page == 3) onComplete() else page++ }, contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.ArrowForward, if (page == 3) "Explore full profile" else "Next", tint = CardBg, modifier = Modifier.size(21.dp))
            }
        }
    }
}

@Composable
private fun MeetPlant(plant: Plant) {
    Column(Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().height(290.dp).clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(Color(0xFFB4C999), Color(0xFF536C4B), Color(0xFFE7E8C8)))), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.LocalFlorist, null, tint = Color.White.copy(alpha = .9f), modifier = Modifier.size(120.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(plant.commonNames.first(), color = Ink, fontFamily = FontFamily.Serif, fontSize = 31.sp)
        Text(plant.botanicalName, color = Moss, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        Text("Family: ${plant.family}  ·  ${plant.pronunciation}", color = Charcoal, fontSize = 13.sp)
    }
}

@Composable
private fun Origin(plant: Plant) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.fillMaxWidth().height(68.dp).clip(RoundedCornerShape(18.dp)).background(Brush.horizontalGradient(listOf(Color(0xFFDDE7D1), Color(0xFFEAE2D2)))), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Public, "Native region map", tint = Moss, modifier = Modifier.size(42.dp))
        }
        IntroCard(Icons.Outlined.Grass, "Plant type", plant.badges.plantType)
        IntroCard(Icons.Outlined.LocationOn, "Native region", plant.badges.nativeRegion)
        IntroCard(Icons.Outlined.Height, "Mature height", plant.badges.matureHeight)
        IntroCard(Icons.Outlined.LocalFlorist, "Flowering season", plant.badges.flowering)
        Text("Botanical fact", color = Ink, fontFamily = FontFamily.Serif, fontSize = 20.sp)
        Text(plant.about, color = Charcoal, fontSize = 14.sp, lineHeight = 21.sp, maxLines = 5)
    }
}

@Composable
private fun Matters(plant: Plant) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        plant.uses.take(2).forEach { IntroCard(Icons.Outlined.LocalFlorist, it.label, it.description, Color(0xFFE8EFD9)) }
        plant.warnings?.let { IntroCard(Icons.Outlined.Warning, "Safety first", "${it.headline}. ${it.chips.joinToString()}", Color(0xFFF3E2CF)) }
    }
}

@Composable
private fun Care(plant: Plant, onComplete: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
            IntroCard(Icons.Outlined.WbSunny, plant.care.sunlight.label, plant.care.sunlight.value, modifier = Modifier.weight(1f))
            IntroCard(Icons.Outlined.WaterDrop, plant.care.water.label, plant.care.water.value, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
            IntroCard(Icons.Outlined.Terrain, plant.care.soil.label, plant.care.soil.value, modifier = Modifier.weight(1f))
            IntroCard(Icons.Outlined.AcUnit, plant.care.temperature.label, plant.care.temperature.value, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFD6B19E)).clickable(onClick = onComplete), contentAlignment = Alignment.Center) {
            Text("Explore Full Profile  →", color = Color.White, fontFamily = FontFamily.Serif, fontSize = 16.sp)
        }
    }
}

@Composable
private fun IntroCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color = CardBg, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(color).padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Moss, modifier = Modifier.size(23.dp))
            Spacer(Modifier.width(10.dp))
            Text(label, color = Ink, fontFamily = FontFamily.Serif, fontSize = 17.sp)
        }
        Text(value, color = Charcoal, fontSize = 12.sp, lineHeight = 18.sp)
    }
}
