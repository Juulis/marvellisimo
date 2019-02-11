package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_favorite_character.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.adapters.SeriesListAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Favorite
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler

class FavoriteCharacterActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private var characterList: MutableList<Character> = mutableListOf()
    private var seriesList: MutableList<Series> = mutableListOf()
    private lateinit var characterAdapter: CharacterListAdapter
    private lateinit var seriesAdapter: SeriesListAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_character)
        realm = Realm.getDefaultInstance()
        RECYCLER_FAVORITES.layoutManager = linearLayoutManager
        checkType()

    }

    @SuppressLint("CheckResult")
    private fun getCharacterFavorites() {

        characterAdapter = CharacterListAdapter(characterList, this, realm)
        RECYCLER_FAVORITES.adapter = characterAdapter
        val favorites = realm.where<Favorite>().equalTo("type", type).findAll()
        for (i in 0 until favorites.size) {
            MarvelServiceHandler.charactersByIdRequest(favorites[i]!!.itemId!!).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                characterList.add(data.data.results[0])
                if (i + 1 == favorites.size) {
                    setItems()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getSeriesFavorites() {
        seriesAdapter = SeriesListAdapter(seriesList, this)
        RECYCLER_FAVORITES.adapter = seriesAdapter
        val favorites = realm.where<Favorite>().equalTo("type", type).findAll()
        for (i in 0 until favorites.size) {
            MarvelServiceHandler.seriesByIdRequest(favorites[i]!!.itemId!!).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                seriesList.add(data.data.results[0])
                if (i + 1 == favorites.size) {
                    setItems()
                }
            }
        }
    }

    private fun setItems() {
        if (type.equals("character")) {
            characterAdapter.addItems(characterList)
        } else if (type.equals("series")) {
            seriesAdapter.addItems(seriesList)
        }
    }


    private fun checkType() {
        val extras = intent.extras

        if (extras != null) {
            type = extras.getString("type")
            when (type) {
                "character" -> getCharacterFavorites()
                "series" -> getSeriesFavorites()
            }
        }
    }

}
