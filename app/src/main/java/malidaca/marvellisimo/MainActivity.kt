package malidaca.marvellisimo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import malidaca.marvellisimo.activities.SeriesActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    /*
        val intent = Intent(this, SeriesActivity::class.java).apply {
            action = Intent.ACTION_VIEW
        }
        if(intent.resolveActivity(packageManager) != null)
            startActivity(intent)
    */
    }
}
