package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_new.*
import kotlinx.android.synthetic.main.series_list_view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.SeriesListAdapter
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Message
import malidaca.marvellisimo.models.User
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper
import malidaca.marvellisimo.utilities.LoadDialog
import malidaca.marvellisimo.utilities.SnackbarManager
import java.lang.Exception


class CharacterActivity : AppCompatActivity() {

    private var loadDialog: LoadDialog? = null
    private var favorite: Boolean = false
    private var redFavorite: Int = 0
    private var blackFavorite: Int = 0
    private lateinit var adapter: SeriesListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var topToolbar: Toolbar
    private lateinit var context: Context
    private lateinit var view: View
    private lateinit var activityHelper: ActivityHelper

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_new)
        view = findViewById(android.R.id.content)
        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
        context = this

        activityHelper = ActivityHelper()

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
                            setClickListener(character)
                        }
                    }
            initAdapter(id)
            initScrollListener(gridLayoutManager, id)
        }
        Picasso.get().load(blackFavorite).into(favoriteBtn)
    }

    private fun setClickListener(character: Character) {
        homeButton.setOnClickListener {
            activityHelper.changeActivity(this, MenuActivity::class.java)
        }
        share_button.setOnClickListener {
            shareCharacter(character)
        }
    }

    private fun createImage(character: Character) {
        var webViewExist = true
        try {
            WebView(context).settings.userAgentString
        } catch (e: Exception) {
            webViewExist = false
            println(e.localizedMessage)
        }

        var charUrl = character.urls[0].url
        for (url in character.urls)
            if (url.type == "detail") {
                charUrl = url.url
            }
        var url = "${character.thumbnail.path}//landscape_amazing.${character.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).into(bigpic)
        bigpic.setOnClickListener {
            if (webViewExist && charUrl.isNotEmpty()) {
                val intent = Intent(context, WebViewer::class.java)
                intent.putExtra("url", charUrl)
                context.startActivity(intent)
            }else {
                SnackbarManager().createSnackbar(view, "No infopage available", R.color.colorPrimaryDark)
            }
        }
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
        if (offset > 10)
            SnackbarManager().createSnackbar(view, "Loading content", R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
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

    private fun shareCharacter(character: Character) {
        val itemName = character.name
        val itemType = resources.getString(R.string.menu_characters)
        val itemId = character.id
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
