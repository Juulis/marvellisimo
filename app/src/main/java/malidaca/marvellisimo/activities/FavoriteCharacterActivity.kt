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
    private fun getCharacterFavorites(type: String) {

        characterAdapter = CharacterListAdapter(characterList, this, realm)
        RECYCLER_FAVORITES.adapter = characterAdapter
        val characterFavorites = realm.where<Favorite>().equalTo("type", type).findAll()
        for (i in 0 until characterFavorites.size) {
            MarvelServiceHandler.charactersByIdRequest(characterFavorites[i]!!.itemId!!).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                characterList.add(data.data.results[0])
                if (i + 1 == characterFavorites.size) {
                    setItems(type)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getSeriesFavorites(type: String) {
        seriesAdapter = SeriesListAdapter(seriesList, this)
        RECYCLER_FAVORITES.adapter = seriesAdapter
        val seriesFavorites = realm.where<Favorite>().equalTo("type", type).findAll()
        for (i in 0 until seriesFavorites.size) {
            MarvelServiceHandler.seriesByIdRequest(seriesFavorites[i]!!.itemId!!).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                seriesList.add(data.data.results[0])
                if (i + 1 == seriesFavorites.size) {
                    setItems(type)
                    println(seriesList.size)
                }
            }
        }
    }

    private fun setItems(type: String) {
        if (type == "Characters") {
            characterAdapter.addItems(characterList)
        } else if (type == "Series") {
            seriesAdapter.addItems(seriesList)
        }
    }

    private fun checkType() {
        val extras = intent.extras
        if (extras != null) {
            type = extras.getString("type")
            when (type) {
                "Characters" -> getCharacterFavorites(type!!)
                "Series" -> getSeriesFavorites(type!!)
            }
        }
    }

}
