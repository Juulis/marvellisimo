package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler
import malidaca.marvellisimo.utilities.SeriesViewAdapter

class SeriesActivity: AppCompatActivity(){
    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
        viewManager = LinearLayoutManager(this)
        viewAdapter = SeriesViewAdapter(this, MarvelServiceHandler.sendRequest())
        recyclerView = findViewById<RecyclerView>(R.id.series_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


}