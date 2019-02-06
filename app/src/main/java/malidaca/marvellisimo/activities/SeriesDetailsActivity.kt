package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharactersViewAdapter
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler

class SeriesDetailsActivity : AppCompatActivity() {

    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var topToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_details)
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)

        val id = intent.getIntExtra("id", 0)
        var response: Series

        MarvelServiceHandler.seriesByIdRequest(id).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            if(data.data.results.isNotEmpty()){
                response = data.data.results[0]
                createImage(response)
                fillViewsWithSeriesData(response)
                getCharactersFromSeries(id)
            }
        }
    }

    fun getCharactersFromSeries(id: Int) {
        MarvelServiceHandler.charactersBySeriesIdRequest(0, id).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            var characters = data.data.results
            println(characters.size)
            viewManager = LinearLayoutManager(this)
            viewAdapter = CharactersViewAdapter(characters, this)
            recyclerView = findViewById<RecyclerView>(R.id.characters_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
    
    fun createImage(series: Series){
        var path = "${series.thumbnail.path}/landscape_incredible.${series.thumbnail.extension}"
        path = path.replace("http", "https")
        Picasso.get().load(path).resize(928, 522).into(series_picture)
    }

    fun fillViewsWithSeriesData(series: Series) {
        series_title.text = series.title
        series_description.text = series.description
        series_start_year.text = "START YEAR: " + series.startYear.toString()
        series_end_year.text = "END YEAR: " + series.endYear.toString()
        series_rating.text = "RATING: " + series.rating
        val creators = series.creators.items
        var creatorsNames = ""
        for (cr in creators)
            creatorsNames += "${cr.name}, "
        series_creators.text = "CREATORS: $creatorsNames"
        val comics = series.comics.items
        var comicsTitles = ""
        for (co in comics)
            comicsTitles += "${co.name}, "
        series_comics.text = "COMICS: $comicsTitles"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}