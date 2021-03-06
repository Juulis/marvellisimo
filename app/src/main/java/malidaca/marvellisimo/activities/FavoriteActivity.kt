package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_favorite.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.adapters.SeriesListAdapter
import malidaca.marvellisimo.adapters.SeriesViewAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Favorite
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.services.FireBaseService

class FavoriteActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private var characterList: MutableList<Character> = mutableListOf()
    private var seriesList: MutableList<Series> = mutableListOf()
    private lateinit var characterAdapter: CharacterListAdapter
    private lateinit var seriesAdapter: SeriesViewAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private var type: String? = ""
    private lateinit var userFavorites: RealmResults<Favorite>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        realm = Realm.getDefaultInstance()
        RECYCLER_FAVORITES.layoutManager = linearLayoutManager
    }

    override fun onPause() {
        FireBaseService.toggleOnline(false)
        super.onPause()
    }

    override fun onResume() {
        checkType()
        FireBaseService.checkIfOnline(this)
        FireBaseService.toggleOnline(true)
        super.onResume()
    }

    @SuppressLint("CheckResult")
    private fun getCharacterFavorites(type: String) {
        characterList.clear()
        userFavorites = realm.where<Favorite>().equalTo("type", "Characters").findAll()
        userFavorites.addChangeListener { data -> characterAdapter.addFavorites(data) }
        characterAdapter = CharacterListAdapter(characterList, this, userFavorites)

        RECYCLER_FAVORITES.adapter = characterAdapter
        val characterFavorites = realm.where<Favorite>().equalTo("type", type).findAll()
        for (i in 0 until characterFavorites.size) {
            MarvelServiceHandler.charactersByIdRequest(characterFavorites[i]!!.itemId!!).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                characterList.add(data.data.results[0])
                if (i + 1 == characterFavorites.size) {
                    setItems(type)
                }
            }
            characterAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("CheckResult")
    private fun getSeriesFavorites(type: String) {
        seriesList.clear()
        userFavorites = realm.where<Favorite>().equalTo("type", "Series").findAll()
        userFavorites.addChangeListener { data -> seriesAdapter.addFavorites(data) }
        seriesAdapter = SeriesViewAdapter(seriesList, this, userFavorites)

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
            seriesAdapter.notifyDataSetChanged()
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
