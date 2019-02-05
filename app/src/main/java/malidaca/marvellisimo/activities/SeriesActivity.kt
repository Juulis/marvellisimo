package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import io.reactivex.android.schedulers.AndroidSchedulers
import malidaca.marvellisimo.R
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.adapters.SeriesViewAdapter
import malidaca.marvellisimo.models.Series

class SeriesActivity : AppCompatActivity() {
    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var topToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
        topToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(topToolbar)

        var response: Array<Series>

        MarvelServiceHandler.seriesRequest().observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            response = data.data.results
            viewManager = LinearLayoutManager(this)
            viewAdapter = SeriesViewAdapter(response, this)
            recyclerView = findViewById<RecyclerView>(R.id.series_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}