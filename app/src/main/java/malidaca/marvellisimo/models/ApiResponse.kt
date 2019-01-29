package malidaca.marvellisimo.models

import com.google.gson.annotations.SerializedName

class ApiResponse(@SerializedName("code") val code: Int,
                  @SerializedName("status") val status: String,
                  @SerializedName("data") val data: DataModel)