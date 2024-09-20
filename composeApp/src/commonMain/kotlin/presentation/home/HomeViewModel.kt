package presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import domain.Movie
import domain.TrendingDto
import events.HomePageEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.network.ApiServiceClient

class HomeViewModel(
    private val apiServiceClient: ApiServiceClient
): ScreenModel {

    var page = 1
    
    val MAX_PAGE_LIMIT = 100

    val showLoader: MutableState<Boolean> = mutableStateOf(false)
    
    private val _trendingDtoList: MutableState<MutableList<TrendingDto?>> = mutableStateOf(mutableListOf())
    val trendingDtoList: State<List<TrendingDto?>> get() = _trendingDtoList

    private val _bannerList: MutableState<MutableList<Movie>> = mutableStateOf(mutableListOf())
    val bannerList: State<List<Movie>> get() = _bannerList
    private val _contentList: MutableState<MutableList<Movie>> = mutableStateOf(mutableListOf())
    val contentList: State<List<Movie>> get() = _contentList

    private val _likedListMap: MutableState<MutableMap<Int, Boolean>> = mutableStateOf(mutableMapOf())
    val likedListMap: State<Map<Int, Boolean>> get() = _likedListMap

    suspend fun init() {
        try {
            println("init called")
            val response = apiServiceClient.getTrendingMoviesForWeek(page)
            _trendingDtoList.value.add(response)
            response?.let {
                it.results.forEach {
                    _bannerList.value.add(it)
                }
            }
            println("bannerList: $bannerList")
            page++
            val page2Data = apiServiceClient.getTrendingMoviesForWeek(page)
            _trendingDtoList.value.add(page2Data)
            page2Data?.let {
                it.results.forEach {
                    _contentList.value.add(it)
                }
            }
            println("contentList: $contentList")
        } catch (ex: Exception) {
            println("init: exception -> ${ex.message}")
            ex.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) {
                showLoader.value = false
            }
        }

    }

    fun <T> onEvent(event: T) {

        when(event) {

            is HomePageEvents.OnLikeButtonClicked -> {
                if (event.id != 0) {
                    _likedListMap.value[event.id] = event.isLiked
                }
            }

        }

    }
    
    suspend fun loadMoreData() {
        try {
            page++
            if (page <= MAX_PAGE_LIMIT){
                val responseData = apiServiceClient.getTrendingMoviesForWeek(page)
                _trendingDtoList.value.add(responseData)
                val tempList = contentList.value.toMutableList()
                responseData?.let {
                    it.results?.forEach {
                        tempList.add(it)
                    }
                    _contentList.value = tempList
                }
            }
        } catch (ex: Exception) {
            println("init: exception -> ${ex.message}")
            ex.printStackTrace()
        }
    }

    fun isLiked(id: Int): Boolean {
        return likedListMap.value[id] ?: false
    }

}