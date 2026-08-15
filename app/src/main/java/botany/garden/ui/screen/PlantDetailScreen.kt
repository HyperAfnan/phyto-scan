package botany.garden.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import botany.garden.data.model.Plant
import botany.garden.ui.components.BotanicalInfoCards
import botany.garden.ui.components.FullScreenImageViewer
import botany.garden.ui.components.ImageCarousel
import botany.garden.ui.components.PlantDetailTopBar
import botany.garden.ui.screen.tabs.MedicinalScienceTabContent
import botany.garden.ui.screen.tabs.MoreInfoTabContent
import botany.garden.ui.screen.tabs.OverviewTabContent
import botany.garden.ui.screen.tabs.PracticalSafetyTabContent
import botany.garden.ui.theme.Moss
import botany.garden.ui.theme.Paper
import botany.garden.ui.theme.SubText
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantDetailScreen(plant: Plant, onBack: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    val pagerState = rememberPagerState(pageCount = { 4 })
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(plant.id) {
        visible = true
    }

    // Reset scroll position when swiping to a new tab
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            if (listState.firstVisibleItemIndex >= 2) {
                listState.animateScrollToItem(index = 2)
            }
        }
    }

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 100
        }
    }

    val heroAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(stiffness = 260f, dampingRatio = 0.92f),
        label = "heroAlpha",
    )
    val heroOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 24f,
        animationSpec = spring(stiffness = 260f, dampingRatio = 0.92f),
        label = "heroOffset",
    )

    val tabTitles = listOf("Overview", "Medicinal & Science", "Practical & Safety", "More Info")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper),
    ) {
        // Fixed top bar — TTS speaks common name
        PlantDetailTopBar(
            plantName = plant.commonNames.firstOrNull() ?: plant.botanicalName,
            speakText = plant.commonNames.firstOrNull() ?: plant.botanicalName,
            onBack = onBack,
        )

        Box(modifier = Modifier.fillMaxSize()) {
            // Scrollable content with sticky tab row
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = heroOffset.dp)
                    .alpha(heroAlpha),
            ) {
                // Image carousel
                item(key = "carousel") {
                    ImageCarousel(
                        images = plant.images,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onImageClick = { selectedImageUrl = it },
                    )
                }

                // Botanical info cards (Scientific Name + Family) — clickable with TTS
                item(key = "botanical_cards") {
                    BotanicalInfoCards(
                        botanicalName = plant.botanicalName,
                        family = plant.family,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }

                // Sticky tab row
                stickyHeader(key = "tab_row") {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Paper,
                        contentColor = Moss,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (pagerState.currentPage < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    color = Moss,
                                )
                            }
                        },
                        divider = {},
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (pagerState.currentPage == index) Moss else SubText,
                                        maxLines = 1,
                                    )
                                },
                            )
                        }
                    }
                }

                // Tab content with swipe gesture
                item(key = "tab_pager") {
                    Spacer(Modifier.height(12.dp))
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                    ) { page ->
                        when (page) {
                            0 -> OverviewTabContent(
                                plant = plant,
                                onImageClick = { selectedImageUrl = it },
                            )
                            1 -> MedicinalScienceTabContent(
                                plant = plant,
                            )
                            2 -> PracticalSafetyTabContent(
                                plant = plant,
                                onImageClick = { selectedImageUrl = it },
                            )
                            3 -> MoreInfoTabContent(
                                plant = plant,
                                onImageClick = { selectedImageUrl = it },
                            )
                        }
                    }
                }
            }

            // Floating "Scroll to top" button
            this@Column.AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 108.dp, end = 20.dp),
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    shape = CircleShape,
                    containerColor = Moss,
                    contentColor = Paper,
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                    )
                }
            }
        }
    }

    // Full-screen image viewer overlay
    if (!selectedImageUrl.isNullOrBlank()) {
        FullScreenImageViewer(
            imageUrl = selectedImageUrl!!,
            onDismiss = { selectedImageUrl = null },
        )
    }
}
