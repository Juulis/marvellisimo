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

class CharacterListAdapter(private val characters: Array<Character>, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.character_list_card, p0, false))
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = characters[position].name.toUpperCase()
        val url = "${characters[position].thumbnail.path}/landscape_large.${characters[position].thumbnail.extension}"

       var split1 =  url.subSequence(0,4)
       var split2 =  url.subSequence(4, url.length)
        val newUrl = "${split1}s$split2"

        Picasso.get().load(newUrl).into(holder.img)

        holder.setOnItemClickListener(object : ItemClickListener {
            override fun onCustomClickListener(view: View, pos: Int) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("itemId",characters[position].id )
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

