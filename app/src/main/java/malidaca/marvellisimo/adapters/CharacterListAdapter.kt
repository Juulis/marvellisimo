package malidaca.marvellisimo.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.character_list_card.view.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.activities.CharacterActivity
import malidaca.marvellisimo.activities.CharacterListActivity
import malidaca.marvellisimo.activities.ItemClickListener
import malidaca.marvellisimo.models.ListCharacter

class CharacterListAdapter(private val characters: ArrayList<ListCharacter>, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.character_list_card, p0, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.name.text = characters[p1].name.toUpperCase()
        Picasso.get().load(characters[p1].thumbnail.path).into(p0.img)
        p0.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("itemId",characters[p1].id )
                context.startActivity(intent)
            }

        })
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

