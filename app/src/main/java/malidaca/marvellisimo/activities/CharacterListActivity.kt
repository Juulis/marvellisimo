package malidaca.marvellisimo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_character_list.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.adapters.CharacterListAdapter
import malidaca.marvellisimo.models.ListCharacter
import malidaca.marvellisimo.models.Picture

class CharacterListActivity : AppCompatActivity() {


    var characterList = arrayListOf<ListCharacter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        characterList.add(ListCharacter(1,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(2,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))
        characterList.add(ListCharacter(3,"IronMan", Picture("https://www.hindustantimes.com/rf/image_size_960x540/HT/p2/2018/10/18/Pictures/_5fb51944-d2ee-11e8-841e-211dfd3178e1.jpg","vet ej")))


        RECYCLER.layoutManager = LinearLayoutManager(this)
        RECYCLER.adapter = CharacterListAdapter(characterList, this)

    }



}
