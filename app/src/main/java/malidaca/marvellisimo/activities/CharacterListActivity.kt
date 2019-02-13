package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.rest.MarvelServiceHandler
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_character_new.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.Favorite
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

import malidaca.marvellisimo.utilities.LoadDialog
import malidaca.marvellisimo.utilities.SnackbarManager


class CharacterListActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private var characterList: List<Character> = emptyList()
    private lateinit var adapter: CharacterListAdapter
    private var search: String = ""
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var view: View
    private lateinit var userFavorites: RealmResults<Favorite>

    lateinit var topToolbar: Toolbar


    var loadDialog: LoadDialog? = null
    private lateinit var activityHelper: ActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
        setContentView(R.layout.activity_character_list)
        view = findViewById(android.R.id.content)

        topToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
        val linearLayoutManager = LinearLayoutManager(this)
        RECYCLER.layoutManager = linearLayoutManager
        initAdapter()
        initScrollListener(linearLayoutManager)
        initQueryTextListener()
        loadDialog = LoadDialog(this)
        loadDialog!!.showDialog()
        setClickListener()
        activityHelper = ActivityHelper()
        Picasso.get().load(R.drawable.marvel_logo_test).placeholder(R.drawable.marvel_logo_test).into(homeButton1)


        userFavorites = realm.where<Favorite>().equalTo("type", "Characters").findAll()
        userFavorites.addChangeListener{ data -> adapter.addFavorites(data)}


    }

    private fun initQueryTextListener() {
        SEARCH.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (search == query)
                    return false
                search = query!!
                addItems(search = search)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                search = newText
                addItems(search = search)
                return false
            }
        })
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager) {
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {  // Use scrollListener.resetState() to reset list when searching
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addItems(page * 20, search) //multiply with how many items you showing in list
            }
        }
        RECYCLER.addOnScrollListener(scrollListener)
    }

    @SuppressLint("CheckResult")
    private fun addItems(offset: Int = 0, search: String) {
        if (offset == 0) {
            scrollListener.resetState()
            adapter.resetList()
            characterList = emptyList()
        }
        if (search.isEmpty()) {
            SnackbarManager().createSnackbar(view,  getString(R.string.loading_content),R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
            MarvelServiceHandler.charactersRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                characterList = characterList + data.data.results.asList()
                adapter.addItems(characterList)

            }
        } else {
            SnackbarManager().createSnackbar(view, getString(R.string.loading_content),R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
            MarvelServiceHandler.characterByNameRequest(offset,search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                characterList = characterList + data.data.results.asList()
                adapter.addItems(characterList)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun initAdapter() {
        MarvelServiceHandler.charactersRequest(0).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            characterList = characterList + data.data.results.asList()
            adapter = CharacterListAdapter(characterList, this, userFavorites)
            RECYCLER.adapter = adapter
            loadDialog!!.hideDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    private fun setClickListener() {
        homeButton1.setOnClickListener {
            activityHelper.changeActivity(this, MenuActivity::class.java)
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

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}

