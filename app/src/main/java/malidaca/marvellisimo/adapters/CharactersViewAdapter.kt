package malidaca.marvellisimo.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_list_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.CharacterActivity
import malidaca.marvellisimo.models.Character

class CharactersViewAdapter(private var characters: Array<Character>, private val context: Context) : RecyclerView.Adapter<CharactersViewAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_list_card, parent, false)
        view.setOnClickListener(this)
        return CharactersViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = characters[position].id
        holder.name.text = characters[position].name
        createImage(characters[position], holder)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun createImage(character: Character, holder: ViewHolder){
        var url = "${character.thumbnail.path}/portrait_fantastic.${character.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).fit().into(holder.img)
    }


    override fun onClick(v: View?) {
        val id = v!!.tag as Int
        val intent = Intent(context, CharacterActivity::class.java)
        intent.putExtra("itemId", id)
        context.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name!!
        val img = itemView.img!!
    }

}