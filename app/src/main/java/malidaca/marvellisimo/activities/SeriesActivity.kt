package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.adapters.SeriesViewAdapter
import malidaca.marvellisimo.models.Series

class SeriesActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: SeriesViewAdapter
    private var search: String = ""
    private var response: List<Series> = emptyList()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
        val viewManager = LinearLayoutManager(this)
        initScrollListener(viewManager)
        MarvelServiceHandler.seriesRequest(0).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
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
    }

    private fun initQueryTextListener() {
        SEARCH.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(search == query)
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
        }
        if (search.isEmpty()) {
            MarvelServiceHandler.seriesRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                response = response + data.data.results.asList()
                viewAdapter.addItems(response)
            }
        } else {
            MarvelServiceHandler.serieXRequest(offset, search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                response = response + data.data.results.asList()
                viewAdapter.addItems(response)
            }
        }
    }
}