package presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.Movie
import events.HomePageEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.ADULT_LABEL
import presentation.BannerCarouselWidget
import presentation.CustomImageView
import presentation.LikeButtonComponent
import presentation.UA_LABEL
import presentation.details.DetailsScreen
import ui.network.ApiServiceClient
import ui.network.IMAGE_BASE_URL

class Home(apiServiceClient: ApiServiceClient) : Screen {

    private val viewModel: HomeViewModel = HomeViewModel(apiServiceClient)

    @Composable
    override fun Content() {

        val coroutineScope = rememberCoroutineScope()

        val navigator = LocalNavigator.currentOrThrow

        val gridState = rememberLazyGridState()


        LaunchedEffect(Unit) {
            while (viewModel.page < viewModel.MAX_PAGE_LIMIT) {
                delay(5000)
                viewModel.loadMoreData()
            }
        }


        LaunchedEffect(Unit) {
            println("LaunchedEffect called")
            coroutineScope.launch(Dispatchers.IO) {
                println("inside coroutineScope")
                withContext(Dispatchers.Main) {
                    viewModel.showLoader.value = true
                }
                viewModel.init()
            }
        }

        if (!viewModel.showLoader.value) {

            Scaffold(
                modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets(top = 0.dp))
            ) { it ->
                LazyVerticalGrid(
                    modifier = Modifier.padding(it),
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {

                    item(span = { GridItemSpan(2) }) {
                        BannerCarouselWidget(
                            banners = viewModel.bannerList.value,
                            modifier = Modifier.height(600.dp),
                            likedMoviesMap = viewModel.likedListMap.value,
                            onLikeButtonClick = { id, isLiked ->
                                viewModel.onEvent(HomePageEvents.OnLikeButtonClicked(id, isLiked))
                            },
                            onBannerCarouselItemClick = { id: Int ->
                                navigator.push(DetailsScreen(viewModel.bannerList.value.find { movie -> movie.id == id }, viewModel.isLiked(id)))
                            }
                        )
                    }

                    itemsIndexed(viewModel.contentList.value) { index, item ->
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            PosterCard(item,
                                isLiked = viewModel.isLiked(item.id ?: 0),
                                onLikeButtonClick = { id, isLiked ->
                                    viewModel.onEvent(HomePageEvents.OnLikeButtonClicked(id, isLiked))
                                },
                                onPosterCardClick = { id ->
                                    navigator.push(DetailsScreen(viewModel.contentList.value.find { movie -> movie.id == id }, viewModel.isLiked(id)))
                                }
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                    }

                }


            }

        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }


}

@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
private fun PosterCard(
    item: Movie,
    isLiked: Boolean,
    onLikeButtonClick: (Int, Boolean) -> Unit,
    onPosterCardClick: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable {
                onPosterCardClick(item.id ?: 0)
            },
        shape = RoundedCornerShape(6.dp),
        elevation = 10.dp
    ) {
        Column {
            Box(modifier = Modifier) {
                CustomImageView(
                    modifier = Modifier
                        .height(300.dp)
                        .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)),
                    imageUrl = IMAGE_BASE_URL + item.poster_path
                )

                LikeButtonComponent(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    alignment = Alignment.TopEnd,
                    isLiked = isLiked
                ) {
                    onLikeButtonClick(item.id ?: 0, it)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = (item.title ?: ""),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(if (item.adult == true) ADULT_LABEL else UA_LABEL)
                            append(" | ")
                            append(item.original_language ?: "")
                        },
                        modifier = Modifier,
                        style = TextStyle(color = Color.Black, fontSize = 14.sp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}