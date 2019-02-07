package malidaca.marvellisimo.utilities

import android.content.Context
import android.content.Intent

class ActivityHelper {
    fun changeActivity(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }
}