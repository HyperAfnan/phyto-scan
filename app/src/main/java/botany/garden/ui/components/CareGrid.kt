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

@Composable
fun CareGrid(care: PlantCare, modifier: Modifier = Modifier) {
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
                careValue = care.sunlight,
                icon = Icons.Outlined.WbSunny,
                modifier = Modifier.weight(1f),
            )
            CareItem(
                careValue = care.water,
                icon = Icons.Outlined.WaterDrop,
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CareItem(
                careValue = care.soil,
                icon = Icons.Outlined.Terrain,
                modifier = Modifier.weight(1f),
            )
            CareItem(
                careValue = care.temperature,
                icon = Icons.Outlined.AcUnit,
                modifier = Modifier.weight(1f),
            )
        }

        CareItem(
            careValue = care.humidity,
            icon = Icons.Outlined.Opacity,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
