package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_series.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.rest.SeriesServiceHandler

class SeriesActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        button.setOnClickListener { view ->

            SeriesServiceHandler().sendRequest()
        }
    }



}