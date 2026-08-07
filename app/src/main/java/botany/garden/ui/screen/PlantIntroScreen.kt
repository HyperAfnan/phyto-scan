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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import botany.garden.data.model.Plant
import botany.garden.ui.components.CareItem
import botany.garden.ui.components.HeroSection
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
    var appeared by remember(plant.id) { mutableStateOf(false) }

    LaunchedEffect(plant.id) { appeared = true }

    val entranceAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(500),
        label = "entranceAlpha",
    )
    val entranceOffset by animateFloatAsState(
        targetValue = if (appeared) 0f else 32f,
        animationSpec = tween(500),
        label = "entranceOffset",
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .alpha(entranceAlpha)
            .offset(y = entranceOffset.dp)
            .pointerInput(plant.id) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, amount -> dragDistance += amount },
                    onDragEnd = {
                        page = resolveIntroPage(page, dragDistance, 4)
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
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                (slideInHorizontally { width -> direction * width / 3 } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { width -> -direction * width / 3 } + fadeOut(tween(200)))
            },
            label = "introTitleTransition",
        ) { targetPage ->
            Text(
                listOf("Meet the Plant", "Plant Snapshot", "Why It Matters", "Care Snapshot")[targetPage],
                color = Ink,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Spacer(Modifier.height(14.dp))
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                (slideInHorizontally { width -> direction * width / 3 } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { width -> -direction * width / 3 } + fadeOut(tween(200)))
            },
            modifier = Modifier.weight(1f).fillMaxWidth(),
            label = "introContentTransition",
        ) { targetPage ->
            Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                when (targetPage) {
                    0 -> MeetPlant(plant)
                    1 -> Origin(plant)
                    2 -> Matters(plant)
                    else -> Care(plant, onComplete)
                }
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
    HeroSection(plant = plant, modifier = Modifier.clip(RoundedCornerShape(24.dp)))
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
            CareItem(plant.care.sunlight, Icons.Outlined.WbSunny, modifier = Modifier.weight(1f))
            CareItem(plant.care.water, Icons.Outlined.WaterDrop, modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
            CareItem(plant.care.soil, Icons.Outlined.Terrain, modifier = Modifier.weight(1f))
            CareItem(plant.care.temperature, Icons.Outlined.AcUnit, modifier = Modifier.weight(1f))
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

private fun resolveIntroPage(currentPage: Int, dragDistance: Float, pageCount: Int, threshold: Float = 72f): Int {
    return when {
        dragDistance < -threshold && currentPage < pageCount - 1 -> currentPage + 1
        dragDistance > threshold && currentPage > 0 -> currentPage - 1
        else -> currentPage
    }
}

