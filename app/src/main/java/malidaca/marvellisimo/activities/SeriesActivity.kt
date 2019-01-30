package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_series.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler

class SeriesActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        button.setOnClickListener { view ->

            //TODO: make the response show on a recycleview
            val series = MarvelServiceHandler.request("series")
            for(s in series!!){
                var serie = s as Series
                println(serie.title)
            }
        }
    }
}