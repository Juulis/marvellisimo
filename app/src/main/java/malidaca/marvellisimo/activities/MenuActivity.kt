package malidaca.marvellisimo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import malidaca.marvellisimo.R
import malidaca.marvellisimo.services.FireBaseService

class MenuActivity : AppCompatActivity() {
    private lateinit var characterButton: Button
    private lateinit var seriesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initButtons()
        setClickListeners()
    }

    override fun onPause() {
        FireBaseService.toggleOnline(false)
        super.onPause()
    }

    override fun onResume() {
           FireBaseService.toggleOnline(true)
        super.onResume()
    }

    private fun initButtons() {
        characterButton = findViewById(R.id.menu_button_characters)
        seriesButton = findViewById(R.id.menu_button_series)
    }


    //TODO Add your own activity in your own clickListener
    private fun setClickListeners() {
        characterButton.setOnClickListener {

            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }

        seriesButton.setOnClickListener {
            val intent = Intent(this, SeriesActivity::class.java)
            startActivity(intent)
        }
    }
}
