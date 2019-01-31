package malidaca.marvellisimo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.ListCharacter
import malidaca.marvellisimo.models.Picture
import malidaca.marvellisimo.rest.series.SeriesService
import malidaca.marvellisimo.rest.MarvelServiceHandler

class CharacterListActivity : AppCompatActivity() {



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        MarvelServiceHandler.charactersRequest().observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
            RECYCLER.layoutManager = LinearLayoutManager(this)
            RECYCLER.adapter = CharacterListAdapter(data.data.results, this)
        }
    }
}
