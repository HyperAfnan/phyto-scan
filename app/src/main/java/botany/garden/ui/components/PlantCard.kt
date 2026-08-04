package botany.garden.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.theme.CardBg
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Line
import botany.garden.ui.theme.Moss

@Composable
fun PlantCard(plant: Plant, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, Line),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = plant.commonNames.first(),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Ink,
            )
            Text(
                text = plant.botanicalName,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = Charcoal,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Fam. ${plant.family}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                letterSpacing = (0.06 * 10).sp,
                color = Moss,
            )
        }
    }
}
