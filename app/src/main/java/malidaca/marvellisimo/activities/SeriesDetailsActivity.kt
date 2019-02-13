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
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharactersViewAdapter
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Favorite
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.*
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.services.RealmService
import java.lang.Exception

class SeriesDetailsActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private var id: Int = 0
    private var favorite: Boolean = false
    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var topToolbar: Toolbar
    lateinit var view: View
    private lateinit var activityHelper: ActivityHelper
    private lateinit var loadDialog: LoadDialog

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        realm = Realm.getDefaultInstance()
        super.onCreate(savedInstanceState)
        loadDialog = LoadDialog(this)
        loadDialog.showDialog()
        setContentView(R.layout.activity_series_details)
        view = findViewById(android.R.id.content)
        initToolbar()
        val context = this
        activityHelper = ActivityHelper()

        id = intent.getIntExtra("id", 0)
Picasso.get().load(R.drawable.share_icon).placeholder(R.drawable.share_icon).into(share_series_button)
        checkIfIsFavorite(id)
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
        Picasso.get().load(path).into(series_picture)
        series_picture.setOnClickListener {
            if (webViewExist && charUrl.isNotEmpty()) {
                val intent = Intent(context, WebViewer::class.java)
                intent.putExtra("url", charUrl)
                context.startActivity(intent)
            } else {
                SnackbarManager().createSnackbar(view, getString(R.string.no_infopage), R.color.colorPrimaryDark)
            }
        }
    }

    fun fillViewsWithSeriesData(series: Series) {
        series_title.text = series.title
        series_description.text = series.description
        series_start_year.text = getString(R.string.start_year, series.startYear.toString())//"START YEAR: " + series.startYear.toString()
        series_end_year.text = getString(R.string.end_year, series.endYear.toString())//"END YEAR: " + series.endYear.toString()
        series_rating.text = getString(R.string.rating, series.rating)//"RATING: " + series.rating
        val creators = series.creators.items
        var creatorsNames = ""
        for (cr in creators)
            creatorsNames += "${cr.name}, "
        series_creators.text = getString(R.string.creators, creatorsNames)//"CREATORS: $creatorsNames"
        val comics = series.comics.items
        var comicsTitles = ""
        for (co in comics)
            comicsTitles += "${co.name}, "
        series_comics.text = getString(R.string.comics, comicsTitles)//"COMICS: $comicsTitles"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun setClickListener(series: Series) {
        homeButton3.setOnClickListener {
            activityHelper.changeActivity(this, MenuActivity::class.java)
        }
        share_series_button.setOnClickListener {
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
                activityHelper.changeActivityFavorite(this, FavoriteActivity::class.java, "Characters")
            }
            R.id.favorite_series -> {
                activityHelper.changeActivityFavorite(this, FavoriteActivity::class.java, "Series")
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

    @SuppressLint("CheckResult")
    private fun shareSeries(series: Series) {
        val itemName = series.title
        val itemType = resources.getString(R.string.menu_series)
        val itemId = series.id
        var sender: String
        FireBaseService.getUsersName()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ data ->
                    sender = data.value as String
                    val message = Message(sender, itemName, itemType, itemId)
                    val fragment = PeopleOnline.newInstance(message)
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .addToBackStack(null)
                            .add(R.id.fragment_container, fragment)
                            .commitAllowingStateLoss()
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

    fun changeSeriesFavorite(view: View) {
        favorite = !favorite
        if (favorite) {
            Picasso.get().load(R.drawable.thumbs_up_yes).into(series_f_btn)
            RealmService.addFavorite(realm, id, "Series")
        } else {
            Picasso.get().load(R.drawable.thumbs_up).into(series_f_btn)
            RealmService.deleteFavorite(realm, id, "Series")
        }
    }

    private fun checkIfIsFavorite(thisId: Int) {
        val result = realm.where<Favorite>().equalTo("itemId", thisId).findFirst()
        if (result != null) {
            Picasso.get().load(R.drawable.thumbs_up_yes).into(series_f_btn)
            favorite = true
        } else {
            Picasso.get().load(R.drawable.thumbs_up).into(series_f_btn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}