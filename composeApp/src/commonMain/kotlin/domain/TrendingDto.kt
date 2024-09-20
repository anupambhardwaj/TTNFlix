package domain

import kotlinx.serialization.Serializable

@Serializable
data class TrendingDto(

    var page: Int? = null,
    var results: ArrayList<Movie> = arrayListOf(),
    var total_pages: Int? = null,
    var total_results: Int? = null

)