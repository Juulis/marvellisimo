package malidaca.marvellisimo.utilities

import android.content.Context
import android.content.Intent

class ActivityHelper {
    fun changeActivity(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }

   fun changeActivityFavorite(context: Context, cls: Class<*>, value: String) {
       val intent = Intent(context, cls)
       intent.putExtra("type", value)
       context.startActivity(intent)
    }
}