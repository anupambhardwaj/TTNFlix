package presentation.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import events.HomePageEvents

class DetailsScreenViewModel(): ScreenModel {

    private val _likedListMap: MutableState<MutableMap<Int, Boolean>> = mutableStateOf(mutableMapOf())
    val likedListMap: State<Map<Int, Boolean>> get() = _likedListMap

    init {
        _likedListMap.value = mutableMapOf()
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

}
