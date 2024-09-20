package presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.landscapist.ImageOptions
import domain.Movie
import events.HomePageEvents
import presentation.ADULT_LABEL
import presentation.BannerWidget
import presentation.CustomImageView
import presentation.LikeButtonComponent
import presentation.UA_LABEL
import presentation.home.HomeViewModel
import ui.network.IMAGE_BASE_URL

class DetailsScreen(val movieDetails: Movie?, val isLiked: Boolean): Screen {

    val viewModel: DetailsScreenViewModel = DetailsScreenViewModel()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.onEvent(HomePageEvents.OnLikeButtonClicked(movieDetails?.id ?: 0, isLiked))
        }

        Scaffold(modifier = Modifier
            .fillMaxSize()
        ) {

            movieDetails?.let { movie ->
                LazyColumn(modifier = Modifier.padding(it), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    item {

                        Box(modifier = Modifier.fillMaxWidth()
                            .height(655.dp)
                            .background(Color.Transparent)
                        ) {

                            Box(modifier = Modifier.fillMaxWidth()
                                .height(600.dp)) {
                                BannerWidget(
                                    imageUrl = IMAGE_BASE_URL + movie.backdrop_path,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    imageOptions = ImageOptions(
                                        contentScale = ContentScale.Crop,
                                        alignment = Alignment.Center,
                                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                            setToSaturation(0f)
                                        })
                                    )
                                )
                            }

                            Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)) {

                                IconButton(
                                    modifier = Modifier.align(Alignment.TopStart).padding(vertical = 16.dp, horizontal = 12.dp),
                                    onClick = {
                                        navigator.pop()
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.ArrowBack, "Back button", tint = Color.Black)
                                }

                            }


                            LikeButtonComponent(
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 12.dp),
                                alignment = Alignment.TopEnd,
                                isLiked = isLiked,
                            ) {
                                viewModel.onEvent(HomePageEvents.OnLikeButtonClicked(movie.id ?: 0, isLiked))
                            }

                            Box(
                                modifier = Modifier.align(Alignment.BottomCenter).padding(top = 50.dp)
                            ) {
                                CustomImageView(
                                    imageUrl = IMAGE_BASE_URL + movie.poster_path,
                                    modifier = Modifier
                                        .height(300.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    imageOptions = ImageOptions(
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center
                                    )
                                )
                            }

                        }

                    }

                    item {
                        Text(
                            text = (movie.title ?: ""),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).basicMarquee(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(color = Color.Black, fontSize = 24.sp),
                            maxLines = 1
                        )
                    }

                    item {
                        Text(
                            text = (movie.overview ?: ""),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .background(color = Color.LightGray, shape = RoundedCornerShape(6.dp))
                                .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append(if (movie.adult == true) ADULT_LABEL else UA_LABEL)
                                    append(" | ")
                                    append(movie.original_language ?: "")
                                },
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                                textAlign = TextAlign.Start,
                                style = TextStyle(color = Color.Black, fontSize = 16.sp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                    append("Release Date: ")
                                }
                                append(movie.release_date ?: "")
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        )
                    }

                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                    append("Rating: ")
                                }
                                append(movie.vote_average.toString() ?: "")
                                append("(${movie.vote_count})")
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        )
                    }

                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                    append("Popularity: ")
                                }
                                append(movie.popularity.toString() ?: "")
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        )
                    }

                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                    append("Media Type: ")
                                }
                                append(if ((movie.media_type ?: "") == "movie") "Movie" else "TV Series")
                            },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(color = Color.Black, fontSize = 16.sp),
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                    }
                }
            }

        }


    }


}