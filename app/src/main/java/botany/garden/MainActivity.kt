package botany.garden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import botany.garden.data.model.Plant
import botany.garden.data.repository.PlantRepository
import botany.garden.ui.components.BottomNavBar
import botany.garden.ui.screen.ExploreScreen
import botany.garden.ui.screen.PlantDetailScreen
import botany.garden.ui.screen.PlantIntroScreen
import botany.garden.ui.screen.ScanScreen
import botany.garden.ui.theme.BotanyGardenTheme
import botany.garden.ui.theme.Paper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotanyGardenTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(2) }
    val context = LocalContext.current
    val repository = remember(context) { PlantRepository.getInstance(context) }
    var plants by remember { mutableStateOf<List<Plant>>(emptyList()) }
    var selectedPlantId by remember { mutableStateOf<String?>(null) }
    var showIntro by remember { mutableStateOf(false) }
    var previousTab by remember { mutableIntStateOf(2) }
    val selectedPlant = plants.firstOrNull { it.id == selectedPlantId }

    LaunchedEffect(repository) {
        plants = repository.loadPlants()
    }

    fun openPlant(plant: Plant) {
        previousTab = selectedTab
        selectedPlantId = plant.id
        selectedTab = 1
        showIntro = !isIntroSeen(context, plant.id)
    }

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                val direction = if (targetState > initialState) -1 else 1
                (slideInHorizontally { width -> direction * width / 3 } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { width -> -direction * width / 3 } + fadeOut(tween(200)))
            },
            modifier = Modifier.fillMaxSize(),
            label = "tabTransition",
        ) { tab ->
            when (tab) {
                0 -> ExploreScreen(repository = repository, onPlantSelected = ::openPlant)
                1 -> selectedPlant?.let { plant ->
                    if (showIntro) {
                        PlantIntroScreen(plant = plant, onComplete = {
                            markIntroSeen(context, plant.id)
                            showIntro = false
                        })
                    } else {
                        PlantDetailScreen(plant, onBack = { selectedTab = previousTab })
                    }
                } ?: Text("Loading…")
                2 -> ScanScreen(repository = repository, onPlantFound = ::openPlant)
            }
        }

        if (!(selectedTab == 1 && showIntro)) {
            BottomNavBar(
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                    .padding(horizontal = 14.dp, vertical = 14.dp),
            )
        }
    }
}

private fun isIntroSeen(context: android.content.Context, plantId: String) =
    context.getSharedPreferences("plant_intro", 0).getBoolean("intro_seen_$plantId", false)

private fun markIntroSeen(context: android.content.Context, plantId: String) {
    context.getSharedPreferences("plant_intro", 0).edit().putBoolean("intro_seen_$plantId", true).apply()
}
