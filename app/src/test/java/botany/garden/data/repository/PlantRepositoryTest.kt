package botany.garden.data.repository

import botany.garden.data.model.Plant
import botany.garden.data.model.PlantImages
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlantRepositoryTest {
    private val neem = plant("azadirachta-indica", "Azadirachta indica A. Juss.", "Neem", "Meliaceae")
    private val musli = plant(
        "chlorophytum-borivilianum",
        "Chlorophytum borivilianum Santapau & R.R. Fern.",
        "Safed Musli",
        "Asparagaceae",
    )
    private val aloe = plant("aloe-vera", "Aloe vera (L.) Burm.f.", "Indian Aloe Vera", "Asphodelaceae")

    private fun plant(id: String, botanicalName: String, commonName: String, family: String) = Plant(
        id = id,
        botanicalName = botanicalName,
        commonNames = listOf(commonName),
        family = family,
        pronunciation = "",
        images = PlantImages(""),
    )

    @Test
    fun matchesCommonName() {
        val plants = listOf(neem, musli, aloe)
        assertEquals(neem, findPlantByQrCode(plants, "NEEM"))
        assertEquals(musli, findPlantByQrCode(plants, "Safed Musli"))
        assertEquals(aloe, findPlantByQrCode(plants, "Indian Aloe Vera"))
    }

    @Test
    fun matchesBiologicalBotanicalName() {
        val plants = listOf(neem, musli, aloe)
        assertEquals(neem, findPlantByQrCode(plants, "Azadirachta indica"))
        assertEquals(musli, findPlantByQrCode(plants, "Chlorophytum borivilianum"))
        assertEquals(aloe, findPlantByQrCode(plants, "Aloe vera"))
    }

    @Test
    fun matchesPlantIdDirectly() {
        val plants = listOf(neem, musli, aloe)
        assertEquals(neem, findPlantByQrCode(plants, "azadirachta-indica"))
        assertEquals(aloe, findPlantByQrCode(plants, "aloe-vera"))
    }

    @Test
    fun matchesUrlAndDeepLinkQrPayloads() {
        val plants = listOf(neem, musli, aloe)
        assertEquals(aloe, findPlantByQrCode(plants, "https://botanygarden.app/plant?id=aloe-vera"))
        assertEquals(musli, findPlantByQrCode(plants, "botanygarden://plant/chlorophytum-borivilianum"))
    }

    @Test
    fun matchesJsonQrPayload() {
        val plants = listOf(neem, musli, aloe)
        assertEquals(neem, findPlantByQrCode(plants, "{\"id\":\"azadirachta-indica\"}"))
        assertEquals(aloe, findPlantByQrCode(plants, "{\"name\":\"Indian Aloe Vera\"}"))
    }

    @Test
    fun rejectsInvalidOrUnknownQrStrings() {
        val plants = listOf(neem, musli, aloe)
        assertNull(findPlantByQrCode(plants, "123456"))
        assertNull(findPlantByQrCode(plants, "xyzabc"))
        assertNull(findPlantByQrCode(plants, "a"))
        assertNull(findPlantByQrCode(plants, ""))
    }
}
