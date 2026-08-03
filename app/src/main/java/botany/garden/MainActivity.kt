package botany.garden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    var selectedTab by remember { mutableIntStateOf(1) }
    val context = LocalContext.current
    val repository = remember { PlantRepository(context) }
    var plants by remember { mutableStateOf<List<Plant>>(emptyList()) }
    var selectedPlantId by remember { mutableStateOf<String?>(null) }
    var showIntro by remember { mutableStateOf(false) }
    val selectedPlant = plants.firstOrNull { it.id == selectedPlantId }

    LaunchedEffect(Unit) {
        plants = repository.loadPlants()
        if (selectedPlantId == null) selectedPlantId = plants.firstOrNull()?.id
    }

    LaunchedEffect(selectedPlantId, plants) {
        if (selectedPlantId != null && selectedPlantId == plants.firstOrNull()?.id) {
            showIntro = !isIntroSeen(context, selectedPlantId!!)
        }
    }

    fun openPlant(plant: Plant) {
        selectedPlantId = plant.id
        selectedTab = 1
        showIntro = !isIntroSeen(context, plant.id)
    }

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        when (selectedTab) {
            0 -> ExploreScreen(onPlantSelected = ::openPlant)
            1 -> selectedPlant?.let { plant ->
                if (showIntro) {
                    PlantIntroScreen(plant = plant, onComplete = {
                        markIntroSeen(context, plant.id)
                        showIntro = false
                    })
                } else {
                    PlantDetailScreen(plant)
                }
            } ?: Text("Loading…")
            2 -> ScanScreen()
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
