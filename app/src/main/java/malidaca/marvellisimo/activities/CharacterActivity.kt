package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import kotlinx.android.synthetic.main.activity_character_new.*
import kotlinx.android.synthetic.main.series_list_view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.SeriesListAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.LoadDialog


class CharacterActivity : AppCompatActivity() {

    private var loadDialog: LoadDialog? = null
    private var favorite: Boolean = false
    private  var redFavorite: Int = 0
    private  var blackFavorite: Int = 0
    private lateinit var adapter: SeriesListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var topToolbar: Toolbar

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_new)
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)

        redFavorite = R.drawable.favorite_red
        blackFavorite = R.drawable.favorite_black

        loadDialog = LoadDialog(this)
        loadDialog!!.showDialog()
        gridLayoutManager = GridLayoutManager(this, 2)
        series_grid_view.layoutManager = gridLayoutManager
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
                            createImage(character)
                        }
                    }
            initAdapter(id)
            initScrollListener(gridLayoutManager,id)
        }
        Picasso.get().load(blackFavorite).into(favoriteBtn)
    }

    fun createImage(character: Character){
        var url = "${character.thumbnail.path}//landscape_amazing.${character.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).into(bigpic)
    }

    private fun initScrollListener(gridLayoutManager: GridLayoutManager, id: Int) {
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {  // Use scrollListener.resetState() to reset list when searching
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addItems(page * 10, id) //multiply with how many items you showing in list
            }
        }
        series_grid_view.addOnScrollListener(scrollListener)
    }

    @SuppressLint("CheckResult")
    private fun addItems(offset: Int, id: Int) {
        MarvelServiceHandler.seriesByCharactersId(offset, id).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    adapter.addItems(data.data.results.asList())
                    loadDialog!!.hideDialog()
                }
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(id: Int) {
        MarvelServiceHandler.seriesByCharactersId(0, id).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    adapter = SeriesListAdapter(data.data.results.asList(), this)
                    series_grid_view.adapter = adapter
                    loadDialog!!.hideDialog()
                }
    }

    fun changeFavorite(view: View) {
        favorite = !favorite
        if (favorite) {
            Picasso.get().load(redFavorite).into(favoriteBtn)
        } else {
            Picasso.get().load(blackFavorite).into(favoriteBtn)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}
