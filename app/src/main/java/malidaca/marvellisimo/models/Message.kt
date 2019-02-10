package malidaca.marvellisimo.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Message(
        val sender: String,
        val itemName: String,
        val itemType: String,
        val itemId: Int)