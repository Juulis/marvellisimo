package malidaca.marvellisimo.models

import com.google.gson.annotations.SerializedName

class ApiResponse<T>(@SerializedName("code") val code: Int,
                  @SerializedName("status") val status: String,
                  @SerializedName("data") val data: DataModel<T>)