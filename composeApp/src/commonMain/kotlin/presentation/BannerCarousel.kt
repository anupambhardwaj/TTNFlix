package presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import domain.Movie
import ui.network.IMAGE_BASE_URL


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarouselWidget(
    banners: List<Movie>,
    likedMoviesMap: Map<Int, Boolean> = mapOf<Int, Boolean>(653346 to true),
    modifier: Modifier = Modifier,
    onLikeButtonClick: (Int, Boolean) -> Unit,
    onBannerCarouselItemClick: (id: Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        banners.size
    })

    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Box {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 8.dp,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) { page ->
                Column(
                    modifier = Modifier
                        .clickable {
                            onBannerCarouselItemClick(banners[page].id ?: 0)
                        }
                ) {
                    Box(modifier = Modifier) {
                        BannerWidget(
                            imageUrl = IMAGE_BASE_URL + banners[page].backdrop_path,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black
                                        ),
                                        startY = 400f
                                    )
                                )
                                .align(Alignment.BottomCenter)
                        )
                        LikeButtonComponent(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 12.dp),
                            alignment = Alignment.TopEnd,
                            isLiked = likedMoviesMap[banners[page].id] ?: false
                        ) {
                            onLikeButtonClick(banners[page].id ?: 0, it)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                                .align(Alignment.BottomCenter),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = (banners[page].title ?: ""),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .basicMarquee(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    style = TextStyle(color = Color.White, fontSize = 24.sp)
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        append(if (banners[page].adult == true) ADULT_LABEL else UA_LABEL)
                                        append(" | ")
                                        append(banners[page].original_language ?: "")
                                    },
                                    modifier = Modifier,
                                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                                )
                                Spacer(Modifier.fillMaxWidth().height(16.dp))
                            }
                        }
                    }
                }

            }
            Row(
                Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 8.dp, top = 16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {

                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.White else Color.DarkGray


                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(if (pagerState.currentPage == iteration) RoundedCornerShape(16.dp) else CircleShape )
                            .background(if (pagerState.currentPage == iteration) Color.DarkGray else color)
                            .size(height = 8.dp, width = if (pagerState.currentPage == iteration) 20.dp else 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                                /*.clickable {
                                    scope.launch {
                                        if (pagerState.currentPage != iteration) {
                                            pagerState.animateScrollToPage(iteration)
                                        }
                                    }
                                }*/
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BannerWidget(
    imageUrl: String,
    modifier: Modifier = Modifier,
    imageOptions: ImageOptions = ImageOptions(
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
) {
    CustomImageView(
        imageUrl = imageUrl,
        modifier = modifier
            .fillMaxWidth(),
        imageOptions = imageOptions
    )
}