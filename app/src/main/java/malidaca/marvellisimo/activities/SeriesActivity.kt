package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.rest.MarvelServiceHandler

class SeriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        button.setOnClickListener { view ->

            MarvelServiceHandler.seriesRequest()


            /* //Exempel på användning
            var ar: Array<Character> = emptyArray()
            MarvelServiceHandler.charactersRequest().observeOn(AndroidSchedulers.mainThread()).subscribe { wrapper ->
                ar = wrapper.data.results
                for (a in ar) {
                    println("name: " + a.name)
                }
            }*/


        }
    }


}