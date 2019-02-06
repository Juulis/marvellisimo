package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharactersViewAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

class SeriesDetailsActivity : AppCompatActivity() {

    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var topToolbar: Toolbar
    private lateinit var activityHelper: ActivityHelper

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_details)
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
        activityHelper = ActivityHelper()

        val id = intent.getIntExtra("id", 0)
        var response: Series


        MarvelServiceHandler.seriesByIdRequest(id).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            if (data.data.results.isNotEmpty()) {
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
        }
    }


    @SuppressLint("CheckResult")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FireBaseService.signOut()
                activityHelper.changeActivity(this, LoginActivity::class.java)
                finish()
            }
            R.id.favorite_characters -> {
            }
            R.id.favorite_series -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}