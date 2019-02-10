package malidaca.marvellisimo.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        var email: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var favoriteCharacters: Map<String, Boolean>? = null,
        var favoriteSeries: Map<String, Boolean>? = null,
        var messages: MutableList<Message>? = null,
        var isOnline: Boolean = false
)