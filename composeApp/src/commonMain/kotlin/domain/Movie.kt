package domain

import kotlinx.serialization.Serializable


@Serializable
data class Movie (

  var backdrop_path: String? = null,
  var id: Int? = null,
  var title: String? = null,
  var original_title: String? = null,
  var overview: String? = null,
  var poster_path: String? = null,
  var media_type: String? = null,
  var adult: Boolean? = null,
  var original_language: String? = null,
  var genre_ids: List<Int>? = null,
  var popularity: Double? = null,
  var release_date: String? = null,
  var video: Boolean? = null,
  var vote_average: Double? = null,
  var vote_count: Int? = null

)