package malidaca.marvellisimo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListeners()
    }


    //TODO Add your own activity in your own clickListener
    fun setClickListeners() {
    val searchButton = findViewById<Button>(R.id.menu_button_search)
    val characterButton = findViewById<Button>(R.id.menu_button_characters)
    val seriesButton = findViewById<Button>(R.id.menu_button_series)
        /*searchButton.setOnClickListener {
            val intent = Intent(this, CHANGETHIS::class.java)
            startActivity(intent)
        }*/

        characterButton.setOnClickListener {
            val chars = MarvelServiceHandler.request("characterX","spider")
            for(c in chars!!){
                c as Character
                println(c.name)
            }

            //            val intent = Intent(this, CHANGETHIS::class.java)
//            startActivity(intent)
        }

        seriesButton.setOnClickListener {
            val intent = Intent(this, SeriesActivity::class.java)
            startActivity(intent)
        }
    }
}
