package malidaca.marvellisimo.rest.series

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesService {

    @GET("/v1/public/series")
    fun getSeries(@Query("ts") ts: String,
                  @Query("apikey") apikey: String,
                  @Query("hash") hash: String,
                  @Query("offset") offset: Int): Single<SeriesApiResponse>

    @GET("/v1/public/series/{id}")
    fun getSeriesById(@Path("id") id: Int, @Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String): Single<SeriesApiResponse>


    @GET("/v1/public/characters/{id}/series")
    fun getSeriesByCharactersId(@Path("id") id: Int, @Query("ts") ts: String,
                                @Query("apikey") apikey: String,
                                @Query("hash") hash: String,
                                @Query("offset") offset: Int,
                                @Query("limit") limit: Int = 6): Single<SeriesApiResponse>

    @GET("/v1/public/series")
    fun getSerieByName(@Query("ts") ts: String,
                       @Query("apikey") apikey: String,
                       @Query("hash") hash: String,
                       @Query("titleStartsWith") titleStartsWith: String,
                       @Query("offset") offset: Int): Single<SeriesApiResponse>

}