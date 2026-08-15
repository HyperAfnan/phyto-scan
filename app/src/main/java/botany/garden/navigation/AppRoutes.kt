package botany.garden.navigation

/**
 * Navigation route definitions for the Botany Garden application.
 */
object AppRoutes {
    const val EXPLORE = "explore"
    const val GARDEN = "garden"
    const val SCAN = "scan"
    const val PLANT_DETAIL = "plant_detail/{plantId}"

    fun plantDetail(plantId: String) = "plant_detail/$plantId"
}
