package malidaca.marvellisimo.rest.characters

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersService{

    @GET("/v1/public/characters")
    fun getCharacters(@Query("ts") ts: String,
                  @Query("apikey") apikey: String,
                  @Query("hash") hash: String): Single<CharactersApiResponse>
}