package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.Gravity
import android.view.View
import android.view.MenuItem
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.adapters.SeriesViewAdapter
import malidaca.marvellisimo.fragments.PeopleOnline
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.utilities.LoadDialog
import malidaca.marvellisimo.utilities.SnackbarManager
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.ActivityHelper

class SeriesActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var topToolbar: Toolbar
    lateinit var viewAdapter: SeriesViewAdapter
    private var search: String = ""
    private var response: List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var view: View
    private lateinit var activityHelper: ActivityHelper

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loadDialog = LoadDialog(this)
        loadDialog.showDialog()
        setContentView(R.layout.activity_series)
        view = findViewById(android.R.id.content)
        topToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(topToolbar)

        val viewManager = LinearLayoutManager(this)
        initScrollListener(viewManager)
        MarvelServiceHandler.seriesRequest(0)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    loadDialog.hideDialog()
                }.subscribe { data ->
                    response += data.data.results.asList()
                    viewAdapter = SeriesViewAdapter(response, this)
                    recyclerView = findViewById<RecyclerView>(R.id.series_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                        addOnScrollListener(scrollListener)
                    }
                }
        initQueryTextListener()
        setClickListener()
        activityHelper = ActivityHelper()
    }

    private fun initQueryTextListener() {
        SEARCH1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
    }

    @SuppressLint("CheckResult")
    private fun addItems(offset: Int = 0, search: String) {
        if (offset == 0) {
            scrollListener.resetState()
            viewAdapter.resetList()
            response = emptyList()
        } else {
            SnackbarManager().createSnackbar(view, "Loading content", R.color.colorPrimaryDarkTransparent, Gravity.BOTTOM)
        }
        if (search.isEmpty()) {
            MarvelServiceHandler.seriesRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                response = response + data.data.results.asList()
                viewAdapter.addItems(response)
            }
        } else {
            MarvelServiceHandler.serieByNameRequest(offset, search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                response = response + data.data.results.asList()
                viewAdapter.addItems(response)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun setClickListener() {
        homeButton2.setOnClickListener {
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