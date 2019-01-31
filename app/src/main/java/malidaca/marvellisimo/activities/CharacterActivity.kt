package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.character_list_card.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler


class CharacterActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        var character: Character
        val extras = intent.extras
        if (extras != null) {
            val id: Int = extras.getInt("itemId")

            MarvelServiceHandler.charactersByIdRequest(id).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { data ->
                        character = data.data.results[0]

                        characterName.text = character.name
                        infoText.text = character.description
                        val url = "${character.thumbnail.path}//landscape_amazing.${character.thumbnail.extension}"
                        var split1 = url.subSequence(0, 4)
                        var split2 = url.subSequence(4, url.length)
                        val newUrl = "${split1}s$split2"

                        Picasso.get().load(newUrl).into(bigpic)
                    }

        }
    }
}
