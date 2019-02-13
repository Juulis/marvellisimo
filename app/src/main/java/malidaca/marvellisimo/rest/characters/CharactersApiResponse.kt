package malidaca.marvellisimo.rest.characters

import com.google.gson.annotations.SerializedName

class CharactersApiResponse(@SerializedName("code") val code: Int,
                            @SerializedName("status") val status: String,
                            @SerializedName("data") val data: CharactersDataModel)