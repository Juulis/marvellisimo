package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_new.*
import kotlinx.android.synthetic.main.series_list_view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.SeriesListAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.LoadDialog


class CharacterActivity : AppCompatActivity() {

    private var loadDialog: LoadDialog? = null
    private var favorite: Boolean = false
    private var redFavorite: Int = 0
    private var blackFavorite: Int = 0

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_new)

        redFavorite = R.drawable.favorite_red
        blackFavorite = R.drawable.favorite_black

        loadDialog = LoadDialog(this)
        loadDialog!!.showDialog()
        var character: Character
        val extras = intent.extras
        if (extras != null) {
            val id: Int = extras.getInt("itemId")

            MarvelServiceHandler.charactersByIdRequest(id).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { data ->
                        if (data.data.results.isNotEmpty()) {
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
            MarvelServiceHandler.seriesByCharactersId(id).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { data ->
                            series_grid_view.layoutManager = GridLayoutManager(this, 2)
                            series_grid_view.adapter = SeriesListAdapter(data.data.results, this)
                            loadDialog!!.hideDialog()
                        }
        }

        Picasso.get().load(blackFavorite).into(favoriteBtn)
    }

    fun changeFavorite(view: View) {
        favorite = !favorite
        if (favorite) {
            Picasso.get().load(redFavorite).into(favoriteBtn)
        } else {
            Picasso.get().load(blackFavorite).into(favoriteBtn)
        }
    }
}
