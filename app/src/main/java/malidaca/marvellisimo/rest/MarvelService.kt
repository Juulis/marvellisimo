package malidaca.marvellisimo.rest

import io.reactivex.Single
import malidaca.marvellisimo.models.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService{

    @GET("series")
    fun getSeries(@Query("ts") ts: String,
                  @Query("apikey") apikey: String,
                  @Query("hash") hash: String): Single<ApiResponse>
}