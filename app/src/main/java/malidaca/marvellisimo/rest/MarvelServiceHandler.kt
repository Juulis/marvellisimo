package malidaca.marvellisimo.rest

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import malidaca.marvellisimo.rest.characters.CharactersApiResponse
import malidaca.marvellisimo.rest.characters.CharactersService
import malidaca.marvellisimo.rest.series.SeriesApiResponse
import malidaca.marvellisimo.rest.series.SeriesService
import malidaca.marvellisimo.utilities.HashHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object MarvelServiceHandler {
    private val baseUrl = "https://gateway.marvel.com"
    private val httpClient = OkHttpClient.Builder()
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()


    fun seriesRequest(): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSeries(ts, HashHandler.publicKey, HashHandler.getHash(ts)).subscribeOn(Schedulers.io())
    }

    fun seriesByCharactersId(id: Int): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSeriesByCharactersId(id, ts, HashHandler.publicKey, HashHandler.getHash(ts)).subscribeOn(Schedulers.io())
    }

    fun charactersRequest(): Single<CharactersApiResponse> {
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharacters(ts, HashHandler.publicKey, HashHandler.getHash(ts)).subscribeOn(Schedulers.io())
    }

    fun charactersByIdRequest(id: Int): Single<CharactersApiResponse> {
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharactersById( id , ts, HashHandler.publicKey, HashHandler.getHash(ts)).subscribeOn(Schedulers.io())
    }



    fun characterXRequest(search:String): Single<CharactersApiResponse> {
        val resultLimit = 50
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharacterX(ts, HashHandler.publicKey, HashHandler.getHash(ts), search, resultLimit).subscribeOn(Schedulers.io())
    }

    fun serieXRequest(search:String): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSerieX(ts, HashHandler.publicKey, HashHandler.getHash(ts), search).subscribeOn(Schedulers.io())
    }
}