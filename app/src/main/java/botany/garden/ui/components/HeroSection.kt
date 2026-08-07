package botany.garden.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import botany.garden.data.model.Plant
import botany.garden.ui.theme.Ink
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HeroSection(plant: Plant, modifier: Modifier = Modifier) {
    HeroSection(
        commonName = plant.commonNames.firstOrNull() ?: "",
        scientificName = plant.botanicalName,
        familyTag = "Fam. ${plant.family} · ${plant.id}",
        pronunciation = plant.pronunciation,
        imageUrl = plant.images.hero,
        modifier = modifier,
    )
}

@Composable
fun HeroSection(
    commonName: String,
    scientificName: String,
    familyTag: String,
    pronunciation: String,
    imageUrl: String = "",
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(322.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF5E3E8),
                        Color(0xFFE9F0DF),
                        Color(0xFFCFDCC4),
                    ),
                ),
            ),
    ) {
        if (imageUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data("file:///android_asset/$imageUrl").crossfade(true).build(),
                contentDescription = commonName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.45f to Color.Transparent,
                        1.0f to Ink.copy(alpha = 0.88f),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 18.dp),
        ) {
            Text(
                text = familyTag,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.5.sp,
                letterSpacing = (0.09 * 10.5).sp,
                color = Color.White,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = commonName,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                lineHeight = 34.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = scientificName,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.95f),
                )
                Spacer(Modifier.width(10.dp))
                SayButton(label = pronunciation)
            }
        }
    }
}
