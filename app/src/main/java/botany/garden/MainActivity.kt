package botany.garden

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import botany.garden.data.model.Plant
import botany.garden.data.repository.PlantRepository
import botany.garden.navigation.AppRoutes
import botany.garden.ui.components.BottomNavBar
import botany.garden.ui.screen.ExploreScreen
import botany.garden.ui.screen.NoPlantSelectedScreen
import botany.garden.ui.screen.PlantDetailScreen
import botany.garden.ui.screen.ScanScreen
import botany.garden.ui.theme.BotanyGardenTheme
import botany.garden.ui.theme.Paper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
        )
        setContent {
            BotanyGardenTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = remember(context) { PlantRepository.getInstance(context) }
    var plants by remember { mutableStateOf<List<Plant>>(emptyList()) }
    var gardenPlantId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(repository) {
        plants = repository.loadPlants()
    }

    fun openPlant(plant: Plant) {
        gardenPlantId = plant.id
        navController.navigate(AppRoutes.plantDetail(plant.id))
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = when {
        currentRoute == AppRoutes.EXPLORE -> 0
        currentRoute == AppRoutes.GARDEN ||
            currentRoute?.startsWith("plant_detail") == true -> 1
        else -> 2 // AppRoutes.SCAN
    }

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        NavHost(
            navController = navController,
            startDestination = AppRoutes.SCAN,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { width -> width / 3 }, animationSpec = tween(300)) + fadeIn(tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { width -> -width / 3 }, animationSpec = tween(200)) + fadeOut(tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { width -> -width / 3 }, animationSpec = tween(300)) + fadeIn(tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { width -> width / 3 }, animationSpec = tween(200)) + fadeOut(tween(200))
            },
        ) {
            composable(AppRoutes.SCAN) {
                ScanScreen(repository = repository, onPlantFound = ::openPlant)
            }
            composable(AppRoutes.EXPLORE) {
                ExploreScreen(repository = repository, onPlantSelected = ::openPlant)
            }
            composable(AppRoutes.GARDEN) {
                val gardenPlant = plants.firstOrNull { it.id == gardenPlantId }
                if (gardenPlant != null) {
                    PlantDetailScreen(
                        plant = gardenPlant,
                        onBack = { gardenPlantId = null },
                    )
                } else {
                    NoPlantSelectedScreen(
                        onExploreClick = {
                            navController.navigate(AppRoutes.EXPLORE) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                            }
                        },
                        onScanClick = {
                            navController.navigate(AppRoutes.SCAN) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                            }
                        },
                    )
                }
            }
            composable(
                route = AppRoutes.PLANT_DETAIL,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId")
                val plant = plants.firstOrNull { it.id == plantId }
                if (plant != null) {
                    PlantDetailScreen(
                        plant = plant,
                        onBack = { navController.popBackStack() },
                    )
                }
            }
        }

        BottomNavBar(
            selectedIndex = selectedIndex,
            onTabSelected = { index ->
                val targetRoute = when (index) {
                    0 -> AppRoutes.EXPLORE
                    1 -> AppRoutes.GARDEN
                    else -> AppRoutes.SCAN
                }
                navController.navigate(targetRoute) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 14.dp, vertical = 14.dp),
        )
    }
}
