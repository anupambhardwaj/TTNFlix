package events

sealed class HomePageEvents() {

    data class OnLikeButtonClicked(val id: Int, val isLiked: Boolean) : HomePageEvents()

}