package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharactersViewAdapter
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.*
import malidaca.marvellisimo.services.FireBaseService
import java.lang.Exception

class SeriesDetailsActivity : AppCompatActivity() {

    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var topToolbar: Toolbar
    lateinit var view: View
    private lateinit var activityHelper: ActivityHelper
    private lateinit var loadDialog: LoadDialog

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadDialog = LoadDialog(this)
        loadDialog.showDialog()
        setContentView(R.layout.activity_series_details)
        view = findViewById(android.R.id.content)
        initToolbar()
        val context = this
        activityHelper = ActivityHelper()

        val id = intent.getIntExtra("id", 0)
        var response: Series
        MarvelServiceHandler.seriesByIdRequest(id).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            if (data.data.results.isNotEmpty()) {
                response = data.data.results[0]
                createImage(response, context)
                fillViewsWithSeriesData(response)
                setClickListener(response)
                getCharactersFromSeries(id)
            }
        }

    }

    @SuppressLint("CheckResult")
    fun getCharactersFromSeries(id: Int) {
        MarvelServiceHandler.charactersBySeriesIdRequest(0, id)
                .observeOn(AndroidSchedulers.mainThread()).doOnSuccess {
                    loadDialog.hideDialog()
                }
                .subscribe { data ->
                    var characters = data.data.results
                    viewManager = LinearLayoutManager(this)
                    viewAdapter = CharactersViewAdapter(characters, this)
                    recyclerView = findViewById<RecyclerView>(R.id.characters_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
    }

    private fun createImage(series: Series, context: Context) {
        var webViewExist = true
        try {
            WebView(context).settings.userAgentString
        } catch (e: Exception) {
            webViewExist = false
            println(e.localizedMessage)
        }
        var charUrl = series.urls[0].url
        for (url in series.urls)
            if (url.type == "detail") {
                charUrl = url.url
            }
        var path = "${series.thumbnail.path}/landscape_incredible.${series.thumbnail.extension}"
        path = path.replace("http", "https")
        Picasso.get().load(path).resize(928, 522).into(series_picture)
        series_picture.setOnClickListener {
            if (webViewExist && charUrl.isNotEmpty()) {
                val intent = Intent(context, WebViewer::class.java)
                intent.putExtra("url", charUrl)
                context.startActivity(intent)
            } else {
                SnackbarManager().createSnackbar(view, "No infopage available", R.color.colorPrimaryDark)
            }
        }
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

    private fun setClickListener(series: Series) {
        homeButton3.setOnClickListener {
            activityHelper.changeActivity(this, MenuActivity::class.java)
        }
        share_button.setOnClickListener {
            shareSeries(series)
        }
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
            R.id.people_online -> {
                val fragment = PeopleOnline()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .addToBackStack(null)
                        .add(R.id.fragment_container, fragment)
                        .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
    }

    private fun shareSeries(series: Series) {
        val itemName = series.title
        val itemType = resources.getString(R.string.menu_series)
        val itemId = series.id
        var sender: String
        FireBaseService.getUsersName()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    var user = data.getValue(User::class.java)
                    sender = "${user!!.firstName} ${user!!.lastName}"
                    val message = Message(sender, itemName, itemType, itemId)
                }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}