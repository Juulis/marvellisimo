package malidaca.marvellisimo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Favorite : RealmObject() {
    @PrimaryKey
    var itemId: Int? = null
    var type: String? = null
}

