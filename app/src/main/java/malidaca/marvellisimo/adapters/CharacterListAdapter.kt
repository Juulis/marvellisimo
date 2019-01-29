package malidaca.marvellisimo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.character_list_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.models.Character

class CharacterListAdapter(private val characters: Array<Character>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.character_list_card, p0, false))    }

    override fun getItemCount(): Int {
        return characters.size    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.name.text = characters[p1].name
    }


}
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name!!
        val imgview = view.imageView!!
    }
