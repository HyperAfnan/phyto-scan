package botany.garden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import botany.garden.ui.screen.PlantDetailScreen
import botany.garden.ui.theme.BotanyGardenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotanyGardenTheme {
                PlantDetailScreen()
            }
        }
    }
}
