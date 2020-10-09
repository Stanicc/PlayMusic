package stanic.playmusic.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import stanic.playmusic.service.model.Result

interface YoutubeService {

    @GET("search")
    fun getResult(
        @Query("part") part: String,
        @Query("q") q: String?,
        @Query("order") order: String,
        @Query("key") key: String?,
        @Query("type") type: String = "video",
        @Query("safeSearch") safeSearch: String = "none",
        @Query("videoType") videoType: String = "any",
        @Query("videoCaption") videoCaption: String = "any",
        @Query("videoDefinition") videoDefinition: String = "any",
        @Query("maxResults") maxResults: String = "50"
    ): Call<Result>

}