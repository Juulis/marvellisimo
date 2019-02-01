package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.rest.MarvelServiceHandler
import android.support.v7.widget.RecyclerView
import malidaca.marvellisimo.models.Character


class CharacterListActivity : AppCompatActivity() {
    private var ar: List<Character> = emptyList()
    private lateinit var adapter: CharacterListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(malidaca.marvellisimo.R.layout.activity_character_list)
        val linearLayoutManager = LinearLayoutManager(this)
        RECYCLER.layoutManager = linearLayoutManager
        addItems(0) //init adapter

        val scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {  // Use scrollListener.resetState() to reset list when searching
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addItems(page*20) //multiply with how many items you showing in list
            }
        }
        RECYCLER.addOnScrollListener(scrollListener)
    }

    @SuppressLint("CheckResult")
    private fun addItems(offset: Int) {
        MarvelServiceHandler.charactersRequest(offset).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            ar = ar + data.data.results.asList()
            if (offset == 0) {
                adapter = CharacterListAdapter(ar, this)
                RECYCLER.adapter = adapter
            } else {
                adapter.addItems(ar)
            }
        }
    }
}
