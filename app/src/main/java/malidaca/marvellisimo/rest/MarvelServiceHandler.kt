package malidaca.marvellisimo.rest

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import malidaca.marvellisimo.rest.characters.CharactersApiResponse
import malidaca.marvellisimo.rest.characters.CharactersDataModel
import malidaca.marvellisimo.rest.characters.CharactersService
import malidaca.marvellisimo.rest.series.SeriesApiResponse
import malidaca.marvellisimo.rest.series.SeriesDataModel
import malidaca.marvellisimo.rest.series.SeriesService
import malidaca.marvellisimo.utilities.HashHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object MarvelServiceHandler {
    private var errorCount: Int = 0
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
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    SeriesApiResponse(1, "", SeriesDataModel(emptyArray()))
                }
    }


    fun seriesByIdRequest(id: Int): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSeriesById(id, ts, HashHandler.publicKey, HashHandler.getHash(ts))
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    (SeriesApiResponse(1, "", SeriesDataModel(emptyArray())))
                }
    }

    fun seriesByCharactersId(offset: Int, id: Int): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSeriesByCharactersId(id, ts, HashHandler.publicKey, HashHandler.getHash(ts), offset)
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    (SeriesApiResponse(1, "", SeriesDataModel(emptyArray())))
                }
    }


    fun charactersRequest(offset: Int): Single<CharactersApiResponse> {
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharacters(ts, HashHandler.publicKey, HashHandler.getHash(ts), offset)
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    CharactersApiResponse(1, "", CharactersDataModel(emptyArray()))
                }
    }


    fun charactersByIdRequest(id: Int): Single<CharactersApiResponse> {
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharactersById(id, ts, HashHandler.publicKey, HashHandler.getHash(ts))
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    CharactersApiResponse(1, "", CharactersDataModel(emptyArray()))
                }
    }

    fun characterByNameRequest(offset: Int, search: String): Single<CharactersApiResponse> {
        val resultLimit = 20
        val ts = Date().time.toString()
        val service: CharactersService = retrofit.create(CharactersService::class.java)
        return service.getCharacterByName(ts, HashHandler.publicKey, HashHandler.getHash(ts), search, resultLimit, offset)
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    CharactersApiResponse(1, "", CharactersDataModel(emptyArray()))
                }
    }


    fun serieByNameRequest(offset: Int, search: String): Single<SeriesApiResponse> {
        val ts = Date().time.toString()
        val service: SeriesService = retrofit.create(SeriesService::class.java)
        return service.getSerieByName(ts, HashHandler.publicKey, HashHandler.getHash(ts), search, offset)
                .subscribeOn(Schedulers.io())
                .retry(10)
                .onErrorReturn {
                    //TODO: make snackbar appear
                    println("error: ${it.message}")
                    SeriesApiResponse(1, "", SeriesDataModel(emptyArray()))
                }
    }
}


