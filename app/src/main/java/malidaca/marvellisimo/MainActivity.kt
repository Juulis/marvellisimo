package malidaca.marvellisimo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import malidaca.marvellisimo.login.LoginActivity

class MainActivity : AppCompatActivity() {


    private lateinit var searchButton: Button
    private lateinit var characterButton: Button
    private lateinit var seriesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtons()
        setClickListeners()
    }

    private fun initButtons() {
        searchButton = findViewById(R.id.menu_button_search)
        characterButton = findViewById(R.id.menu_button_characters)
        seriesButton = findViewById(R.id.menu_button_series)
    }


    //TODO Add your own activity in your own clickListener
    fun setClickListeners() {
        searchButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        /*characterButton.setOnClickListener {
            val intent = Intent(this, CHANGETHIS::class.java)
            startActivity(intent)
        }*/

        /*seriesButton.setOnClickListener {
            val intent = Intent(this, CHANGETHIS::class.java)
            startActivity(intent)
        }*/
    }
}
