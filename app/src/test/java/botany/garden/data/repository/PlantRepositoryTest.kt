package botany.garden.data.repository

import botany.garden.data.model.CareValue
import botany.garden.data.model.Plant
import botany.garden.data.model.PlantBadges
import botany.garden.data.model.PlantCare
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

    private fun plant(id: String, botanicalName: String, commonName: String, family: String) = Plant(
        id = id,
        botanicalName = botanicalName,
        commonNames = listOf(commonName),
        family = family,
        pronunciation = "",
        images = PlantImages(""),
        badges = PlantBadges("", "", "", "", "", "", ""),
        warnings = null,
        care = PlantCare(
            CareValue("", ""),
            CareValue("", ""),
            CareValue("", ""),
            CareValue("", ""),
            CareValue("", ""),
        ),
        about = "",
        uses = emptyList(),
        funFacts = emptyList(),
    )

    private val aloe = plant("aloe-vera", "Aloe vera (L.) Burm.f.", "Indian Aloe Vera", "Asphodelaceae")

    @Test
    fun matchesCommonNameAndRejectsDigits() {
        assertEquals(neem, findBestPlantMatch(listOf(neem), "NEEM"))
        assertNull(findBestPlantMatch(listOf(neem), "123456"))
    }

    @Test
    fun matchesNameInsideMultilineOcrWithBotanicalTypo() {
        assertEquals(
            musli,
            findBestPlantMatch(
                listOf(musli),
                "Chlorophylum brivillianum\n(ASPARAGACAE)\nSafed Musli",
            ),
        )
    }

    @Test
    fun matchesAloeVeraCorrectly() {
        assertEquals(aloe, findBestPlantMatch(listOf(aloe), "Aloe Vera"))
        assertEquals(aloe, findBestPlantMatch(listOf(aloe), "Indian Aloe Vera"))
    }

    @Test
    fun ignoresShortOcrFragments() {
        assertNull(findBestPlantMatch(listOf(neem), "ee"))
        assertNull(findBestPlantMatch(listOf(neem), "a"))
        assertNull(findBestPlantMatch(listOf(aloe, neem), "haloes"))
        assertNull(findBestPlantMatch(listOf(aloe, neem), "xyzabc"))
    }
}
