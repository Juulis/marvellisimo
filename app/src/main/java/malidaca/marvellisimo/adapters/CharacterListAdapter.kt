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
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.models.Character

class CharacterListAdapter(private var characters: List<Character>, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.character_list_card, p0, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun addItems(list:List<Character>){
        characters = list
        notifyDataSetChanged()
    }

    fun resetList(){
        characters = emptyList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = characters[position].name.toUpperCase()
        createImage(characters[position], holder)

        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("itemId",characters[position].id )
                context.startActivity(intent)
            }

        })
    }

    fun createImage(character: Character, holder: ViewHolder){
        var url = "${character.thumbnail.path}/landscape_large.${character.thumbnail.extension}"
        url = url.replace("http", "https")
        Picasso.get().load(url).into(holder.img)
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val name = itemView.name!!
    val img = itemView.img!!
    private var itemClickListener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        this.itemClickListener!!.onCustomClickListener(v!!, adapterPosition)
    }
}

