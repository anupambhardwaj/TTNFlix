package ui.network

import domain.TrendingDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"

class ApiServiceClient(
    private val httpClient: HttpClient
) {
    
    private val BAES_URL = "http://0.0.0.0:8080"

    
    fun getHttpClient(): HttpClient {
        return httpClient
    }

//    suspend fun signUp(email: String, password: String): UserDetails? {
//
//        val response: ApiResponse<AuthResponseModel>? = httpClient?.post("$BAES_URL/signUp") {
//            contentType(ContentType.Application.Json)
//            setBody(AuthRequest(email, password))
//        }?.body<ApiResponse<AuthResponseModel>>()
//
//        if (response == null)
//            return null
//
//        if (!response.success)
//            return null
//
//        if (response.data == null)
//            return null
//
//        if (response.data.isFailure())
//            return null
//
//        val data = response.data
//        val userDetails = data.toUserDetails(password)
//        return userDetails
//    }

//    suspend fun signIn(email: String, password: String): UserDetails? {
//        val response: ApiResponse<AuthResponseModel>? = httpClient?.post("$BAES_URL/signIn") {
//            contentType(ContentType.Application.Json)
//            setBody(AuthRequest(email, password))
//        }?.body<ApiResponse<AuthResponseModel>>()
//
//        if (response == null)
//            return null
//
//        if (!response.success)
//            return null
//
//        if (response.data == null)
//            return null
//
//        if (response.data.isFailure())
//            return null
//
//        val data = response.data
//        val userDetails = data.toUserDetails(password)
//        return userDetails
//    }

    suspend fun getTrendingMoviesForWeek(pageNumber: Int): TrendingDto? {
        
        return try {
            val response = httpClient.get(
                urlString = "https://api.themoviedb.org/3/trending/movie/week?api_key=060e7c76aff06a20ca4a875981216f3f&page=$pageNumber"
            )
            val responseString = response.body<TrendingDto>()
            println("responseString: $responseString")
            responseString
            
        } catch (ex: Exception) {
            println("Exception: getTrendingMoviesForWeek -> ${ex.message}")
            null
        }
        
    }

//    suspend fun authenticate(userDetails: UserDetails): UserDetails? {
//        try {
//            val response: ApiResponse<String?>? = httpClient?.get("$BAES_URL/authenticated") {
//                contentType(ContentType.Application.Json)
//                header("Authorization", "Bearer ${userDetails.token}")
//            }?.body<ApiResponse<String?>>()
//            if (response?.success == false) {
//                if (response?.message.equals("User Unauthorized")) {
//                    val updatedUserDetails = signIn(userDetails.email, userDetails.password)
//                    return updatedUserDetails
//                } else {
//                    return null
//                }
//            } else {
//                return null
//            }
//        } catch (ex: Exception) {
//            println(ex?.message)
//            return null
//        }
//    }

}