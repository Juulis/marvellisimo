package malidaca.marvellisimo.rest

import malidaca.marvellisimo.models.ApiResponse
import malidaca.marvellisimo.utilities.HashHandler
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MarvelServiceHandler{
    private val baseUrl = "https://gateway.marvel.com/v1/public/"
    private val httpClient = OkHttpClient.Builder()
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    private val service: MarvelService = retrofit.create(MarvelService::class.java)
    private val ts = Date().time.toString()
    private val callAsync = service.getSeries(ts, HashHandler().publicKey, HashHandler().getHash(ts))

    fun sendRequest(){
        callAsync.enqueue(object: Callback<ApiResponse> {
            override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                var responseBody = response.body()
                var status = responseBody?.status
                var code = responseBody?.code
                var series = responseBody?.data?.results

                println("Status: $status, Code: $code")
                println("Series: $series")
            }

            override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                println(t)
            }
        })
    }
}