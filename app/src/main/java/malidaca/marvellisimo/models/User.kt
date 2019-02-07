package malidaca.marvellisimo.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        var email: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var favoriteCharacters: MutableCollection<String>? = null,
        var favoriteSeries: MutableCollection<String>? = null,
        var messages: MutableCollection<Message>? = null,
        var isOnline: Boolean = false
)