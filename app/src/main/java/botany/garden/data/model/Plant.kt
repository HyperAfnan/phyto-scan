package botany.garden.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: String,
    val botanicalName: String,
    val commonNames: List<String>,
    val family: String,
)
