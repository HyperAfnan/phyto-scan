package botany.garden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Caution
import botany.garden.ui.theme.CautionPale
import botany.garden.ui.theme.WarnBorder
import botany.garden.ui.theme.WarnText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WarningCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(CautionPale, Color(0xFFF8EEDF)),
                ),
            )
            .border(1.dp, WarnBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = Caution,
                modifier = Modifier.height(22.dp),
            )
            Spacer(Modifier.width(9.dp))
            Text(
                text = "Every part of this plant is toxic",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.5.sp,
                color = WarnText,
            )
        }
        Spacer(Modifier.height(10.dp))
        FlowRow(
            modifier = Modifier.padding(start = 6.dp),
        ) {
            WarningChip(icon = Icons.Outlined.Lock, label = "Toxic to humans")
            Spacer(Modifier.width(8.dp))
            WarningChip(icon = Icons.Outlined.Pets, label = "Toxic to pets")
            Spacer(Modifier.width(8.dp))
            WarningChip(icon = Icons.Outlined.WaterDrop, label = "Allergenic sap")
            Spacer(Modifier.width(8.dp))
            WarningChip(icon = Icons.Outlined.Close, label = "Do not consume")
        }
    }
}
