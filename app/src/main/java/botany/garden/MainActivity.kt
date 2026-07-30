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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import botany.garden.ui.components.BottomNavBar
import botany.garden.ui.screen.ExploreScreen
import botany.garden.ui.screen.PlantDetailScreen
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

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        when (selectedTab) {
            0 -> ExploreScreen()
            1 -> PlantDetailScreen()
            2 -> ScanScreen()
        }

        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(horizontal = 14.dp, vertical = 14.dp),
        )
    }
}
