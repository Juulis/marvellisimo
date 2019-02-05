package malidaca.marvellisimo.rest.characters

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("/v1/public/characters")
    fun getCharacters(@Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String,
                      @Query("offset") offset: Int): Single<CharactersApiResponse>

    @GET("/v1/public/characters/{id}")
    fun getCharactersById(@Path("id") id: Int, @Query("ts") ts: String,
                          @Query("apikey") apikey: String,
                          @Query("hash") hash: String): Single<CharactersApiResponse>

    @GET("/v1/public/series/{id}/characters")
    fun getCharactersBySeriesId(@Path("id") id: Int, @Query("ts") ts: String,
                                @Query("apikey") apikey: String,
                                @Query("hash") hash: String,
                                @Query("offset") offset: Int,
                                @Query("limit") limit: Int = 10): Single<CharactersApiResponse>

    @GET("/v1/public/characters")
    fun getCharacterX(@Query("ts") ts: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String,
                      @Query("nameStartsWith") nameStartsWith: String,
                      @Query("offset") limit: Int): Single<CharactersApiResponse>
}