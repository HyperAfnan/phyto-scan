package botany.garden.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: String,
    val botanicalName: String,
    val commonNames: List<String>,
    val family: String,
    val pronunciation: String,
    val images: PlantImages,
    val badges: PlantBadges,
    val warnings: PlantWarnings?,
    val care: PlantCare,
    val about: String,
    val uses: List<PlantUse>,
    val funFacts: List<PlantFact>,
)

@Serializable
data class PlantImages(val hero: String, val leaf: String, val flower: String, val fruit: String)

@Serializable
data class PlantBadges(
    val conservation: String,
    val nativeRegion: String,
    val flowering: String,
    val lifespan: String,
    val plantType: String,
    val matureHeight: String,
    val growthRate: String,
)

@Serializable
data class PlantWarnings(val headline: String, val chips: List<String>)

@Serializable
data class PlantCare(
    val sunlight: CareValue,
    val water: CareValue,
    val soil: CareValue,
    val temperature: CareValue,
    val humidity: CareValue,
)

@Serializable
data class CareValue(
    val label: String,
    val value: String,
    val meterFilled: Int? = null,
    val fullWidth: Boolean = false,
)

@Serializable
data class PlantUse(val label: String, val description: String)

@Serializable
data class PlantFact(val question: String, val answer: String)
