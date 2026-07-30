package botany.garden.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.data.repository.PlantRepository
import botany.garden.ui.components.PlantCard
import botany.garden.ui.components.SearchBar
import botany.garden.ui.theme.Paper
import botany.garden.ui.theme.SubText

@Composable
fun ExploreScreen() {
    val context = LocalContext.current
    val repository = remember { PlantRepository(context) }

    var allPlants by remember { mutableStateOf<List<Plant>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        allPlants = repository.loadPlants()
    }

    val filteredPlants = remember(searchQuery, allPlants) {
        if (searchQuery.isBlank()) allPlants
        else allPlants.filter { plant ->
            plant.commonNames.any { it.contains(searchQuery, ignoreCase = true) }
                || plant.botanicalName.contains(searchQuery, ignoreCase = true)
                || plant.family.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Paper)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(60.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
            )

            Spacer(Modifier.height(16.dp))

            if (filteredPlants.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (allPlants.isEmpty()) "Loading..." else "No plants match your search",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = SubText,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 100.dp,
                    ),
                ) {
                    items(filteredPlants, key = { it.id }) { plant ->
                        PlantCard(plant = plant)
                    }
                }
            }
        }
    }
}
