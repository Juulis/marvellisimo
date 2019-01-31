package malidaca.marvellisimo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.models.Character
import malidaca.marvellisimo.models.ListCharacter
import malidaca.marvellisimo.models.Picture
import malidaca.marvellisimo.rest.MarvelService
import malidaca.marvellisimo.rest.MarvelServiceHandler

class CharacterListActivity : AppCompatActivity() {


    var characterList = arrayListOf<ListCharacter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

     val testList =  MarvelServiceHandler.request("character")
        for (data in testList!!) {
            characterList.add(data as ListCharacter)
        }

        RECYCLER.layoutManager = LinearLayoutManager(this)
        RECYCLER.adapter = CharacterListAdapter(characterList, this)

    }



}
