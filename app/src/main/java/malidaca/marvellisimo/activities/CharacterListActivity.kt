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
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Character


class CharacterListActivity : AppCompatActivity() {
    private var ar: List<Character> = emptyList()
    private lateinit var adapter: CharacterListAdapter
    private var search: String = ""
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        val linearLayoutManager = LinearLayoutManager(this)
        RECYCLER.layoutManager = linearLayoutManager
        initAdapter()
        initScrollListener(linearLayoutManager)
        initQueryTextListener()
    }

    private fun initQueryTextListener() {
        SEARCH.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            MarvelServiceHandler.charactersRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                ar = ar + data.data.results.asList()
                adapter.addItems(ar)
            }
        } else {
            MarvelServiceHandler.characterXRequest(offset, search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
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
        }
    }
}

