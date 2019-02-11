package malidaca.marvellisimo.services

import io.realm.Realm
import io.realm.kotlin.where
import malidaca.marvellisimo.models.Favorite

object RealmService {

    fun addFavorite(realm: Realm, id: Int, favoriteType: String) {
        FireBaseService.addFavorite(id, favoriteType)

        realm.beginTransaction()
        val favorite = Favorite().apply {
            itemId = id
            type = favoriteType
        }
        realm.copyToRealmOrUpdate(favorite)
        realm.commitTransaction()

    }

    fun deleteFavorite(realm: Realm, id: Int, favoriteType: String) {
        FireBaseService.deleteFavorite(id, favoriteType)
        realm.executeTransaction {
            it.where<Favorite>()
                    .equalTo("itemId", id).findAll().deleteAllFromRealm()
        }
    }
}