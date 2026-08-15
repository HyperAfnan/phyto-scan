package botany.garden.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: String,
    val botanicalName: String,
    val commonNames: List<String>,
    val family: String,
    val pronunciation: String = "",
    val images: PlantImages,
    val about: String = "",
    val synonyms: List<String> = emptyList(),
    val commonNameTable: List<CommonNameEntry> = emptyList(),
    val spotImages: List<SpotImage> = emptyList(),
    val habitat: String = "",
    val medicinalUses: List<MedicinalUseData> = emptyList(),
    val pharmacologicalActivities: List<String> = emptyList(),
    val phytochemicals: List<String> = emptyList(),
    val scientificEvidence: String = "",
    val safetyPrecautions: List<String> = emptyList(),
    val preparationSteps: List<String> = emptyList(),
    val cultivationInfo: List<CultivationInfoData> = emptyList(),
    val medicinalParts: List<MedicinalPartData> = emptyList(),
    val traditionalUses: List<String> = emptyList(),
    val ayurvedicProperties: AyurvedicPropertiesData? = null,
    val interestingFacts: List<InterestingFactData> = emptyList(),
    val doYouKnow: List<DoYouKnowData> = emptyList(),
    val references: List<String> = emptyList(),
)

@Serializable
data class PlantImages(
    val hero: String,
    val gallery: List<String> = emptyList(),
)

@Serializable
data class CommonNameEntry(
    val language: String,
    val name: String,
)

@Serializable
data class SpotImage(
    val imageUrl: String = "",
    val caption: String,
)

@Serializable
data class MedicinalUseData(
    val title: String,
    val icon: String = "",
    val points: List<String>,
)

@Serializable
data class CultivationInfoData(
    val label: String,
    val imageUrl: String = "",
)

@Serializable
data class MedicinalPartData(
    val name: String,
    val imageUrl: String = "",
)

@Serializable
data class AyurvedicPropertiesData(
    val rasa: List<String> = emptyList(),
    val guna: List<String> = emptyList(),
    val virya: List<String> = emptyList(),
    val doshaAction: String = "",
)

@Serializable
data class InterestingFactData(
    val title: String,
    val imageUrl: String = "",
    val description: String = "",
)

@Serializable
data class DoYouKnowData(
    val title: String,
    val imageUrl: String = "",
    val description: String = "",
)
