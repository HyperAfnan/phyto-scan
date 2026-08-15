package botany.garden.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import botany.garden.data.model.PlantImages
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.Paper
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: PlantImages,
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit = {},
) {
    val displayImages = remember(images) {
        val list = mutableListOf<String>()
        if (images.hero.isNotBlank()) {
            list.add(images.hero)
        }
        images.gallery.filter { it.isNotBlank() && it != images.hero }.forEach {
            list.add(it)
        }
        list
    }

    if (displayImages.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { displayImages.size })

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(end = 48.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val imagePath = displayImages[page]
            AsyncImage(
                model = "file:///android_asset/$imagePath",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onImageClick(imagePath) },
            )
        }

        if (displayImages.size > 1) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(displayImages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Moss else Paper
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp),
                    )
                }
            }
        }
    }
}
