package malidaca.marvellisimo.rest

import io.reactivex.schedulers.Schedulers
import malidaca.marvellisimo.utilities.HashHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object MarvelServiceHandler {
    private const val baseUrl = "https://gateway.marvel.com/v1/public/"
    private val pubKey = HashHandler().publicKey
    private val service: MarvelService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(MarvelService::class.java)


    /**
     * To make new api call, make new when statement and fill ar with the result
     * */
    fun request(str: String, search:String=""): Array<*>? {
        val time = getTime()
        val hash = HashHandler().getHash(time)
        var ar:Array<*>? = null

        when (str) {
            "series" -> {
                service.getSeries(time, pubKey, hash).subscribeOn(Schedulers.io()).subscribe { wrapper -> ar = wrapper.data.results }
            }
            "characterX" -> {
                service.getCharacters(time, pubKey, hash,search).subscribeOn(Schedulers.io()).subscribe { wrapper -> ar = wrapper.data.results }
            }
            "character" -> {
                service.getCharacters(time, pubKey, hash).subscribeOn(Schedulers.io()).subscribe { wrapper -> ar = wrapper.data.results }
            }
        }

        //wait on db to be ready before returning ar
        while (ar == null || ar!!.isEmpty())
            Thread.sleep(5)
        return ar
    }

    /**
     * If we want observers, this is the way to add:
     * val logging = HttpLoggingInterceptor()
     * logging.level = HttpLoggingInterceptor.Level.BODY
     * val builder = OkHttpClient.Builder()
     * builder.addInterceptor(logging)
     * val okHttpClient = builder.build()
     */
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val okHttpClient = builder.build()
        return okHttpClient
    }
    private fun getTime(): String {
        return Date().time.toString()
    }

}