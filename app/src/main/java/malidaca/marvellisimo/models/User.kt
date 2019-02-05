package malidaca.marvellisimo.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        var uID: String = "",
        var email: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var favoriteCharacters: MutableCollection<String>? = null,
        var favoriteSeries: MutableCollection<String>? = null,
        var isOnline: Boolean = false
)