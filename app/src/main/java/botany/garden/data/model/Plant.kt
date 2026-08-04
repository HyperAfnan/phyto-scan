package botany.garden.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: String,
    val botanicalName: String,
    val commonNames: List<String>,
    val family: String,
    val morphology: Morphology? = null,
    val microscopy: List<String> = emptyList(),
    val transverseSection: TransverseSection? = null,
    val chemicalConstituents: List<String> = emptyList(),
    val pharmacologicalActivities: List<String> = emptyList(),
    val traditionalUses: List<String> = emptyList(),
    val quickFacts: QuickFacts? = null,
)

@Serializable
data class Morphology(val height: String = "", val description: List<String> = emptyList())

@Serializable
data class TransverseSection(val organ: String = "", val description: List<String> = emptyList())

@Serializable
data class QuickFacts(
    val medicinalParts: List<String> = emptyList(),
    val majorActiveCompounds: List<String> = emptyList(),
    val specialFeature: String = "",
)
