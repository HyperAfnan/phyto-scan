package botany.garden.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.SubText

@Composable
fun MedicinalUseCard(
    title: String,
    points: List<String>,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.LocalHospital,
) {
    if (points.isEmpty()) return

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Moss,
                modifier = Modifier
                    .size(28.dp)
                    .padding(top = 2.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                points.forEach { point ->
                    Row(modifier = Modifier.padding(bottom = 4.dp)) {
                        Text(
                            text = "– ",
                            style = MaterialTheme.typography.bodySmall,
                            color = SubText,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodySmall,
                            color = Charcoal,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
