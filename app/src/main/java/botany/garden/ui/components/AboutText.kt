package botany.garden.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.ui.theme.Charcoal
import botany.garden.ui.theme.MossDark

@Composable
fun AboutText(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Charcoal, fontSize = 14.5.sp)) {
                append("An ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = MossDark, fontSize = 14.5.sp)) {
                append("evergreen shrub")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Charcoal, fontSize = 14.5.sp)) {
                append(" with slender, lance-shaped leaves arranged in whorls of three, oleander produces clusters of five-petalled flowers in white, pink, or deep rose through the warm months. In the wild it lines dry riverbeds and rocky coastal ground across the Mediterranean basin, where its deep roots and waxy leaves help it withstand long stretches without rain. Nearly every tissue in the plant carries potent ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = MossDark, fontSize = 14.5.sp)) {
                append("cardiac glycosides")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = Charcoal, fontSize = 14.5.sp)) {
                append(", a defence so effective that livestock and most insects leave it untouched \u2014 one reason it survives so readily along roadsides and neglected slopes.")
            }
        },
        modifier = modifier,
        lineHeight = 24.sp,
    )
}
