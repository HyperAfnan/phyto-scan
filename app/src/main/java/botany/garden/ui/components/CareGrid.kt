package botany.garden.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import botany.garden.data.model.PlantCare

data class CareData(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val meterFilled: Int? = null,
)

@Composable
fun CareGrid(care: PlantCare, modifier: Modifier = Modifier) {
    val items = listOf(
        CareData(Icons.Outlined.WbSunny, care.sunlight.label, care.sunlight.value, care.sunlight.meterFilled),
        CareData(Icons.Outlined.WaterDrop, care.water.label, care.water.value, care.water.meterFilled),
        CareData(Icons.Outlined.Terrain, care.soil.label, care.soil.value, care.soil.meterFilled),
        CareData(Icons.Outlined.AcUnit, care.temperature.label, care.temperature.value, care.temperature.meterFilled),
        CareData(Icons.Outlined.Opacity, care.humidity.label, care.humidity.value, care.humidity.meterFilled),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CareItem(
                icon = items[0].icon,
                label = items[0].label,
                value = items[0].value,
                meterFilled = items[0].meterFilled,
                modifier = Modifier.weight(1f),
            )
            CareItem(
                icon = items[1].icon,
                label = items[1].label,
                value = items[1].value,
                meterFilled = items[1].meterFilled,
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CareItem(
                icon = items[2].icon,
                label = items[2].label,
                value = items[2].value,
                meterFilled = items[2].meterFilled,
                modifier = Modifier.weight(1f),
            )
            CareItem(
                icon = items[3].icon,
                label = items[3].label,
                value = items[3].value,
                meterFilled = items[3].meterFilled,
                modifier = Modifier.weight(1f),
            )
        }

        CareItem(
            icon = items[4].icon,
            label = items[4].label,
            value = items[4].value,
            meterFilled = items[4].meterFilled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
