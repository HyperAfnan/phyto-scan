package botany.garden.data.repository

import botany.garden.data.model.Plant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlantRepositoryTest {
    private val neem = Plant("azadirachta-indica", "Azadirachta indica A. Juss.", listOf("Neem"), "Meliaceae")
    private val musli = Plant(
        "chlorophytum-borivilianum",
        "Chlorophytum borivilianum Santapau & R.R. Fern.",
        listOf("Safed Musli"),
        "Asparagaceae",
    )

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
    fun ignoresShortOcrFragments() {
        assertNull(findBestPlantMatch(listOf(neem), "ee"))
        assertNull(findBestPlantMatch(listOf(neem), "a"))
    }
}
