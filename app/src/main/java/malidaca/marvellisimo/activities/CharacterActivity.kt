package malidaca.marvellisimo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler


class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val character: Character
        val extras = intent.extras
        if (extras != null) {
        val data:Int = extras.getInt("itemId")



        }
    }
}
