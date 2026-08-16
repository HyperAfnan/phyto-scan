package botany.garden.data.repository

import android.content.Context
import botany.garden.data.model.Plant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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

    fun findBestMatch(text: String): Plant? = findPlantByQrCode(loadPlants(), text)

    fun findPlantByQrCode(qrText: String): Plant? = findPlantByQrCode(loadPlants(), qrText)

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

fun findPlantByQrCode(plants: List<Plant>, qrText: String): Plant? {
    val raw = extractRawPayload(qrText).trim()
    if (raw.isBlank() || raw.length < 2) return null

    val targetNorm = normalizeQr(raw)
    if (targetNorm.isBlank()) return null

    // 1. Direct ID match (raw or normalized)
    plants.firstOrNull { plant ->
        plant.id.equals(raw, ignoreCase = true) || normalizeQr(plant.id) == targetNorm
    }?.let { return it }

    // 2. Botanical name / Common name exact or normalized match
    plants.firstOrNull { plant ->
        val names = (listOf(plant.botanicalName) + plant.commonNames + plant.synonyms)
        names.any { name ->
            name.equals(raw, ignoreCase = true) || normalizeQr(name) == targetNorm
        }
    }?.let { return it }

    // 3. Substring / Prefix match for binomial botanical names & common names
    return plants.firstOrNull { plant ->
        val normBotanical = normalizeQr(plant.botanicalName)
        val normCommons = plant.commonNames.map(::normalizeQr)

        (targetNorm.length >= 4 && normBotanical.startsWith(targetNorm)) ||
            (normBotanical.length >= 4 && targetNorm.startsWith(normBotanical)) ||
            normCommons.any { common ->
                (targetNorm.length >= 4 && common.startsWith(targetNorm)) ||
                    (common.length >= 4 && targetNorm.startsWith(common))
            }
    }
}

private fun extractRawPayload(qrText: String): String {
    val trimmed = qrText.trim()
    if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
        try {
            val jsonObj = Json.parseToJsonElement(trimmed).jsonObject
            val id = jsonObj["id"]?.jsonPrimitive?.content
                ?: jsonObj["plantId"]?.jsonPrimitive?.content
                ?: jsonObj["name"]?.jsonPrimitive?.content
            if (!id.isNullOrBlank()) return id
        } catch (_: Exception) { }
    }

    if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("botanygarden://")) {
        val queryParam = listOf("id", "plantId", "name", "plant").firstNotNullOfOrNull { param ->
            val regex = Regex("[?&]$param=([^&]+)", RegexOption.IGNORE_CASE)
            regex.find(trimmed)?.groupValues?.get(1)
        }
        if (!queryParam.isNullOrBlank()) return queryParam

        val lastSegment = trimmed.substringBefore('?').substringAfterLast('/').trim()
        if (lastSegment.isNotBlank() && !lastSegment.contains('.')) return lastSegment
    }

    return trimmed
}

private fun normalizeQr(value: String): String {
    return value.lowercase()
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
