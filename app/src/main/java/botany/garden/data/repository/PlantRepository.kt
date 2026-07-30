package botany.garden.data.repository

import android.content.Context
import botany.garden.data.model.Plant
import kotlinx.serialization.json.Json

class PlantRepository(private val context: Context) {
    private var cached: List<Plant>? = null
    private val json = Json { ignoreUnknownKeys = true }

    fun loadPlants(): List<Plant> {
        if (cached != null) return cached!!
        val text = context.assets.open("plants.json").bufferedReader().readText()
        val decoded = json.decodeFromString<List<Plant>>(text)
        cached = decoded
        return decoded
    }
}
