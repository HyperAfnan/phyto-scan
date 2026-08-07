package botany.garden.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Line
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.SubText
import botany.garden.data.model.CareValue

@Composable
fun CareItem(
    careValue: CareValue,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, Line),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Moss,
                    modifier = Modifier.height(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = careValue.label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SubText,
                        fontSize = 10.sp,
                    ),
                )
            }
            Spacer(Modifier.height(9.dp))
            careValue.meterFilled?.let { filled ->
                MeterBar(filled = filled)
                Spacer(Modifier.height(6.dp))
            }
            Text(
                text = careValue.value,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Charcoal,
                    fontSize = 12.sp,
                ),
            )
        }
    }
}
