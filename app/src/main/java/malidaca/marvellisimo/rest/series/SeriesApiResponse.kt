package malidaca.marvellisimo.rest.series

import com.google.gson.annotations.SerializedName

class SeriesApiResponse(@SerializedName("code") val code: Int,
                        @SerializedName("status") val status: String,
                        @SerializedName("data") val data: SeriesDataModel)