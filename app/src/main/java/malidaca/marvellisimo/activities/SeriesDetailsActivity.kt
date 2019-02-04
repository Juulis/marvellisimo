package malidaca.marvellisimo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_series_details.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Series
import malidaca.marvellisimo.rest.MarvelServiceHandler

class SeriesDetailsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_details)

        val id = intent.getIntExtra("id", 0)
        var response: Series

        MarvelServiceHandler.seriesByIdRequest(id).observeOn(AndroidSchedulers.mainThread()).subscribe {
            data -> response= data.data.results[0]
            var path = "${response.thumbnail.path}/landscape_incredible.${response.thumbnail.extension}"
            path = path.replace("http", "https")
            println(path)
            Picasso.get().load(path).resize(928, 522).into(series_picture)
            series_title.text = response.title
            series_description.text = response.description
            series_start_year.text = "Start year: " + response.startYear.toString()
            series_end_year.text = "End year: " + response.endYear.toString()
            series_rating.text = "Rating: " + response.rating
            val creators = response.creators.items
            var creatorsNames = ""
            for (cr in creators)
                creatorsNames += "${cr.name}, "
            series_creators.text = "Creators: $creatorsNames"
            val comics = response.comics.items
            var comicsTitles = ""
            for (co in comics)
                comicsTitles += "${co.name}, "
            series_comics.text = "Comics: $comicsTitles"
            val characters = response.characters.items


        }
    }
}