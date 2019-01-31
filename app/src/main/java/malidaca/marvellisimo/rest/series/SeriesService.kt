package malidaca.marvellisimo.rest.series

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesService{

    @GET("/v1/public/series")
    fun getSeries(@Query("ts") ts: String,
                  @Query("apikey") apikey: String,
                  @Query("hash") hash: String): Single<SeriesApiResponse>
}