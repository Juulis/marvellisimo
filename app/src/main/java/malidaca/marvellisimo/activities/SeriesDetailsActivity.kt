package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharactersViewAdapter
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.LoadDialog
import malidaca.marvellisimo.utilities.SnackbarManager

class SeriesDetailsActivity : AppCompatActivity() {

    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var view: View

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoadDialog(this).showDialog()
        setContentView(R.layout.activity_series_details)
        view = findViewById(android.R.id.content)
        val id = intent.getIntExtra("id", 0)
        var response: Series
        MarvelServiceHandler.seriesByIdRequest(id).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            if(data.data.results.isNotEmpty()){
                response = data.data.results[0]
                var path = "${response.thumbnail.path}/landscape_incredible.${response.thumbnail.extension}"
                path = path.replace("http", "https")
                Picasso.get().load(path).resize(928, 522).into(series_picture)
                series_title.text = response.title
                series_description.text = response.description
                series_start_year.text = "START YEAR: " + response.startYear.toString()
                series_end_year.text = "END YEAR: " + response.endYear.toString()
                series_rating.text = "RATING: " + response.rating
                val creators = response.creators.items
                var creatorsNames = ""
                for (cr in creators)
                    creatorsNames += "${cr.name}, "
                series_creators.text = "CREATORS: $creatorsNames"
                val comics = response.comics.items
                var comicsTitles = ""
                for (co in comics)
                    comicsTitles += "${co.name}, "
                series_comics.text = "COMICS: $comicsTitles"
                getCharactersFromSeries(id)
            }
            LoadDialog(this).hideDialog()
        }
    }

    @SuppressLint("CheckResult")
    fun getCharactersFromSeries(id: Int) {
        SnackbarManager().createSnackbar(view,"Loading content", R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
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
}