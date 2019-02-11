package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.content.Intent
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
import malidaca.marvellisimo.R
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

import malidaca.marvellisimo.utilities.LoadDialog
import malidaca.marvellisimo.utilities.SnackbarManager


class CharacterListActivity : AppCompatActivity() {
    private var ar: List<Character> = emptyList()
    private lateinit var adapter: CharacterListAdapter
    private var search: String = ""
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var view: View

    lateinit var topToolbar: Toolbar

    var loadDialog: LoadDialog? = null
    private lateinit var activityHelper: ActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            ar = emptyList()
        }
        if (search.isEmpty()) {
            SnackbarManager().createSnackbar(view,  getString(R.string.loading_content),R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
            MarvelServiceHandler.charactersRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                ar = ar + data.data.results.asList()
                adapter.addItems(ar)
            }
        } else {
            SnackbarManager().createSnackbar(view, getString(R.string.loading_content),R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
            MarvelServiceHandler.characterByNameRequest(offset,search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                ar = ar + data.data.results.asList()
                adapter.addItems(ar)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun initAdapter() {
        MarvelServiceHandler.charactersRequest(0).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            ar = ar + data.data.results.asList()
            adapter = CharacterListAdapter(ar, this)
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

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}

