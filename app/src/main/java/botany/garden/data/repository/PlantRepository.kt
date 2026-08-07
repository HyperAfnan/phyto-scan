package botany.garden.data.repository

import android.content.Context
import botany.garden.data.model.Plant
import kotlinx.serialization.json.Json
import kotlin.math.max

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

    fun findBestMatch(text: String): Plant? = findBestPlantMatch(loadPlants(), text)

    companion object {
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(context: Context): PlantRepository {
            return instance ?: synchronized(this) {
                instance ?: PlantRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}

fun findBestPlantMatch(plants: List<Plant>, text: String): Plant? {
    val inputs = ocrCandidates(text)
    if (inputs.isEmpty()) return null

    return plants.asSequence()
        .flatMap { plant -> (plant.commonNames + plant.botanicalName).asSequence().map { name -> plant to name } }
        .map { (plant, name) ->
            plant to inputs.maxOf { input -> similarity(input, normalizeOcr(name)) }
        }
        .maxByOrNull { it.second }
        ?.takeIf { it.second >= 0.70 }
        ?.first
}

private fun ocrCandidates(value: String): List<String> {
    val normalized = normalizeOcr(value)
    if (normalized.isBlank()) return emptyList()

    val words = normalized.split(' ')
    return buildList {
        if (normalized.length >= 4) add(normalized)
        for (size in 1..words.size) {
            for (start in 0..words.size - size) {
                val candidate = words.subList(start, start + size).joinToString(" ")
                if (candidate.length >= 4) add(candidate)
            }
        }
    }
}

private fun normalizeOcr(value: String) = value.lowercase()
    .replace(Regex("[^a-z ]"), " ")
    .replace(Regex("\\s+"), " ")
    .trim()

private fun similarity(a: String, b: String): Double {
    if (a == b) return 1.0
    val minLen = minOf(a.length, b.length)
    if (minLen >= 4 && (a.contains(b) || b.contains(a))) {
        if (minLen >= 5 || isWordMatch(a, b)) return 0.9
    }
    val distance = levenshtein(a, b)
    return 1.0 - distance.toDouble() / max(a.length, b.length).coerceAtLeast(1)
}

private fun isWordMatch(a: String, b: String): Boolean {
    val (shorter, longer) = if (a.length < b.length) a to b else b to a
    val regex = Regex("\\b${Regex.escape(shorter)}\\b")
    return regex.containsMatchIn(longer)
}

private fun levenshtein(a: String, b: String): Int {
    var previous = IntArray(b.length + 1) { it }
    for (i in a.indices) {
        val current = IntArray(b.length + 1)
        current[0] = i + 1
        for (j in b.indices) current[j + 1] = minOf(
            current[j] + 1,
            previous[j + 1] + 1,
            previous[j] + if (a[i] == b[j]) 0 else 1,
        )
        previous = current
    }
    return previous[b.length]
}
