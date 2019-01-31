package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.rest.MarvelServiceHandler

class CharacterListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        buildList()

        SEARCH.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                buildList(newText)
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    @SuppressLint("CheckResult")
    fun buildList(search: String = "") {
        if (search.equals("")) {
            MarvelServiceHandler.charactersRequest().observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                RECYCLER.layoutManager = LinearLayoutManager(this)
                RECYCLER.adapter = CharacterListAdapter(data.data.results, this)
            }
        }else{
            MarvelServiceHandler.characterXRequest(search).observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                RECYCLER.layoutManager = LinearLayoutManager(this)
                RECYCLER.adapter = CharacterListAdapter(data.data.results, this)
            }
        }
    }
}

