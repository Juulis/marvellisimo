package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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


class CharacterActivity : AppCompatActivity() {

    private var favorite: Boolean = false
    private lateinit var adapter: SeriesListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var gridLayoutManager: GridLayoutManager

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_new)
        gridLayoutManager = GridLayoutManager(this, 2)
        series_grid_view.layoutManager = gridLayoutManager
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
            initAdapter(id)
            initScrollListener(gridLayoutManager,id)
        }


        Picasso.get().load(R.drawable.favorite_black).into(favoriteBtn)
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
                }
    }

    @SuppressLint("CheckResult")
    private fun initAdapter(id: Int) {
        MarvelServiceHandler.seriesByCharactersId(0, id).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    adapter = SeriesListAdapter(data.data.results.asList(), this)
                    series_grid_view.adapter = adapter
                }
    }

    fun changeFavorite(view: View) {
        favorite = !favorite
        if (favorite) {
            Picasso.get().load(R.drawable.favorite_red).into(favoriteBtn)
        } else {
            Picasso.get().load(R.drawable.favorite_black).into(favoriteBtn)
        }
    }
}
