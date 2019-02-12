package malidaca.marvellisimo.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class Message(
        val sender: String = "",
        val itemName: String = "",
        val itemType: String = "",
        val itemId: Int = 0): Serializable