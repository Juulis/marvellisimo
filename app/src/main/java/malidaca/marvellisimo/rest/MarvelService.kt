package malidaca.marvellisimo.rest

import io.reactivex.Single
import malidaca.marvellisimo.models.*
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("series")
    fun getSeries(@Query("ts") ts: String,
                  @Query("apikey") apikey: String,
                  @Query("hash") hash: String): Single<ApiResponse<Array<Series>>>

    @GET("characters")
    fun getCharacters(@Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String,
                      @Query("nameStartsWith") nameStartsWith: String): Single<ApiResponse<Array<Character>>>
    @GET("characters")
    fun getCharacters(@Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String): Single<ApiResponse<Array<ListCharacter>>>

}